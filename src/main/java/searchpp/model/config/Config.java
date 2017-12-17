package searchpp.model.config;

public final class Config {

    private String name;
    private String clientID;
    private String accessKey;
    private String secretKey;

    private boolean finalConfig = false;

    public Config(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getClientID() {
        return clientID;
    }
    public void setClientID(String clientID) {
        if (!finalConfig)
            this.clientID = clientID;
    }

    public String getAccessKey() {
        return accessKey;
    }
    public void setAccessKey(String accessKey) {
        if (!finalConfig)
            this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        if (!finalConfig)
            this.secretKey = secretKey;
    }

    public void setFinalConfig() {
        finalConfig = true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%s api keys:", name));
        if (clientID != null) {
            stringBuilder.append("\n  clientID: ");
            for (int i = 0; i < clientID.length(); ++i)
                stringBuilder.append('x');
        }
        if (accessKey != null) {
            stringBuilder.append("\n  accesskey: ");
            for (int i = 0; i < accessKey.length(); ++i)
                stringBuilder.append('x');
        }
        if (secretKey != null) {
            stringBuilder.append("\n  secretKey: ");
            for (int i = 0; i < secretKey.length(); ++i)
                stringBuilder.append('x');
        }
        return stringBuilder.toString();
    }
}
