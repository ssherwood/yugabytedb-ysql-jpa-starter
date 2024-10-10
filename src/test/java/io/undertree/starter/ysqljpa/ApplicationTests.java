package io.undertree.starter.ysqljpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class ApplicationTests {

    @Test
    void contextLoads() {
        // This is a basic Spring bean "wiring" test and intentionally excludes datasource configurations
        // if this fails, it usually means a configuration error of some kind
    }

}
