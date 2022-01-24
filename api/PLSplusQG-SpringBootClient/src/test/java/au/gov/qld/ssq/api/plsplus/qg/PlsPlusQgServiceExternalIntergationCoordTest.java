package au.gov.qld.ssq.api.plsplus.qg;

import au.gov.qld.ssq.api.plsplus.qg.handler.ApiException;
import au.gov.qld.ssq.api.plsplus.qg.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {PlsPlusQgConfig.class, PlsPlusQgService.class},
    initializers = ConfigDataApplicationContextInitializer.class)
public class PlsPlusQgServiceExternalIntergationCoordTest {

    @Autowired
    private PlsPlusQgService plsPlusQgService;

    @Tag("ExternalIntegrationTest")
    @Test
    public void shouldGetAddressFromCoodinatesPassedInViaPost() throws ApiException {
        OffsetDateTime now = getNowMinusOneSecond();
        ValidateCoordinatesResult result = plsPlusQgService.validateCoordinatesViaPost(BigDecimal.valueOf(-28.00323935), BigDecimal.valueOf(153.33099322), false);
        assertThat(result.getResultCount()).isEqualTo(1);
        Result firstObject = result.getResults().getResult().get(0);
        assertThat(firstObject.getMetaData().size()).isEqualTo(3);
        assertThat(firstObject.getMetaData().get(0).getName()).isEqualTo("Timestamp");
        assertThat(OffsetDateTime.parse(firstObject.getMetaData().get(0).getValue())).isAfterOrEqualTo(now);
        assertThat(firstObject.getMetaData().get(1).getName()).isEqualTo("FullAddressString");
        assertThat(firstObject.getMetaData().get(1).getValue()).isEqualTo("27 PLUM PDE, NERANG QLD 4211");
        assertThat(firstObject.getAddress().getRoadNumber().getFirst()).isEqualTo("27");
        assertThat(firstObject.getAddress().getRoadNumber().getLast()).isNull();
        assertThat(firstObject.getAddress().getRoad().getName()).isEqualTo("PLUM");
        assertThat(firstObject.getAddress().getRoad().getTypeCode()).isEqualTo(Road.TypeCodeEnum.PDE);
        assertThat(firstObject.getAddress().getRoad().getSuffix()).isNull();
        assertThat(firstObject.getAddress().getSiteName()).isNull();
        assertThat(firstObject.getAddress().getUnit()).isNull();
        assertThat(firstObject.getAddress().getLevel()).isNull();
        assertThat(firstObject.getAddress().getLocality()).isEqualTo("NERANG");
        assertThat(firstObject.getAddress().getState()).isEqualTo("QLD");
        assertThat(firstObject.getAddress().getPostcode()).isEqualTo("4211");
        assertThat(firstObject.getParcel().getLot()).isEqualTo("123");
        assertThat(firstObject.getParcel().getPlan()).isEqualTo("RP182442");
        assertThat(firstObject.getLocalGovernmentArea().getCode()).isEqualTo("3430");
        assertThat(firstObject.getLocalGovernmentArea().getName()).isEqualTo("GOLD COAST CITY");
        assertThat(firstObject.getGeocode().size()).isEqualTo(1);
        assertThat(firstObject.getGeocode().get(0).getTypeCode()).isEqualTo(Geocode.TypeCodeEnum.PC);
        assertThat(firstObject.getGeocode().get(0).getLatitude()).isEqualTo(BigDecimal.valueOf(-28.00323935));
        assertThat(firstObject.getGeocode().get(0).getLongitude()).isEqualTo(BigDecimal.valueOf(153.33099322));
        assertThat(firstObject.getAliases()).isNull();
        assertThat(firstObject.getConfidence()).isEqualTo(100);
    }
    @Tag("ExternalIntegrationTest")
    @Test
    public void shouldGetAddressFromCoodinatesPassedInViaGet() throws ApiException {
        OffsetDateTime now = getNowMinusOneSecond();
        ValidateCoordinatesResult result = plsPlusQgService.validateCoordinatesViaGet(BigDecimal.valueOf(-28.00323935), BigDecimal.valueOf(153.33099322));
        assertThat(result.getResultCount()).isEqualTo(1);
        Result firstObject = result.getResults().getResult().get(0);
        assertThat(firstObject.getMetaData().size()).isEqualTo(2);
        assertThat(firstObject.getMetaData().get(0).getName()).isEqualTo("Timestamp");
        assertThat(OffsetDateTime.parse(firstObject.getMetaData().get(0).getValue())).isAfterOrEqualTo(now);
        assertThat(firstObject.getMetaData().get(1).getName()).isEqualTo("FullAddressString");
        assertThat(firstObject.getMetaData().get(1).getValue()).isEqualTo("27 PLUM PDE, NERANG QLD 4211");
        assertThat(firstObject.getAddress().getRoadNumber().getFirst()).isEqualTo("27");
        assertThat(firstObject.getAddress().getRoadNumber().getLast()).isNull();
        assertThat(firstObject.getAddress().getRoad().getName()).isEqualTo("PLUM");
        assertThat(firstObject.getAddress().getRoad().getTypeCode()).isEqualTo(Road.TypeCodeEnum.PDE);
        assertThat(firstObject.getAddress().getRoad().getSuffix()).isNull();
        assertThat(firstObject.getAddress().getSiteName()).isNull();
        assertThat(firstObject.getAddress().getUnit()).isNull();
        assertThat(firstObject.getAddress().getLevel()).isNull();
        assertThat(firstObject.getAddress().getLocality()).isEqualTo("NERANG");
        assertThat(firstObject.getAddress().getState()).isEqualTo("QLD");
        assertThat(firstObject.getAddress().getPostcode()).isEqualTo("4211");
        assertThat(firstObject.getParcel().getLot()).isEqualTo("123");
        assertThat(firstObject.getParcel().getPlan()).isEqualTo("RP182442");
        assertThat(firstObject.getLocalGovernmentArea().getCode()).isEqualTo("3430");
        assertThat(firstObject.getLocalGovernmentArea().getName()).isEqualTo("GOLD COAST CITY");
        assertThat(firstObject.getGeocode().size()).isEqualTo(1);
        assertThat(firstObject.getGeocode().get(0).getTypeCode()).isEqualTo(Geocode.TypeCodeEnum.PC);
        assertThat(firstObject.getGeocode().get(0).getLatitude()).isEqualTo(BigDecimal.valueOf(-28.00323935));
        assertThat(firstObject.getGeocode().get(0).getLongitude()).isEqualTo(BigDecimal.valueOf(153.33099322));
        assertThat(firstObject.getAliases()).isNull();
        assertThat(firstObject.getConfidence()).isEqualTo(100);
    }

    @Tag("ExternalIntegrationTest")
    @Test()
    public void expectApiExcaptionFromValidateCoordinatesWithInvalidLongitudeCoordViaGet() throws ApiException {
        Assertions.assertThrows(ApiException.class, () -> {
            try {
                plsPlusQgService.validateCoordinatesViaGet(BigDecimal.valueOf(-128.00323935), BigDecimal.valueOf(180.33099322));
            } catch (ApiException e) {
                assertThat(e.getResponseBody()).contains("s:InvalidRequest");
                assertThat(e.getResponseBody()).contains("Invalid Longitude");
                throw e;
            }
        });
    }

    @Tag("ExternalIntegrationTest")
    @Test()
    public void expectApiExcaptionFromValidateCoordinatesWithInvalidLatCoordViaGet() throws ApiException {
        Assertions.assertThrows(ApiException.class, () -> {
            try {
                plsPlusQgService.validateCoordinatesViaGet(BigDecimal.valueOf(-188.00323935), BigDecimal.valueOf(120.33099322));
            } catch (ApiException e) {
                assertThat(e.getResponseBody()).contains("s:InvalidRequest");
                assertThat(e.getResponseBody()).contains("Invalid Latitude");
                throw e;
            }
        });
    }

    @Tag("ExternalIntegrationTest")
    @Test
    public void shouldHaveEmptyResultSetforGetAddressFromCoodinatesWithCoordOutsideQldViaGet() throws ApiException {
        ValidateCoordinatesResult result = plsPlusQgService.validateCoordinatesViaGet(BigDecimal.valueOf(-28.00323935), BigDecimal.valueOf(154));
        assertThat(result.getResultCount()).isEqualTo(0);
        assertThat(result.getResults()).isNull();
    }

    ///

    @Tag("ExternalIntegrationTest")
    @Test()
    public void expectApiExcaptionFromValidateCoordinatesWithInvalidLongitudeCoordViaPost() throws ApiException {
        Assertions.assertThrows(ApiException.class, () -> {
            try {
                plsPlusQgService.validateCoordinatesViaPost(BigDecimal.valueOf(-128.00323935), BigDecimal.valueOf(180.33099322), false);
            } catch (ApiException e) {
                assertThat(e.getResponseBody()).contains("s:InvalidRequest");
                assertThat(e.getResponseBody()).contains("Invalid Longitude");
                throw e;
            }
        });
    }

    @Tag("ExternalIntegrationTest")
    @Test()
    public void expectApiExcaptionFromValidateCoordinatesWithInvalidLatCoordViaPost() throws ApiException {
        Assertions.assertThrows(ApiException.class, () -> {
            try {
                plsPlusQgService.validateCoordinatesViaPost(BigDecimal.valueOf(-188.00323935), BigDecimal.valueOf(120.33099322), false);
            } catch (ApiException e) {
                assertThat(e.getResponseBody()).contains("s:InvalidRequest");
                assertThat(e.getResponseBody()).contains("Invalid Latitude");
                throw e;
            }
        });
    }

    @Tag("ExternalIntegrationTest")
    @Test
    public void shouldHaveEmptyResultSetforGetAddressFromCoodinatesWithCoordOutsideQldViaPost() throws ApiException {
        ValidateCoordinatesResult result = plsPlusQgService.validateCoordinatesViaPost(BigDecimal.valueOf(-28.00323935), BigDecimal.valueOf(154), false);
        assertThat(result.getResultCount()).isEqualTo(0);
        assertThat(result.getResults()).isNull();
    }

    private OffsetDateTime getNowMinusOneSecond() {
        return OffsetDateTime.now(ZoneId.ofOffset("GMT", ZoneOffset.ofHours(10)))
            .minus(1L, ChronoUnit.SECONDS);
    }
}
