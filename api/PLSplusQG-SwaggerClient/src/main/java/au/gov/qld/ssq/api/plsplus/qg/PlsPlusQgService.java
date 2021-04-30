package au.gov.qld.ssq.api.plsplus.qg;

import au.gov.qld.ssq.api.plsplus.qg.handler.ApiClient;
import au.gov.qld.ssq.api.plsplus.qg.handler.ApiException;
import au.gov.qld.ssq.api.plsplus.qg.handler.Configuration;
import au.gov.qld.ssq.api.plsplus.qg.handler.auth.HttpBasicAuth;
import au.gov.qld.ssq.api.plsplus.qg.model.AutoCompleteAddress;
import au.gov.qld.ssq.api.plsplus.qg.model.PLSPlusAutoCompleteAddressInputMessage;
import au.gov.qld.ssq.api.plsplus.qg.model.PLSPlusAutoCompleteAddressOutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PlsPlusQgService {
    private final PlsPlusQgConfig config;

    @Autowired
    public PlsPlusQgService(PlsPlusQgConfig config) {
        this.config = config;
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure HTTP basic authorization: BasicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
        if(config.getPassword() == null || config.getUsername() == null) {
            throw new RuntimeException("Need PlsPlus QG username and password");
        }
        basicAuth.setUsername(config.getUsername());
        basicAuth.setPassword(config.getPassword());
        if (StringUtils.hasText(config.getUrl())) {
            defaultClient.setBasePath(config.getUrl());
        }
        defaultClient.setReadTimeout(60 * 1000); //is in ms
        defaultClient.setConnectTimeout(60 * 1000); //is in ms
    }

    public List<String> autoCompleteAddress(String addressLookup) throws ApiException {
        PlSpQgApi plSpQgApi = new PlSpQgApi();
        PLSPlusAutoCompleteAddressInputMessage a = new PLSPlusAutoCompleteAddressInputMessage();
        a.setAutoCompleteAddress(new AutoCompleteAddress());
        a.getAutoCompleteAddress().setRequest(addressLookup);
        PLSPlusAutoCompleteAddressOutputMessage output = plSpQgApi.autoCompleteAddress(a);
        List<String> results = output.getAutoCompleteAddressResponse().getAutoCompleteAddressResult().getString();
        return results;
    }
}
