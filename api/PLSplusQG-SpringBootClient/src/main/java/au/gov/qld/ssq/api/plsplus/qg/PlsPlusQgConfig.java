package au.gov.qld.ssq.api.plsplus.qg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlsPlusQgConfig {

    @Value(value = "${integration-points.plsplusqg.url:#{null}}")
    private String url;
    @Value(value = "${integration-points.plsplusqg.username:#{null}}")
    private String username;
    @Value(value = "${integration-points.plsplusqg.password:#{null}}")
    private String password;
    @Value(value = "${integration-points.plsplusqg.apiKey:#{null}}")
    private String apiKey;
    @Value(value = "${integration-points.plsplusqg.origin:#{null}}")
    private String origin;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
