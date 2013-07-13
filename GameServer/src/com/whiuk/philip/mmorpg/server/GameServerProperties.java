package com.whiuk.philip.mmorpg.server;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Provides a way to load properties.
 * @author Philip
 */
@Component
public class GameServerProperties extends Properties {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Default listening port for server.
     */
    private static final String DEFALT_PORT = "8443";
    /**
    *
    */
   private static final String DEFAULT_PROPERTIES_FILENAME =
           "/etc/opt/philipwhiuk/gameServer.properties";
   /**
    *
    */
   private static String propertiesFilename = DEFAULT_PROPERTIES_FILENAME;
   /**
    * 
    */
   private Logger logger = Logger.getLogger(GameServerProperties.class);
    /**
     *
     */
    public GameServerProperties() {
        super();
        setProperty("port", DEFALT_PORT);
        File file = new File(propertiesFilename);
        try {
            load(new FileReader(file));
        } catch (Exception e) {
            System.out.println(file.getAbsolutePath());
            logger.log(Level.WARN, "Error reading properties file: "
                    + propertiesFilename, e);
        }
    }
}