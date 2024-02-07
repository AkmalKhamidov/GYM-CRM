package com.epamlearning.actuator.healthchecks;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "database")
//@ConditionalOnEnabledHealthIndicator("database")
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Autowired
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    @Override
    @ReadOperation
    public Health health() {
        if (dataSource.getConnection().isValid(30)) {

            Map<String, Object> details = new HashMap<>();
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            details.put("databaseVendor", metaData.getDatabaseProductName());
            details.put("databaseVersion", metaData.getDatabaseProductVersion());
            details.put("max connections", metaData.getMaxConnections());
            details.put("driverName", metaData.getDriverName());
            details.put("user", metaData.getUserName());
            details.put("url", metaData.getURL());

            return Health.up().withDetails(details).build();
        } else {
            return Health.down().build();
        }
    }
}
