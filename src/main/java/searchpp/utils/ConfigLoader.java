package searchpp.utils;

import searchpp.model.config.Api;
import searchpp.model.config.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Config loader loads configs from searchpp.conf
 */
public class ConfigLoader {

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
     * Load config from file
     */
    private void loadConfig() {
        if (!file.exists())
            return;

        System.out.println("Config loaction: " + file.getAbsolutePath());

        configs = new ArrayList<>();

        String name = null;
        String clientID = null;
        String accessKey = null;
        String secretKey = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            for (String input = in.readLine(); input != null; input = in.readLine()) {
                if (input.indexOf('=') == -1)
                    continue;

                String[] name_value = input.split("=");
                if (name_value.length < 2)
                    continue;

                String[] site_name = name_value[0].split("_");
                if (name == null)
                    name = site_name[0];

                if (!site_name[0].equals(name)) {
                    name = site_name[0];
                    clientID = null;
                    accessKey = null;
                    secretKey = null;
                }

                switch (site_name[1]) {
                    case "clientID":
                        clientID = name_value[1];
                        break;
                    case "accessKey":
                        accessKey = name_value[1];
                        break;
                    case "secretKey":
                        secretKey = name_value[1];
                }


                if (clientID != null && secretKey != null) {
                    System.out.println("Read config for " + name);

                    configs.add(new Config(name, clientID, accessKey, secretKey));

                    name = null;
                    clientID = null;
                    accessKey = null;
                    secretKey = null;
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
