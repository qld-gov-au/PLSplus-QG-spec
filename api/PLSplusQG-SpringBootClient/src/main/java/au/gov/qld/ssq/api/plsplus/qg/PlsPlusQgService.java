package au.gov.qld.ssq.api.plsplus.qg;

import au.gov.qld.ssq.api.plsplus.qg.handler.ApiClient;
import au.gov.qld.ssq.api.plsplus.qg.handler.ApiException;
import au.gov.qld.ssq.api.plsplus.qg.handler.Configuration;
import au.gov.qld.ssq.api.plsplus.qg.handler.auth.HttpBasicAuth;
import au.gov.qld.ssq.api.plsplus.qg.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlsPlusQgService {

    @Autowired
    public PlsPlusQgService(PlsPlusQgConfig config) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure HTTP basic authorization: BasicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
        if (config.getPassword() == null || config.getUsername() == null) {
            throw new RuntimeException("Need PlsPlus QG username and password");
        }
        basicAuth.setUsername(config.getUsername());
        basicAuth.setPassword(config.getPassword());
        if (StringUtils.hasText(config.getUrl())) {
            defaultClient.setBasePath(config.getUrl());
        }
        defaultClient.setReadTimeout(60 * 1000); //is in ms
        defaultClient.setConnectTimeout(60 * 1000); //is in ms
        //context does not read object (is setup to allow override
        defaultClient.getJSON().getContext(null).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    /**
     * Takes partial address and gives a list of full addresses
     * @param addressLookup
     * @return
     * @throws ApiException
     */
    public List<String> autoCompleteAddress(String addressLookup) throws ApiException {
        PlSpQgApi plSpQgApi = new PlSpQgApi();
        PLSPlusAutoCompleteAddressInputMessage a = new PLSPlusAutoCompleteAddressInputMessage();
        a.setAutoCompleteAddress(new AutoCompleteAddress());
        a.getAutoCompleteAddress().setRequest(addressLookup);
        PLSPlusAutoCompleteAddressOutputMessage output = plSpQgApi.autoCompleteAddress(a);
        List<String> results = output.getAutoCompleteAddressResponse().getAutoCompleteAddressResult().getString();
        return results;
    }

    /**
     * Takes full address (or parital) and gives list of results with confidence rating (can return 0 or 100's)
     * @param fullAddress
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ParseAddressResult parseAddress(String fullAddress, boolean includeMesh) throws ApiException {
        PlSpQgApi plSpQgApi = new PlSpQgApi();
        PLSPlusParseAddressInputMessage input = new PLSPlusParseAddressInputMessage();
        ParseAddress pa = new ParseAddress();
        input.setParseAddress(pa);
        pa.setAddressString(fullAddress);
        pa.setMeshblockOption((includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE));
        PLSPlusParseAddressOutputMessage output = plSpQgApi.parseAddress(input);
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
    public ValidateCoordinatesResult validateCoordinates(BigDecimal latitude, BigDecimal longitude, boolean includeMesh) throws ApiException {
        PlSpQgApi plSpQgApi = new PlSpQgApi();
        PLSPlusValidateCoordinatesInputMessage input = new PLSPlusValidateCoordinatesInputMessage();
        ValidateCoordinates vc = new ValidateCoordinates();
        vc.setLatitude(latitude);
        vc.setLongitude(longitude);
        vc.setMeshblockOption(includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE);
        input.setValidateCoordinates(vc);
        PLSPlusValidateCoordinatesOutputMessage output = plSpQgApi.validateCoordinates(input);
        return output.getValidateCoordinatesResponse().getValidateCoordinatesResult();
    }

    /**
     * Returns address details of a lot and plan document
     * @param lot
     * @param plan
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ValidateLotPlanResult validateLotPlan(String lot, String plan, boolean includeMesh) throws ApiException {
        PlSpQgApi plSpQgApi = new PlSpQgApi();
        PLSPlusValidateLotPlanInputMessage input = new PLSPlusValidateLotPlanInputMessage();
        ValidateLotPlan validateLotPlan = new ValidateLotPlan();
        validateLotPlan.setLotNumber(lot);
        validateLotPlan.setPlanNumber(plan);
        validateLotPlan.setMeshblockOption(includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE);
        input.setValidateLotPlan(validateLotPlan);
        PLSPlusValidateLotPlanOutputMessage output = plSpQgApi.validateLotPlan(input);
        return output.getValidateLotPlanResponse().getValidateLotPlanResult();
    }

    /**
     * validate split address
     * @param addressObject
     * @param includeMesh
     * @return
     * @throws ApiException
     */
    public ValidateAddressResult validateAddress(ValidateAddressRequest addressObject, boolean includeMesh) throws ApiException {
        PlSpQgApi plSpQgApi = new PlSpQgApi();
        PLSPlusValidateAddressInputMessage input = new PLSPlusValidateAddressInputMessage();
        ValidateAddress va = new ValidateAddress();
        input.setValidateAddress(va);
        va.setRequest(addressObject);
        va.getRequest().setMeshblockOption(includeMesh ? MeshblockOption.INCLUDE : MeshblockOption.EXCLUDE);
        PLSPlusValidateAddressOutputMessage output = plSpQgApi.validateAddress(input);
        return output.getValidateAddressResponse().getValidateAddressResult();
    }
}
