package com.whiuk.philip.mmorpg.client;

/**
 * Player's representation in the game.
 * @author Philip
 *
 */
public class LobbyCharacterData {

    /**
     * Race.
     * @author Philip
     *
     */
    public static enum Race {
        /**
         * Human.
         */
        HUMAN("Human"),
        /**
         * Dwarf.
         */
        DWARF("Dwarf"),
        /**
         * Elf.
         */
        ELF("Elf");
        /**
         * Name.
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
    /**
     * ID.
     */
    private int id;
    /**
     * Name.
     */
    private String name;
    /**
     * Race.
     */
    private Race race;
    /**
     * Location.
     */
    private String location;
    /**
     * Constructor.
     * @param i ID
     * @param n Name
     * @param r Race
     * @param l Location
     */
    public LobbyCharacterData(final int i, final String n,
            final Race r, final String l) {
        this.id = i;
        this.name = n;
        this.setRace(r);
        this.setLocation(l);
    }
    /**
     * @return id
     */
    public final int getId() {
        return id;
    }

    /**
     * @return name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the race
     */
    final Race getRace() {
        return race;
    }

    /**
     * @param r the race to set
     */
    final void setRace(final Race r) {
        this.race = r;
    }
    /**
     * @return the location
     */
    final String getLocation() {
        return location;
    }
    /**
     * @param l the location to set
     */
    final void setLocation(final String l) {
        this.location = l;
    }
    @Override
    public final String toString() {
        return name;
    }
}
