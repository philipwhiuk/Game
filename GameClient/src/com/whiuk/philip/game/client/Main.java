package com.whiuk.philip.game.client;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Philip
 */
public class Main {

    /**
     * @param args
     *            Command line arguments
     */
    public static void main(final String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        GameClient client = new GameClient();
        GameClient.setGameClient(client);
        client.run();
    }

}
