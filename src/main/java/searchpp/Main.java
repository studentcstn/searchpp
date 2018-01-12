package searchpp;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Timer;

import searchpp.localservices.PriceHistoryService;
import searchpp.model.config.Api;
import searchpp.model.products.PriceHistory;
import searchpp.utils.ConfigLoader;

/**
 * Main class.
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/myapp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in searchpp package
        final ResourceConfig rc = new ResourceConfig().packages("searchpp");

        rc.register(CORSResponseFilter.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args program arguments
     * @throws IOException if there is a problem with the server
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("Client"), "/");
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        PriceHistoryService phs = new PriceHistoryService();
        Timer t = new Timer();
        //t.scheduleAtFixedRate(phs, 0, 15*1000);
        System.in.read();
        t.cancel();
        server.stop();
    }

}
