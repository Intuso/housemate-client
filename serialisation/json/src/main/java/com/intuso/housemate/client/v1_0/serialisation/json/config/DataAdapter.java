package com.intuso.housemate.client.v1_0.serialisation.json.config;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.client.v1_0.api.object.*;
import com.intuso.housemate.client.v1_0.api.object.Object;

/**
 * Created by tomc on 04/06/14.
 */
public class DataAdapter extends RuntimeTypeAdapterFactory<Object.Data> {

    public DataAdapter() {
        super(Object.Data.class, SerializableAdapter.TYPE_FIELD_NAME, SerializableAdapter.ENUM_VALUE_FIELD_NAME);
        registerSubtype(Automation.Data.class, "automation");
        registerSubtype(Command.Data.class, "command");
        registerSubtype(Condition.Data.class, "condition");
        registerSubtype(Device.Data.class, "device");
        registerSubtype(Device.Connected.Data.class, "deviceConnected");
        registerSubtype(Device.Group.Data.class, "deviceGroup");
        registerSubtype(DeviceComponent.Data.class, "deviceComponent");
        registerSubtype(Hardware.Data.class, "hardware");
        registerSubtype(List.Data.class, "list");
        registerSubtype(Node.Data.class, "node");
        registerSubtype(Option.Data.class, "option");
        registerSubtype(Parameter.Data.class, "parameter");
        registerSubtype(Property.Data.class, "property");
        registerSubtype(Reference.Data.class, "reference");
        registerSubtype(Server.Data.class, "server");
        registerSubtype(SubType.Data.class, "subType");
        registerSubtype(Task.Data.class, "task");
        registerSubtype(Type.ChoiceData.class, "choiceType");
        registerSubtype(Type.CompositeData.class, "compoundType");
        registerSubtype(Type.ObjectData.class, "objectType");
        registerSubtype(Type.RegexData.class, "regexType");
        registerSubtype(Type.PrimitiveData.class, "primitiveType");
        registerSubtype(User.Data.class, "user");
        registerSubtype(Value.Data.class, "value");
    }
}
