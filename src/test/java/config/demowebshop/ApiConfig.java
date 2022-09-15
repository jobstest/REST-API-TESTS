package config.demowebshop;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/api_credential.properties"})

public interface ApiConfig extends Config {

    @Key("baseURI")
    String baseURI();

    @Key("remoteUser")
    String remoteUser();

    @Key("remotePassword")
    String remotePassword();

    @Key("server")
    String server();
}
