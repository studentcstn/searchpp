package searchpp.utils;

import org.junit.Test;
import searchpp.model.config.Api;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ConfigLoader_test {

    @Test
    public void getInstance() {
        assertNotNull(ConfigLoader.getInstance());
    }

    @Test
    public void getConfig() {
        ConfigLoader configLoader = ConfigLoader.getInstance();

        //change file and load new
        try {
            Class c = configLoader.getClass();
            Field field = c.getDeclaredField("file");
            field.setAccessible(true);
            field.set(configLoader, new File("src/test/java/searchpp/utils/searchpp_test.conf"));

            Method method = c.getDeclaredMethod("loadConfig");
            method.setAccessible(true);
            method.invoke(configLoader);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        System.out.println(configLoader.toString());

        assertNotNull(ConfigLoader.getInstance().getConfig("amazon"));

        assertNotNull(ConfigLoader.getConfig("amazon", Api.clientID));
        assertTrue(ConfigLoader.getConfig("amazon", Api.clientID).equals("123"));

        assertNotNull(ConfigLoader.getConfig("amazon", Api.accessKey));
        assertTrue(ConfigLoader.getConfig("amazon", Api.accessKey).equals("1234"));

        assertNotNull(ConfigLoader.getConfig("amazon", Api.secretKey));
        assertTrue(ConfigLoader.getConfig("amazon", Api.secretKey).equals("12345"));



        assertNotNull(ConfigLoader.getInstance().getConfig("ebay"));

        assertNotNull(ConfigLoader.getConfig("ebay", Api.clientID));
        assertTrue(ConfigLoader.getConfig("ebay", Api.clientID).equals("678"));

        assertNotNull(ConfigLoader.getConfig("ebay", Api.accessKey));
        assertTrue(ConfigLoader.getConfig("ebay", Api.accessKey).equals("6789"));

        assertNotNull(ConfigLoader.getConfig("ebay", Api.secretKey));
        assertTrue(ConfigLoader.getConfig("ebay", Api.secretKey).equals("67890"));

    }
}
