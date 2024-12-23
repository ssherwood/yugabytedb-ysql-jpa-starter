package io.undertree.starter.ysqljpa.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.YugabyteDBYSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 *
 */
public class IntegrationTestExtension implements BeforeAllCallback, AfterAllCallback {
    static final DockerImageName YUGABYTEDB_IMAGE = DockerImageName.parse("yugabytedb/yugabyte:2024.2.0.0-b145");
    static final String ENTRYPOINT = "bin/yugabyted start --background=false --tserver_flags=yb_enable_read_committed_isolation=true";

    static final YugabyteDBYSQLContainer ysqlDB = new YugabyteDBYSQLContainer(YUGABYTEDB_IMAGE)
            .withCommand(ENTRYPOINT)
            //.withInitScript() <- in case you want to provide an init script to set up the database
            .withReuse(false);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        ysqlDB.start();

        // container.getContainerInfo().getNetworkSettings().getNetworks().entrySet().stream().findFirst().get().getValue().getIpAddress();

        System.setProperty("spring.datasource.url", ysqlDB.getJdbcUrl());
        System.setProperty("spring.datasource.username", ysqlDB.getUsername());
        System.setProperty("spring.datasource.password", ysqlDB.getPassword());
        System.setProperty("spring.datasource.driver-class-name", ysqlDB.getDriverClassName()); // needed for flyway

        System.setProperty("spring.flyway.user", ysqlDB.getUsername());
        System.setProperty("spring.flyway.password", ysqlDB.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // ysqlDB.stop();
        // ^ don't stop the container after each test class!
        // This assumes Ryuk will kill the YugabyteDB container later...
    }
}