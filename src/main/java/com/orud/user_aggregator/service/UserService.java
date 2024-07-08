package com.orud.user_aggregator.service;

import com.orud.user_aggregator.config.DataSourceConfig;
import com.orud.user_aggregator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final DataSourceService dataSourceService;

    @Autowired
    public UserService(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    public List<User> getAllUsers(String filterUsername, String filterName, String filterSurname) {
        List<User> users = new ArrayList<>();
        List<DataSource> dataSources = dataSourceService.getDataSources();
        List<DataSourceConfig> dataSourceConfigs = dataSourceService.getDataSourceConfigs();

        for (int i = 0; i < dataSources.size(); i++) {
            DataSource dataSource = dataSources.get(i);
            DataSourceConfig config = dataSourceConfigs.get(i);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            StringBuilder queryBuilder = new StringBuilder("SELECT ")
                    .append(String.join(", ", config.getMapping().values()))
                    .append(" FROM ").append(config.getTable())
                    .append(" WHERE 1=1");

            List<Object> params = new ArrayList<>();

            // Add filters
            if (filterUsername != null && !filterUsername.isEmpty()) {
                queryBuilder.append(" AND ").append(config.getMapping().get("username"))
                        .append(" LIKE ?");
                params.add("%" + filterUsername + "%");
            }
            if (filterName != null && !filterName.isEmpty()) {
                queryBuilder.append(" AND ").append(config.getMapping().get("name"))
                        .append(" LIKE ?");
                params.add("%" + filterName + "%");
            }
            if (filterSurname != null && !filterSurname.isEmpty()) {
                queryBuilder.append(" AND ").append(config.getMapping().get("surname"))
                        .append(" LIKE ?");
                params.add("%" + filterSurname + "%");
            }

            String query = queryBuilder.toString();

            try {
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params.toArray());

                for (Map<String, Object> row : rows) {
                    User user = new User();
                    user.setId(row.get(config.getMapping().get("id")).toString());
                    user.setUsername(row.get(config.getMapping().get("username")).toString());
                    user.setName(row.get(config.getMapping().get("name")).toString());
                    user.setSurname(row.get(config.getMapping().get("surname")).toString());
                    users.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return users;
    }
}