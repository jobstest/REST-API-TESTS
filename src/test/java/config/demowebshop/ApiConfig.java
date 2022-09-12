package config.demowebshop;

import org.aeonbits.owner.Config;

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
