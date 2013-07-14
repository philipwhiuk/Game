package com.whiuk.philip.mmorpg.client;

/**
 * Player's representation in the game.
 * @author Philip
 *
 */
public class PlayerCharacter {

    /**
     * 
     * @author Philip
     *
     */
    public static enum Race {
        /**
         * 
         */
        HUMAN("Human"),
        /**
         * 
         */
        DWARF("Dwarf"),
        /**
         * 
         */
        ELF("Elf");
        /**
         * 
         */
        private final String name;
        /**
         * @param n Name
         */
        Race(final String n) {
            name = n;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
    private String name;
    private Race race;
    private String location;
    
    public PlayerCharacter(String name, Race race, String location) {
        this.name = name;
        this.race = race;
        this.location = location;
    }

    @Override
    public final String toString() {
        return name;
    }

    /**
     * @return name
     */
    public final String getName() {
        return name;
    }
}
