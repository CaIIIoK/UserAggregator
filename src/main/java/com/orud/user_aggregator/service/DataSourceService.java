package com.orud.user_aggregator.service;

import com.orud.user_aggregator.config.DataSourceConfig;
import com.orud.user_aggregator.config.DataSourceProperties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSourceService {
    private final List<DataSourceConfig> dataSourceConfigs;

    @Autowired
    public DataSourceService(DataSourceProperties dataSourceProperties) {
        this.dataSourceConfigs = dataSourceProperties.getDataSources();
    }

    @PostConstruct
    public void init() {
        if (dataSourceConfigs == null) {
            throw new IllegalStateException("DataSourceConfigs is null!");
        }
    }

    public List<DataSource> getDataSources() {
        List<DataSource> dataSources = new ArrayList<>();
        for (DataSourceConfig config : dataSourceConfigs) {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(getDriverClassName(config.getStrategy()));
            dataSource.setUrl(config.getUrl());
            dataSource.setUsername(config.getUser());
            dataSource.setPassword(config.getPassword());
            dataSources.add(dataSource);
        }
        return dataSources;
    }

    private String getDriverClassName(String strategy) {
        if ("postgres".equalsIgnoreCase(strategy)) {
            return "org.postgresql.Driver";
        } else if ("mysql".equalsIgnoreCase(strategy)) {
            return "com.mysql.cj.jdbc.Driver";
        } else if ("oracle".equalsIgnoreCase(strategy)) {
            return "oracle.jdbc.OracleDriver";
        } else {
            throw new IllegalArgumentException("Unknown database strategy: " + strategy);
        }
    }

    public List<DataSourceConfig> getDataSourceConfigs() {
        return dataSourceConfigs;
    }
}