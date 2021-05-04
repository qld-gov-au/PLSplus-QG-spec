package au.gov.qld.ssq.api.plsplus.qg;

import au.gov.qld.ssq.api.plsplus.qg.handler.ApiException;
import au.gov.qld.ssq.api.plsplus.qg.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
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
public class PlsPlusQgServiceExternalIntergationTest {

    @Autowired
    private PlsPlusQgService plsPlusQgService;

    @Tag("ExternalIntegrationTest")
    @Test
    public void autoCompleteAddressTest() throws ApiException {
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

    private OffsetDateTime getNowMinusOneSecond() {
        return OffsetDateTime.now(ZoneId.ofOffset("GMT", ZoneOffset.ofHours(10)))
            .minus(1L, ChronoUnit.SECONDS);
    }

    @Tag("ExternalIntegrationTest")
    @Test
    public void parseAddressTestSingle() throws ApiException {
        OffsetDateTime now = getNowMinusOneSecond();
        ParseAddressResult result = plsPlusQgService.parseAddress("27 PLUM PDE NERANG QLD 4211", false);
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
    @Test
    public void parseAddressTestMulti() throws ApiException {
        OffsetDateTime now = getNowMinusOneSecond();
        ParseAddressResult result = plsPlusQgService.parseAddress("u5/74 Wardoo St Ashmore", false);
        assertThat(result.getResultCount()).isEqualTo(17);
        Map<String, Result> sortedResults = new HashMap<>();
        //Because server sorts on something that is not sitename, the list is different (after cache wears off)
        for (Result r : result.getResults().getResult()) {
            sortedResults.put(r.getAddress().getSiteName(), r);
        }

        Result firstObject = sortedResults.get("BELLBIRD GROVE");
        assertThat(firstObject.getMetaData().size()).isEqualTo(2);
        assertThat(firstObject.getMetaData().get(0).getName()).isEqualTo("Timestamp");
        assertThat(OffsetDateTime.parse(firstObject.getMetaData().get(0).getValue())).isAfterOrEqualTo(now);
        assertThat(firstObject.getMetaData().get(1).getName()).isEqualTo("FullAddressString");
        assertThat(firstObject.getMetaData().get(1).getValue()).isEqualTo("UNIT 5 74 WARDOO ST, ASHMORE QLD 4214");
        assertThat(firstObject.getAddress().getRoadNumber().getFirst()).isEqualTo("74");
        assertThat(firstObject.getAddress().getRoadNumber().getLast()).isNull();
        assertThat(firstObject.getAddress().getRoad().getName()).isEqualTo("WARDOO");
        assertThat(firstObject.getAddress().getRoad().getTypeCode()).isEqualTo(Road.TypeCodeEnum.ST);
        assertThat(firstObject.getAddress().getRoad().getSuffix()).isNull();
        assertThat(firstObject.getAddress().getSiteName()).isEqualTo("BELLBIRD GROVE");
        assertThat(firstObject.getAddress().getUnit().getTypeCode()).isEqualTo(Unit.TypeCodeEnum.U);
        assertThat(firstObject.getAddress().getUnit().getNumber()).isEqualTo("5");
        assertThat(firstObject.getAddress().getLevel()).isNull();
        assertThat(firstObject.getAddress().getLocality()).isEqualTo("ASHMORE");
        assertThat(firstObject.getAddress().getState()).isEqualTo("QLD");
        assertThat(firstObject.getAddress().getPostcode()).isEqualTo("4214");
        assertThat(firstObject.getParcel().getLot()).isEqualTo("5");
        assertThat(firstObject.getParcel().getPlan()).isEqualTo("SP116750");
        assertThat(firstObject.getLocalGovernmentArea().getCode()).isEqualTo("3430");
        assertThat(firstObject.getLocalGovernmentArea().getName()).isEqualTo("GOLD COAST CITY");
        assertThat(firstObject.getGeocode().size()).isEqualTo(1);
        assertThat(firstObject.getGeocode().get(0).getTypeCode()).isEqualTo(Geocode.TypeCodeEnum.BC);
        assertThat(firstObject.getGeocode().get(0).getLatitude()).isEqualTo(BigDecimal.valueOf(-27.986808));
        assertThat(firstObject.getGeocode().get(0).getLongitude()).isEqualTo(BigDecimal.valueOf(153.389205));
        assertThat(firstObject.getAliases()).isNull();
        assertThat(firstObject.getConfidence()).isEqualTo(98);

//if needed, can fill in the middle results, note numbers have been clipped from this intellij copy so tweaking tests will need to be done
//1 = {Result@7009} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3065352+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: LORIKEET CIRCUIT\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: SP116750\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: BC\n latitude: -27.986164\n longitude: 153.389"
//2 = {Result@7010} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3065352+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: SANDPIPER PLACE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: SP116750\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: BC\n latitude: -27.983995\n longitude: 153.3900"
//3 = {Result@7011} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3115350+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: GARDENIA CLOSE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 8\n plan: BUP10833\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98710566\n longitude: 153.389"
//4 = {Result@7012} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3165353+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: JACARANDA CLOSE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP9324\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98721673\n longitude: 153.390"
//5 = {Result@7013} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3165353+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: WATTLE PLACE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP9302\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98731035\n longitude: 153.391382"
//6 = {Result@7014} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3215335+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: LOTUS SQUARE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP6169\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98645057\n longitude: 153.390257"
//7 = {Result@7015} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3265338+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: HIBISCUS SOUTH\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP8715\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98682927\n longitude: 153.3922"
//8 = {Result@7016} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3265338+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: HIBISCUS NORTH\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP8708\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.9863742\n longitude: 153.39162"
//9 = {Result@7017} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3315345+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: NERINE CLOSE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP6427\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98609108\n longitude: 153.391317"
//10 = {Result@7018} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3365351+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: ORCHID SQUARE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP6663\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98589089\n longitude: 153.39075"
//11 = {Result@7019} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3365351+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: BANKSIA CLOSE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: GTP4074\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98590969\n longitude: 153.38985"
//12 = {Result@7020} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3415358+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: MAGNOLIA CLOSE\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP103345\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98454209\n longitude: 153.39"
//13 = {Result@7021} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3465357+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: PALM COURT\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP7144\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98457493\n longitude: 153.39097296"
//14 = {Result@7022} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3515339+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: DOLPHIN SOUTH\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP8706\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98538473\n longitude: 153.39170"
//15 = {Result@7023} "class Result {\n physicalAddressIndicator: Y\n metaData: [class MetaData {\n name: Timestamp\n value: 2021-05-03T11:52:53.3515339+10:00\n }, class MetaData {\n name: FullAddressString\n value: UNIT 5 74 WARDOO ST, ASHMORE QLD 4214\n }]\n address: class Address {\n roadNumber: class RoadNumber {\n first: 74\n last: null\n }\n road: class Road {\n name: WARDOO\n typeCode: ST\n suffix: null\n }\n siteName: DOLPHIN NORTH\n unit: class Unit {\n typeCode: U\n number: 5\n }\n level: null\n locality: ASHMORE\n state: QLD\n postcode: 4214\n }\n parcel: class Parcel {\n lot: 5\n plan: BUP8707\n }\n localGovernmentArea: class LocalGovernmentArea {\n code: 3430\n name: GOLD COAST CITY\n }\n geocode: [class Geocode {\n typeCode: PC\n latitude: -27.98466131\n longitude: 153.39205"

        Result lastObject = sortedResults.get("ACACIA CLOSE");
        assertThat(lastObject.getMetaData().size()).isEqualTo(2);
        assertThat(lastObject.getMetaData().get(0).getName()).isEqualTo("Timestamp");
        assertThat(OffsetDateTime.parse(lastObject.getMetaData().get(0).getValue())).isAfterOrEqualTo(now);
        assertThat(lastObject.getMetaData().get(1).getName()).isEqualTo("FullAddressString");
        assertThat(lastObject.getMetaData().get(1).getValue()).isEqualTo("UNIT 5 74 WARDOO ST, ASHMORE QLD 4214");
        assertThat(lastObject.getAddress().getRoadNumber().getFirst()).isEqualTo("74");
        assertThat(lastObject.getAddress().getRoadNumber().getLast()).isNull();
        assertThat(lastObject.getAddress().getRoad().getName()).isEqualTo("WARDOO");
        assertThat(lastObject.getAddress().getRoad().getTypeCode()).isEqualTo(Road.TypeCodeEnum.ST);
        assertThat(lastObject.getAddress().getRoad().getSuffix()).isNull();
        assertThat(lastObject.getAddress().getSiteName()).isEqualTo("ACACIA CLOSE");
        assertThat(lastObject.getAddress().getUnit().getTypeCode()).isEqualTo(Unit.TypeCodeEnum.U);
        assertThat(lastObject.getAddress().getUnit().getNumber()).isEqualTo("5");
        assertThat(lastObject.getAddress().getLevel()).isNull();
        assertThat(lastObject.getAddress().getLocality()).isEqualTo("ASHMORE");
        assertThat(lastObject.getAddress().getState()).isEqualTo("QLD");
        assertThat(lastObject.getAddress().getPostcode()).isEqualTo("4214");
        assertThat(lastObject.getParcel().getLot()).isEqualTo("5");
        assertThat(lastObject.getParcel().getPlan()).isEqualTo("BUP9763");
        assertThat(lastObject.getLocalGovernmentArea().getCode()).isEqualTo("3430");
        assertThat(lastObject.getLocalGovernmentArea().getName()).isEqualTo("GOLD COAST CITY");
        assertThat(lastObject.getGeocode().size()).isEqualTo(1);
        assertThat(lastObject.getGeocode().get(0).getTypeCode()).isEqualTo(Geocode.TypeCodeEnum.PC);
        assertThat(lastObject.getGeocode().get(0).getLatitude()).isEqualTo(BigDecimal.valueOf(-27.98400494));
        assertThat(lastObject.getGeocode().get(0).getLongitude()).isEqualTo(BigDecimal.valueOf(153.39179986));
        assertThat(lastObject.getAliases()).isNull();
        assertThat(lastObject.getConfidence()).isEqualTo(98);
    }

    @Tag("ExternalIntegrationTest")
    @Test
    public void shouldGetAddressFromCoodinatesPassedIn() throws ApiException {
        OffsetDateTime now = getNowMinusOneSecond();
        ValidateCoordinatesResult result = plsPlusQgService.validateCoordinates(BigDecimal.valueOf(-28.00323935), BigDecimal.valueOf(153.33099322), false);
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
    public void expectApiExcaptionFromValidateCoordinatesWithInvalidLongitudeCoord() throws ApiException {
        Assertions.assertThrows(ApiException.class, () -> {
            try {
                plsPlusQgService.validateCoordinates(BigDecimal.valueOf(-128.00323935), BigDecimal.valueOf(180.33099322), false);
            } catch (ApiException e) {
                assertThat(e.getResponseBody()).contains("s:InvalidRequest");
                assertThat(e.getResponseBody()).contains("Invalid Longitude");
                throw e;
            }
        });
    }

    @Tag("ExternalIntegrationTest")
    @Test()
    public void expectApiExcaptionFromValidateCoordinatesWithInvalidLatCoord() throws ApiException {
        Assertions.assertThrows(ApiException.class, () -> {
            try {
                plsPlusQgService.validateCoordinates(BigDecimal.valueOf(-188.00323935), BigDecimal.valueOf(120.33099322), false);
            } catch (ApiException e) {
                assertThat(e.getResponseBody()).contains("s:InvalidRequest");
                assertThat(e.getResponseBody()).contains("Invalid Latitude");
                throw e;
            }
        });
    }

    @Tag("ExternalIntegrationTest")
    @Test
    public void shouldHaveEmptyResultSetforGetAddressFromCoodinatesWithCoordOutsideQld() throws ApiException {
        ValidateCoordinatesResult result = plsPlusQgService.validateCoordinates(BigDecimal.valueOf(-28.00323935), BigDecimal.valueOf(154), false);
        assertThat(result.getResultCount()).isEqualTo(0);
        assertThat(result.getResults()).isNull();
    }
}
