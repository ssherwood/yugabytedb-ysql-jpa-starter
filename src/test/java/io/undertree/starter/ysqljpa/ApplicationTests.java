package io.undertree.starter.ysqljpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"testcontainer-defaults"})
class ApplicationTests {

	@Test
	void contextLoads() {
		// TODO this is an expensive "nothing" test because it launches a test container
	}

}
