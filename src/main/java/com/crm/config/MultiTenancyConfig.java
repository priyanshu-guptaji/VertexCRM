package com.crm.config;

import com.crm.tenant.CurrentTenantIdentifierResolverImpl;
import com.crm.tenant.SchemaMultiTenantConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

@Configuration
public class MultiTenancyConfig {

    @Autowired
    private SchemaMultiTenantConnectionProvider connectionProvider;

    @Bean
    public CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(CurrentTenantIdentifierResolverImpl resolver) {
        return (props) -> {
            // Hibernate 6 property keys as strings for portability
            props.put("hibernate.multiTenancy", "SCHEMA");
            props.put("hibernate.multi_tenant_connection_provider", connectionProvider);
            props.put("hibernate.tenant_identifier_resolver", resolver);
        };
    }
}
