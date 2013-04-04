package com.whiuk.philip.game.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.whiuk.philip.game.server.GameServer.GameServerProperties;

/**
 * @author Philip
 *
 */
public class Main {

    private static Logger logger;
    private static String propertiesFilename = "/etc/opt/philipwhiuk/gameServer.properties";
    private static File propertiesFile = new File(propertiesFilename);

    /**
     * @param args
     * @throws IOException IO exception reading property file
     * @throws FileNotFoundException Property file not found
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        GameServerProperties gsProp;
        try {
            Properties prop = new Properties();
            prop.load(new FileReader(propertiesFile));
            gsProp = new GameServerProperties(prop);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error reading properties file: " + propertiesFilename, e);
            gsProp = new GameServerProperties();
        }
        GameServer server = new GameServer(gsProp);


    }

}
