package com.intuso.housemate.client.v1_0.api.object;

import com.intuso.housemate.client.v1_0.api.Failable;
import com.intuso.housemate.client.v1_0.api.Removeable;
import com.intuso.housemate.client.v1_0.api.Renameable;
import com.intuso.housemate.client.v1_0.api.object.view.DeviceConnectedView;
import com.intuso.housemate.client.v1_0.api.object.view.DeviceGroupView;
import com.intuso.housemate.client.v1_0.api.object.view.DeviceView;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 * @param <DEVICE> the type of the feature
 */
public interface Device<
        DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        RENAME_COMMAND extends Command<?, ?, ?, ?>,
        COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
        VALUES extends List<? extends Value<?, ?, ?>, ?>,
        VIEW extends DeviceView<?>,
        DEVICE extends Device<DATA, LISTENER, RENAME_COMMAND, COMMANDS, VALUES, VIEW, DEVICE>>
        extends
        Object<DATA, LISTENER, VIEW>,
        Renameable<RENAME_COMMAND>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES> {

    String COMMANDS_ID = "command";
    String VALUES_ID = "value";

    /**
     *
     * Listener interface for features
     */
    interface Listener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?>> extends Object.Listener,
            Renameable.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of features
     */
    interface Container<DEVICES extends Iterable<? extends Device<?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the features list
         * @return the features list
         */
        DEVICES getDevices();
    }

    /**
     * Data object for a device
     */
    class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public Data() {}

        public Data(String objectClass, String id, String name, String description) {
            super(objectClass, id, name, description);
        }
    }

    /**
     * @param <DEVICE> the type of the device
     */
    interface Connected<
            RENAME_COMMAND extends Command<?, ?, ?, ?>,
            COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
            VALUES extends List<? extends Value<?, ?, ?>, ?>,
            DEVICE extends Connected<RENAME_COMMAND, COMMANDS, VALUES, DEVICE>>
            extends
            Device<Connected.Data, Connected.Listener<? super DEVICE>, RENAME_COMMAND, COMMANDS, VALUES, DeviceConnectedView, DEVICE>  {

        /**
         *
         * Listener interface for devices
         */
        interface Listener<DEVICE extends Connected<?, ?, ?, ?>> extends Device.Listener<DEVICE> {}

        /**
         *
         * Interface to show that the implementing object has a list of features
         */
        interface Container<DEVICES extends Iterable<? extends Device<?, ?, ?, ?, ?, ?, ?>>> {

            /**
             * Gets the features list
             * @return the features list
             */
            DEVICES getDeviceConnecteds();
        }

        /**
         * Data object for a device
         */
        final class Data extends Device.Data {

            private static final long serialVersionUID = -1L;

            public final static String OBJECT_CLASS = "device-connected";

            public Data() {}

            public Data(String id, String name, String description) {
                super(OBJECT_CLASS, id, name, description);
            }
        }
    }

    /**
     * @param <ERROR_VALUE> the type of the error value
     * @param <DEVICE> the type of the device
     */
    interface Group<
            RENAME_COMMAND extends Command<?, ?, ?, ?>,
            REMOVE_COMMAND extends Command<?, ?, ?, ?>,
            ADD_COMMAND extends Command<?, ?, ?, ?>,
            ERROR_VALUE extends Value<?, ?, ?>,
            COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
            VALUES extends List<? extends Value<?, ?, ?>, ?>,
            DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?>, ?>,
            DEVICE extends Group<RENAME_COMMAND, REMOVE_COMMAND, ADD_COMMAND, ERROR_VALUE, COMMANDS, VALUES, DEVICES, DEVICE>>
            extends
            Device<Group.Data, Group.Listener<? super DEVICE>, RENAME_COMMAND, COMMANDS, VALUES, DeviceGroupView, DEVICE>,
            Failable<ERROR_VALUE>,
            Removeable<REMOVE_COMMAND> {

        String PLAYBACK = "playback";
        String ADD_PLAYBACK = "add-playback";
        String POWER = "power";
        String ADD_POWER = "add-power";
        String RUN = "run";
        String ADD_RUN = "add-run";
        String TEMPERATURE_SENSOR = "temperature-sensor";
        String ADD_TEMPERATURE_SENSOR = "add-temperature-sensor";
        String VOLUME = "volume";
        String ADD_VOLUME = "add-volume";

        DEVICES getPlaybackDevices();
        ADD_COMMAND getAddPlaybackDeviceCommand();
        DEVICES getPowerDevices();
        ADD_COMMAND getAddPowerDeviceCommand();
        DEVICES getRunDevices();
        ADD_COMMAND getAddTemperatureSensorDeviceCommand();
        DEVICES getTemperatureSensorDevices();
        ADD_COMMAND getAddRunDeviceCommand();
        DEVICES getVolumeDevices();
        ADD_COMMAND getAddVolumeDeviceCommand();

        /**
         *
         * Listener interface for devices
         */
        interface Listener<DEVICE extends Group<?, ?, ?, ?, ?, ?, ?, ?>> extends Device.Listener<DEVICE>,
                Failable.Listener<DEVICE>,
                Renameable.Listener<DEVICE> {}

        /**
         *
         * Interface to show that the implementing object has a list of features
         */
        interface Container<DEVICES extends Iterable<? extends Device<?, ?, ?, ?, ?, ?, ?>>> {

            /**
             * Gets the features list
             * @return the features list
             */
            DEVICES getDeviceGroups();
        }

        /**
         * Data object for a device
         */
        final class Data extends Device.Data {

            private static final long serialVersionUID = -1L;

            public final static String OBJECT_CLASS = "device-group";

            public Data() {}

            public Data(String id, String name, String description) {
                super(OBJECT_CLASS, id, name, description);
            }
        }
    }
}
