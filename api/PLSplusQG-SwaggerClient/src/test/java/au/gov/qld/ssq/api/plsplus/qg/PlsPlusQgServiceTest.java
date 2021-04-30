package au.gov.qld.ssq.api.plsplus.qg;

import au.gov.qld.ssq.api.plsplus.qg.handler.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PlsPlusQgConfig.class, PlsPlusQgService.class},
    initializers = ConfigDataApplicationContextInitializer.class)
public class PlsPlusQgServiceTest {

    @Autowired
    private PlsPlusQgService plsPlusQgService;

    @Test
    public void simpleStartTest() throws ApiException {
        List<String> output = plsPlusQgService.autoCompleteAddress("27 plu");
        assertThat(output.get(0)).isEqualTo("27 PLUCKS RD ARANA HILLS QLD 4054");
        assertThat(output.get(1)).isEqualTo("27 PLUM PDE NERANG QLD 4211");
        assertThat(output.get(2)).isEqualTo("27 PLUM ST RUNCORN QLD 4113");
        assertThat(output.get(3)).isEqualTo("27 PLUM PINE ST MAUDSLAND QLD 4210");
        assertThat(output.get(4)).isEqualTo("27 PLUM TREE CR MOORE PARK BEACH QLD 4670");
        assertThat(output.get(5)).isEqualTo("27 PLUMBS RD TANAH MERAH QLD 4128");
        assertThat(output.get(6)).isEqualTo("27 PLUMER ST SHERWOOD QLD 4075");
        assertThat(output.get(7)).isEqualTo("27 PLUMERIA PL DREWVALE QLD 4116");
        assertThat(output.get(8)).isEqualTo("27 PLUMMER CR MANGO HILL QLD 4509");
        assertThat(output.get(9)).isEqualTo("27 PLUMRIDGE ST CHELMER QLD 4068");
        assertThat(output.get(10)).isEqualTo("27 PLUNKETT ST PADDINGTON QLD 4064");
        assertThat(output.get(11)).isEqualTo("27 PLUNKETT ST WOODRIDGE QLD 4114");
    }
}
