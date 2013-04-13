package com.whiuk.philip.game.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.whiuk.philip.game.server.GameServer.GameServerProperties;

/**
 * @author Philip
 */
public final class Main {

    /**
     * Utility class - private constructor.
     */
    private Main() {
    }

    /**
	 *
	 */
    private static Logger logger = Logger.getLogger(Main.class);
    /**
     *
     */
    private static final String DEFAULT_PROPERTIES_FILENAME = "/etc/opt/philipwhiuk/gameServer.properties";
    /**
     *
     */
    private static String propertiesFilename = DEFAULT_PROPERTIES_FILENAME;

    /**
     * @param args
     * @throws IOException
     *             IO exception reading property file
     */
    public static void main(final String[] args) throws IOException {
        // TODO: Initialize logging properly
        PropertyConfigurator.configure("log4j.properties");
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        Option help = new Option("help", "print this message");
        Option properties = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription(
                        "use given file to retrieve additional properties")
                .create("properties");
        options.addOption(help);
        options.addOption(properties);
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("help")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("game-server", options);
                System.exit(0);
            }
            if (line.hasOption("properties")) {
                propertiesFilename = line.getOptionValue("properties");
            }
        } catch (ParseException e) {
            // oops, something went wrong
            logger.log(Level.WARN, "Unable to read command line arguments", e);
        }

        ApplicationContext context = new ClassPathXmlApplicationContext(
                "META-INF/beans.xml");
        BeanFactory factory = context;
        GameServer gameServer = (GameServer) factory.getBean("gameServer");
        GameServerProperties gsProp;
        File file = new File(propertiesFilename);
        try {
            Properties prop = new Properties();
            prop.load(new FileReader(file));
            gsProp = new GameServerProperties(prop);
        } catch (Exception e) {
            System.out.println(file.getAbsolutePath());
            logger.log(Level.WARN, "Error reading properties file: "
                    + propertiesFilename, e);
            gsProp = new GameServerProperties();
        }
        gameServer.setProperties(gsProp);
    }

}
