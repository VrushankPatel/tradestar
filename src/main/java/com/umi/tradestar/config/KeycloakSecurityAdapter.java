package com.umi.tradestar.config;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Keycloak security adapter configuration.
 * This class provides custom security configurations for Keycloak integration.
 * This configuration is only active in non-test profiles.
 *
 * @author VrushankPatel
 */
@KeycloakConfiguration
@Profile("!test")
public class KeycloakSecurityAdapter {

    /**
     * Creates a KeycloakAuthenticationProvider with proper role mappings.
     * This bean will be used by SecurityConfig to configure the AuthenticationManager.
     */
    @Bean
    @Primary
    public KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setPrefix(""); // Remove default ROLE_ prefix
        provider.setGrantedAuthoritiesMapper(authorityMapper);
        return provider;
    }
    
    /**
     * Defines the session authentication strategy.
     * Uses RegisterSessionAuthenticationStrategy for public or confidential applications.
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Create AdapterDeploymentContext bean
     */
    @Bean
    public AdapterDeploymentContext adapterDeploymentContext(KeycloakConfigResolver keycloakConfigResolver) {
        return new AdapterDeploymentContext(keycloakConfigResolver);
    }

    /**
     * Create KeycloakAuthenticationProcessingFilter bean
     */
    @Bean
    public KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() {
        return new KeycloakAuthenticationProcessingFilter(authenticationManager -> authenticationManager);
    }

    /**
     * Create KeycloakPreAuthActionsFilter bean
     */
    @Bean
    public KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
        return new KeycloakPreAuthActionsFilter();
    }

    /**
     * Create KeycloakSecurityContextRequestFilter bean
     */
    @Bean
    public KeycloakSecurityContextRequestFilter keycloakSecurityContextRequestFilter() {
        return new KeycloakSecurityContextRequestFilter();
    }

    /**
     * Prevent double registration of Keycloak filters
     */
    @Bean
    public FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Prevent double registration of Keycloak pre-auth actions filter
     */
    @Bean
    public FilterRegistrationBean<KeycloakPreAuthActionsFilter> keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean<KeycloakPreAuthActionsFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
    
    /**
     * Register the KeycloakSecurityContextRequestFilter
     */
    @Bean
    public FilterRegistrationBean<KeycloakSecurityContextRequestFilter> keycloakSecurityContextRequestFilterBean(
            KeycloakSecurityContextRequestFilter filter) {
        FilterRegistrationBean<KeycloakSecurityContextRequestFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Required for session invalidation in logout scenarios
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    /**
     * Use Spring Boot property resolver for Keycloak configuration
     */
    @Bean
    @Primary
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}