package config.demowebshop;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/credential.properties"})
public interface WebConfig extends Config {

    @Key("userLogin")
    String login();

    @Key("userPassword")
    String password();

    @Key("baseUrl")
    @DefaultValue("https://demowebshop.tricentis.com/")
    String basebUrl();
}
