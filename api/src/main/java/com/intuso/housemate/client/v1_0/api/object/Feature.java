package com.intuso.housemate.client.v1_0.api.object;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 * @param <FEATURE> the type of the feature
 */
public interface Feature<
        COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
        VALUES extends List<? extends Value<?, ?, ?>, ?>,
        FEATURE extends Feature<COMMANDS, VALUES, FEATURE>>
        extends
        Object<Feature.Listener<? super FEATURE>>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES> {

    String COMMANDS_ID = "commands";
    String VALUES_ID = "values";

    /**
     *
     * Listener interface for features
     */
    interface Listener<FEATURE extends Feature<?, ?, ?>> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of features
     */
    interface Container<FEATURES extends List<? extends Feature<?, ?, ?>, ?>> {

        /**
         * Gets the features list
         * @return the features list
         */
        FEATURES getFeatures();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "feature";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }
}
