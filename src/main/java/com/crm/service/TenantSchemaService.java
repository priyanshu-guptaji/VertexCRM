package com.crm.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantSchemaService {

    private final JdbcTemplate jdbcTemplate;

    public TenantSchemaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void provisionSchema(String schema) {
        String normalized = normalize(schema);
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + normalized);
        // Optionally create extensions, sequences, or seed data here.
    }

    private String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[^a-z0-9_]+", "_");
    }
}
