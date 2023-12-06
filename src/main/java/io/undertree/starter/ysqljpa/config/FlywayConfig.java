package io.undertree.starter.ysqljpa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * FlywayConfig Background:
 * <p>
 * Flyway is designed to use at least two connections, one for the metadata
 * table and one for the migrations. If the migration flow modifies the
 * system catalog, queries in the metadata session will fail with the
 * catalog snapshot exception because Flyway keeps the same transaction for
 * the metadata connection. It needs to be retried.
 * </p>
 *
 * <p>
 * NOTE: this means that migrations need to be written in an idempotent
 * fashion (e.g. CREATE ... IF NOT EXISTS ...).
 * </p>
 */
@Configuration
public class FlywayConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayConfig.class);

    private final RetryTemplate retryTemplate;

    public FlywayConfig(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> retryTemplate.execute(ctx -> flyway.migrate());
    }
}