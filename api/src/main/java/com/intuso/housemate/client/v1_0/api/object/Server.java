package com.intuso.housemate.client.v1_0.api.object;

public interface Server<
        COMMAND extends Command<?, ?, ?, ?>,
        DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?>, ?>,
        AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        DEVICE_GROUPS extends List<? extends Device.Group<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends List<? extends User<?, ?, ?, ?>, ?>,
        NODES extends List<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends Server<COMMAND, DEVICES, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER>>
        extends Object<Server.Data, Server.Listener<? super SERVER>>,
        Device.Container<DEVICES>,
        Device.Group.Container<DEVICE_GROUPS>,
        Automation.Container<AUTOMATIONS>,
        User.Container<USERS>,
        Node.Container<NODES> {

    String DEVICES_ID = "device";
    String AUTOMATIONS_ID = "automation";
    String DEVICE_GROUPS_ID = "device-group";
    String USERS_ID = "user";
    String NODES_ID = "node";
    String ADD_AUTOMATION_ID = "add-automation";
    String ADD_DEVICE_GROUP_ID = "add-device-group";
    String ADD_USER_ID = "add-user";

    COMMAND getAddAutomationCommand();
    COMMAND getAddDeviceGroupCommand();
    COMMAND getAddUserCommand();

    /**
     *
     * Listener interface for server
     */
    interface Listener<SERVER extends Server> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of server
     */
    interface Container<SERVERS extends Iterable<? extends Server<?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        SERVERS getServers();
    }

    /**
     * Data object for a command
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "server";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
