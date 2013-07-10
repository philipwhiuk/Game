package com.whiuk.philip.mmorpg.server.game.domain;


/**
 * A distinct collection of buildings.
 * @author Philip
 *
 */
public class Settlement {
    /**
     * The classification of settlement based on it's size.
     * @author Philip
     *
     */
    public enum Classification {
        /**
         * A single house or very small collection of buildings.
         */
        HAMLET(1),
        /**
         * A collection of houses and basic ammenities.
         */
        VILLAGE(3),
        /**
         * A medium collection of houses, ammenities.
         */
        TOWN(50),
        /**
         * A large collection houses, ammenities, resources
         * and other structures.
         */
        CITY(300),
        /**
         * An immense collection of buildings of varying types,
         * sizes, with many houses and ammenities.
         */
        MEGACITY(10000);
        /**
         * Minimum size.
         */
        private final int minimumSize;

        /**
         * Defines a classification by it's minimum size.
         * @param size The minimum size to achieve this classification.
         */
        Classification(final int size) {
            this.minimumSize = size;
        }
    }
    /**
     * Classification.
     */
    private Classification classification;
    /**
     * Inhabitants.
     */
    private Tribe tribe;
}
