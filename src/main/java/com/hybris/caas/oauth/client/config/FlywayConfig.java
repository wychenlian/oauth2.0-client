package com.hybris.caas.oauth.client.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by I308493 on 8/10/2017.
 */

@Configuration
public class FlywayConfig
{

    @Value("${db.clean-on-startup:false}")
    private boolean cleanOnStartup;

    @Bean
    public FlywayMigrationStrategy devFlywayMigrationStrategy() {
        return flyway -> {
            // Clean up db schema if property is set to true.
            if (cleanOnStartup) {
                flyway.clean();
            }

            // Give each location its own flyway metadata table.
            final String[] locations = flyway.getLocations();
            for(int i = 0; i < locations.length; i++) {
                flyway.setLocations(locations[i]);
                flyway.migrate();

            }
        };
    }
}
