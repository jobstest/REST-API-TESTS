package config.demowebshop;

import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/credential.properties"
})
public interface WebConfig extends Config{

    @Key("userLogin")
    String login();

    @Key("userPassword")
    String password();

    @Key("baseUrl")
    String baseUrl();

    @Key("browser")
    @DefaultValue("CHROME")
    String browser();

    @Key("browser.version")
    @DefaultValue("105")
    String browserVersion();

    @Key("browser.size")
    @DefaultValue("1920x1080")
    String browserSize();


}
