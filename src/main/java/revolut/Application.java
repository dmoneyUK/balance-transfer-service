package revolut;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import revolut.config.JerseyConfig;

@Slf4j
public class Application {
    
    public static void main(String[] args) throws Exception {
    
        ResourceConfig config = new JerseyConfig();
    
        ServletHolder jerseyServlet
                = new ServletHolder(new ServletContainer(config));
    
        Server jettyServer = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(jettyServer, "/");
        context.addServlet(jerseyServlet, "/*");
        
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception ex) {
            log.error("Error occurred while starting Jetty", ex);
        }finally {
            jettyServer.destroy();
        }
    }
}
