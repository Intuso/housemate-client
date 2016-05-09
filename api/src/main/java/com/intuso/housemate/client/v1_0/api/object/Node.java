package com.intuso.housemate.client.v1_0.api.object;

public interface Node<
        COMMAND extends Command<?, ?, ?, ?>,
        HARDWARES extends List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        NODE extends Node>
        extends Object<Node.Listener<? super NODE>>,
        Hardware.Container<HARDWARES> {

    String HARDWARES_ID = "hardwares";
    String ADD_HARDWARE_ID = "add-hardware";

    COMMAND getAddHardwareCommand();

    /**
     *
     * Listener interface for server
     */
    interface Listener<NODE extends Node> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of server
     */
    interface Container<NODES extends List<? extends Node<?, ?, ?>, ?>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        NODES getNodes();
    }

    /**
     * Data object for a command
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String TYPE = "node";

        public Data() {}

        public Data(String id, String name, String description) {
            super(TYPE, id, name, description);
        }
    }
}
