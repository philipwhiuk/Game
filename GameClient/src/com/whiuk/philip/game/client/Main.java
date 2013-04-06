package com.whiuk.philip.game.client;

import org.apache.log4j.BasicConfigurator;

/**
 * @author Philip
 *
 */
public class Main {

    /**
     * @param args Command line arguments
     */
    public static void main(String[] args) {
    	//TODO: Initialize logging properly
    	BasicConfigurator.configure();
    	GameClient client = new GameClient();
    	GameClient.setGameClient(client);
    	client.run();
    }

}
