package com.intuso.housemate.client.v1_0.serialisation.json.config;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.client.v1_0.api.object.view.*;

/**
 * Created by tomc on 04/06/14.
 */
public class ViewAdapter extends RuntimeTypeAdapterFactory<View> {

    public ViewAdapter() {
        super(View.class, SerializableAdapter.TYPE_FIELD_NAME, SerializableAdapter.ENUM_VALUE_FIELD_NAME);
        registerSubtype(AutomationView.class, "automation");
        registerSubtype(CommandView.class, "command");
        registerSubtype(ConditionView.class, "condition");
        registerSubtype(DeviceConnectedView.class, "deviceConnected");
        registerSubtype(DeviceGroupView.class, "deviceGroup");
        registerSubtype(HardwareView.class, "hardware");
        registerSubtype(ListView.class, "list");
        registerSubtype(NodeView.class, "node");
        registerSubtype(NoView.class, "none");
        registerSubtype(ParameterView.class, "parameter");
        registerSubtype(PropertyView.class, "property");
        registerSubtype(ServerView.class, "server");
        registerSubtype(TaskView.class, "task");
        registerSubtype(TypeView.class, "type");
        registerSubtype(UserView.class, "user");
        registerSubtype(ValueView.class, "value");
    }
}
