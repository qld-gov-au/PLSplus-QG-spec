package au.gov.qld.ssq.api.plsplus.qg;

import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PlsPlusQgConfig.class, PlsPlusQgService.class},
    initializers = ConfigDataApplicationContextInitializer.class)
public class PlsPlusQgServiceTest {

    @Autowired
    private PlsPlusQgConfig plusQgConfig;

    @Tag("FastTest")
    @Test
    public void springBootBooted() {
        assertThat(plusQgConfig).isNotNull();
    }
}
