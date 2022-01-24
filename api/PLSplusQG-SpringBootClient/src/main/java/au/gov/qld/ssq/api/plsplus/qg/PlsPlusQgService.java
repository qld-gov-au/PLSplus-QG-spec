package au.gov.qld.ssq.api.plsplus.qg;

import au.gov.qld.ssq.api.plsplus.qg.handler.ApiClient;
import au.gov.qld.ssq.api.plsplus.qg.handler.ApiException;
import au.gov.qld.ssq.api.plsplus.qg.handler.Configuration;
import au.gov.qld.ssq.api.plsplus.qg.handler.auth.HttpBasicAuth;
import au.gov.qld.ssq.api.plsplus.qg.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PlsPlusQgService {
    private Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public PlsPlusQgService(PlsPlusQgConfig config) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        if (config.getOrigin() != null) {
            //Required to set a restricted header.
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            defaultClient.addDefaultHeader("origin", config.getOrigin());
        }
        if (config.getApiKey() != null) {
            defaultClient.setApiKey(config.getApiKey());
        } else if (config.getPassword() == null || config.getUsername() == null) {
            // Configure HTTP basic authorization: BasicAuth
            HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
            basicAuth.setUsername(config.getUsername());
            basicAuth.setPassword(config.getPassword());
        } else {
            throw new RuntimeException("Need PlsPlus QG username and password");
        }

        if (StringUtils.hasText(config.getUrl())) {
            defaultClient.setBasePath(config.getUrl());
        }

        defaultClient.setReadTimeout(60 * 1000); //is in ms
        defaultClient.setConnectTimeout(60 * 1000); //is in ms
        //context does not read object (is setup to allow override
        defaultClient.getJSON().getContext(null).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        logger.debug("finished config of PlsPlusQg");
    }

    /**
     * Takes partial address and gives a list of full addresses
     *
     * @param addressLookup
     * @return
     * @throws ApiException
     */
    public List<String> autoCompleteAddressPost(String addressLookup) throws ApiException {
        logger.debug("autoCompleteAddressPost Request: {}", addressLookup);
        AutoCompleteAddressApi plSpQgApi = new AutoCompleteAddressApi();
        PLSPlusAutoCompleteAddressInputMessage a = new PLSPlusAutoCompleteAddressInputMessage();
        a.setAutoCompleteAddress(new AutoCompleteAddress());
        a.getAutoCompleteAddress().setRequest(addressLookup);
        PLSPlusAutoCompleteAddressOutputMessage output = plSpQgApi.autoCompleteAddressViaPost(a);
        List<String> results = output.getAutoCompleteAddressResponse().getAutoCompleteAddressResult().getString();
        logger.debug("autoCompleteAddressPost Result: {}", results);
        return results;
    }

    /**
     * Takes partial address and gives a list of full addresses
     *
     * @param addressLookup
     * @return
     * @throws ApiException
     */
    public List<String> autoCompleteAddressGet(String addressLookup) throws ApiException {
        logger.debug("autoCompleteAddressGet Request: {}", addressLookup);
        AutoCompleteAddressApi plSpQgApi = new AutoCompleteAddressApi();
        //Simple
        PLSPlusAutoCompleteAddressOutputMessage output = plSpQgApi.autoCompleteAddressViaGet(addressLookup);
        List<String> results = output.getAutoCompleteAddressResponse().getAutoCompleteAddressResult().getString();
        logger.debug("autoCompleteAddressGet Result: {}", results);
        return results;
    }

    /**
     * Takes full address (or parital) and gives list of results with confidence rating (can return 0 or 100's)
     *
     * @param fullAddress
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ParseAddressResult parseAddressViaPost(String fullAddress, boolean includeMesh) throws ApiException {
        logger.debug("parseAddressViaPost Request: {}", fullAddress);
        ParseAddressApi plSpQgApi = new ParseAddressApi();
        PLSPlusParseAddressInputMessage input = new PLSPlusParseAddressInputMessage();
        ParseAddress pa = new ParseAddress();
        input.setParseAddress(pa);
        pa.setAddressString(fullAddress);
        pa.setMeshblockOption((includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE));
        PLSPlusParseAddressOutputMessage output = plSpQgApi.parseAddressViaPost(input);
        logger.debug("parseAddressViaPost Result: {}", output);
        return output.getParseAddressResponse().getParseAddressResult();
    }

    /**
     * Takes full address (or parital) and gives list of results with confidence rating (can return 0 or 100's)
     *
     * @param fullAddress
     * @return
     * @throws ApiException
     */
    public ParseAddressResult parseAddressViaGet(String fullAddress) throws ApiException {
        logger.debug("parseAddressViaGet Request: {}", fullAddress);
        ParseAddressApi plSpQgApi = new ParseAddressApi();
        PLSPlusParseAddressOutputMessage output = plSpQgApi.parseAddressViaGet(fullAddress);
        logger.debug("parseAddressViaGet Result: {}", output);
        return output.getParseAddressResponse().getParseAddressResult();
    }

    /**
     * Takes Latitude and Longitude which is inside qld state and gives closes address (could be more than 1)
     *
     * @param longitude
     * @param latitude
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ValidateCoordinatesResponse validateCoordinatesViaPost(BigDecimal latitude, BigDecimal longitude, boolean includeMesh) throws ApiException {
        logger.debug("validateCoordinatesViaPost Request: Lat: {} Long: {}", latitude, longitude);
        ValidateCoordinatesApi plSpQgApi = new ValidateCoordinatesApi();
        PLSPlusValidateCoordinatesInputMessage input = new PLSPlusValidateCoordinatesInputMessage();
        ValidateCoordinates vc = new ValidateCoordinates();
        vc.setLatitude(latitude);
        vc.setLongitude(longitude);
        vc.setMeshblockOption(includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE);
        input.setValidateCoordinates(vc);
        PLSPlusValidateCoordinatesOutputMessage output = plSpQgApi.validateCoordinatesViaPost(input);
        logger.debug("validateCoordinatesViaPost Result: {}", output);
        return output.getValidateCoordinatesResponse();
    }

    /**
     * Takes Latitude and Longitude which is inside qld state and gives closes address (could be more than 1)
     *
     * @param longitude
     * @param latitude
     * @return
     * @throws ApiException
     */
    public ValidateCoordinatesResponse validateCoordinatesViaGet(BigDecimal latitude, BigDecimal longitude) throws ApiException {
        logger.debug("validateCoordinatesViaGet Request: Lat: {} Long: {}", latitude, longitude);
        ValidateCoordinatesApi plSpQgApi = new ValidateCoordinatesApi();
        PLSPlusValidateCoordinatesOutputMessage output = plSpQgApi.validateCoordinatesViaGet(latitude.toString(), longitude.toString());
        logger.debug("validateCoordinatesViaGet Result: {}", output);
        return output.getValidateCoordinatesResponse();
    }

    /**
     * Returns address details of a lot and plan document
     *
     * @param lot
     * @param plan
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ValidateLotPlanResult validateLotPlanViaPost(String lot, String plan, boolean includeMesh) throws ApiException {
        logger.debug("validateLotPlanViaPost Request: lot: {} plan: {}", lot, plan);
        ValidateLotPlanApi plSpQgApi = new ValidateLotPlanApi();
        PLSPlusValidateLotPlanInputMessage input = new PLSPlusValidateLotPlanInputMessage();
        ValidateLotPlan validateLotPlan = new ValidateLotPlan();
        validateLotPlan.setLotNumber(lot);
        validateLotPlan.setPlanNumber(plan);
        validateLotPlan.setMeshblockOption(includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE);
        input.setValidateLotPlan(validateLotPlan);
        PLSPlusValidateLotPlanOutputMessage output = plSpQgApi.validateLotPlanViaPost(input);
        logger.debug("validateLotPlanViaPost Result: {}", output);
        return output.getValidateLotPlanResponse().getValidateLotPlanResult();
    }

    /**
     * Returns address details of a lot and plan document
     *
     * @param lot
     * @param plan
     * @return
     * @throws ApiException
     */
    public ValidateLotPlanResult validateLotPlanViaGet(String lot, String plan) throws ApiException {
        logger.debug("validateLotPlanViaGet Request: lot: {} plan: {}", lot, plan);
        ValidateLotPlanApi plSpQgApi = new ValidateLotPlanApi();
        PLSPlusValidateLotPlanOutputMessage output = plSpQgApi.validateLotPlanViaGet(lot, plan);
        logger.debug("validateLotPlanViaGet Result: {}", output);
        return output.getValidateLotPlanResponse().getValidateLotPlanResult();
    }

    /**
     * validate split address
     *
     * @param addressObject
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ValidateAddressResult validateAddress(ValidateAddressRequest addressObject, boolean includeMesh) throws ApiException {
        logger.debug("validateAddress Request: {}", addressObject);
        ValidateAddressApi plSpQgApi = new ValidateAddressApi();
        PLSPlusValidateAddressInputMessage input = new PLSPlusValidateAddressInputMessage();
        ValidateAddress va = new ValidateAddress();
        input.setValidateAddress(va);
        va.setRequest(addressObject);
        va.getRequest().setMeshblockOption(includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE);
        PLSPlusValidateAddressOutputMessage output = plSpQgApi.validateAddressViaPost(input);
        logger.debug("validateAddress Result: {}", output);
        return output.getValidateAddressResponse().getValidateAddressResult();
    }
}
