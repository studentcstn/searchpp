package searchpp.model.config;

public class Config {

    private String name;
    private String clientID;
    private String accessKey;
    private String secretKey;

    public Config(String name, String clientID, String accessKey, String secretKey) {
        this.name = name;
        this.clientID = clientID;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getName() {
        return name;
    }

    public String getClientID() {
        return clientID;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
