package io.undertree.starter.ysqljpa.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.YugabyteDBYSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 *
 */
public class IntegrationTestExtension implements BeforeAllCallback, AfterAllCallback {
    static final DockerImageName YUGABYTEDB_IMAGE = DockerImageName.parse("yugabytedb/yugabyte:2.18.2.1-b1");
    static final String ENTRYPOINT = "bin/yugabyted start --background=false --tserver_flags=yb_enable_read_committed_isolation=true";

    static final YugabyteDBYSQLContainer ysqlDB = new YugabyteDBYSQLContainer(YUGABYTEDB_IMAGE)
            //.withInitScript()
            .withCommand(ENTRYPOINT);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        ysqlDB.start();

        // container.getContainerInfo().getNetworkSettings().getNetworks().entrySet().stream().findFirst().get().getValue().getIpAddress();

        System.setProperty("spring.datasource.url", ysqlDB.getJdbcUrl());
        System.setProperty("spring.datasource.username", ysqlDB.getUsername());
        System.setProperty("spring.datasource.password", ysqlDB.getPassword());
        System.setProperty("spring.datasource.driver-class-name", ysqlDB.getDriverClassName()); // needed for flyway
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // ysqlDB.stop();
        // ^ don't stop the container after each test class!
        // This assumes Ryuk will kill the YugabyteDB container later...
    }
}