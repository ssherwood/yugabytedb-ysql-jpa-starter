package io.undertree.starter.ysqljpa;

import io.undertree.starter.ysqljpa.extensions.IntegrationTestExtension;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.CRC32;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles({"test"})
@ExtendWith(IntegrationTestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
public class FlywayMigrationTests {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    /**
     * Simple test that validates that the Flyway schema migration is done successfully
     * by selecting a count of rows on the flyway_schema_history table.
     * <p>
     * <i>NOTES:</i>
     * <ul>
     * <li>Flyway "clean" needs to be enabled for this to work correctly (see spring.flyway.clean-disabled=true).</li>
     * <li>Requires a running database!!!  This test will effective drop and re-create the database per Flyway</li>
     * </ul>
     */
    @Test
    @FlywayTest(locationsForMigrate = {"/db/testing"})
    @Order(1)
    void runMigration_SelectCountFromFlywaySchemaHistory() throws Exception {

        // determine if any migrations tripped breaking version
        // refer to src/main/resources/db/migration/README.md
        var breakingVersion = template.queryForObject("select last_breaking_version from pg_yb_catalog_version where db_oid = (select oid from pg_database where datname = current_database())", Integer.class);
        assertThat("Flyway Migration contained a catalog breaking feature!", breakingVersion, is(1));

        // count the number of successful migrations in the flyway_schema_history table
        // this should match the number of migration files in db/migration plus db/testing (as included above)
        // plus 1 if you have flyway creating the schema
        var migrationCount = template.queryForObject("select count(*) from flyway_schema_history where success = true", Integer.class);
        assertThat(migrationCount, is(8));

        // for each matching migration file, confirm that it matches the flyway_schema_history checksum
        Arrays.stream(resourcePatternResolver.getResources("classpath*:db/**/V*.sql"))
                .forEach(r -> {
                    var checksum = template.queryForObject("select checksum from flyway_schema_history where script = ?", Integer.class, r.getFilename());
                    assertThat(String.format("Flyway checksum did not match for '%s'", r.getFilename()),
                            checksum, is(calculateFlywayChecksum(r)));
                });
    }

    /*
     * Emulated Flyway checksum calculator (as we did not want to depend on Flyway internal classes).
     */
    private int calculateFlywayChecksum(Resource resource) {
        var crc32 = new CRC32();
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()), 4096)) {
            reader.lines()
                    .map(l -> l.replaceFirst("^\uFEFF", "")) // remove BOM
                    .map(l -> l.getBytes(StandardCharsets.UTF_8))
                    .forEach(crc32::update);
        } catch (Exception e) {
            throw new RuntimeException("Unable to calculate checksum of " + resource.getFilename(), e);
        }
        return (int) crc32.getValue();
    }
}