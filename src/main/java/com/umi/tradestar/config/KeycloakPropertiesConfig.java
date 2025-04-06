package com.umi.tradestar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class to load Keycloak properties from the keycloak.properties file.
 * This ensures that all Keycloak-related properties are properly loaded into the Spring context.
 *
 * @author VrushankPatel
 */
@Configuration
@PropertySource("classpath:keycloak.properties")
public class KeycloakPropertiesConfig {
    // This class doesn't need any methods as it simply loads the properties file
    // Spring will automatically load the properties defined in the specified file
}