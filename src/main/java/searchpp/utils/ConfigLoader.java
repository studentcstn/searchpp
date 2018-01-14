package searchpp.utils;

import searchpp.model.config.Api;
import searchpp.model.config.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Config loader loads configs from searchpp.conf
 */
public final class ConfigLoader {

    private ArrayList<Config> configs;

    private static ConfigLoader self;

    /**
     * Returns instance of ConfigLoader
     * @return ConfigLoader
     */
    public static ConfigLoader getInstance() {
        if (self == null)
            self = new ConfigLoader();
        return self;
    }

    /**
     * @param site Name of site from config
     * @param key Key of config
     * @return Key as String or null if nothing was found
     */
    public static String getConfig(String site, Api key) {
        if (self == null)
            self = new ConfigLoader();

        Config config = self.getConfig(site);
        if (config == null)
            return null;
        switch (key) {
            case clientID:
                return config.getClientID();
            case accessKey:
                return config.getAccessKey();
            case secretKey:
                return config.getSecretKey();
        }
        return null;
    }

    /**
     * Returns config of given site
     * @param site Name of site from config
     * @return Config of given site or null if nothing was found
     */
    public Config getConfig(String site) {
        if (configs == null)
            return null;
        for (Config config : configs)
            if (config.getName().equals(site))
                return config;
        return null;
    }

    /**
     * Builds new ConfigLoader object and load config
     */
    private ConfigLoader() {
        loadConfig();
    }

    private File file = new File("searchpp.conf");

    /**
     * Write configs in {@link Config} objects
     * @param name name of the site
     * @param api api key
     * @param value key value
     */
    private void writeConfig(String name, Api api, String value) {
        for (int i = configs.size() - 1; i >= 0; --i) {
            Config config = configs.get(i);
            if (config.getName().equals(name)) {
                switch (api) {
                    case clientID:
                        config.setClientID(value);
                        break;
                    case secretKey:
                        config.setSecretKey(value);
                        break;
                    case accessKey:
                        config.setAccessKey(value);
                        break;
                }
                return;
            }
        }
        configs.add(new Config(name));
        writeConfig(name, api, value);
    }

    /**
     * Load config from file
     */
    private void loadConfig() {
        if (!file.exists())
            return;

        System.out.println("Config loaction: " + file.getAbsolutePath());

        configs = new ArrayList<>();

        String name;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            for (String input = in.readLine(); input != null; input = in.readLine()) {
                if (input.indexOf('=') == -1)
                    continue;

                //look for comment
                int id = input.indexOf('#');
                if (id >= 0)
                    input = input.substring(0, id);

                //clear string
                if (input.indexOf(' ') >= 0) {
                    StringBuilder stringBuilder = new StringBuilder(input);
                    while ((id = stringBuilder.indexOf(" ")) >= 0)
                        stringBuilder.delete(id, id + 1);
                    input = stringBuilder.toString();
                }

                //split string into name$key and value
                String[] name$key_value = input.split("=");
                if (name$key_value.length < 2)
                    continue;

                //split name$key into name and key
                String[] name_key = name$key_value[0].split("_");
                name = name_key[0];

                switch (name_key[1]) {
                    case "clientID":
                        writeConfig(name, Api.clientID, name$key_value[1]);
                        break;
                    case "accessKey":
                        writeConfig(name, Api.accessKey, name$key_value[1]);
                        break;
                    case "secretKey":
                        writeConfig(name, Api.secretKey, name$key_value[1]);
                        break;
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (Config config : configs)
                config.setFinalConfig();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Config config : configs)
            stringBuilder.append(config.toString()).append('\n');
        return stringBuilder.toString();
    }
}
