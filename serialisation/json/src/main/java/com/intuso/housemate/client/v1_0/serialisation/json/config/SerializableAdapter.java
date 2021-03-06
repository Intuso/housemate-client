package com.intuso.housemate.client.v1_0.serialisation.json.config;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.intuso.housemate.client.v1_0.api.object.*;

import java.io.Serializable;

/**
 * Created by tomc on 04/06/14.
 */
public class SerializableAdapter extends RuntimeTypeAdapterFactory<Serializable> {

    public final static String TYPE_FIELD_NAME = "_type";
    public static final String ENUM_VALUE_FIELD_NAME = "value";

    public SerializableAdapter() {
        super(Serializable.class, TYPE_FIELD_NAME, ENUM_VALUE_FIELD_NAME);
        registerSubtype(Automation.Data.class, "automation");
        registerSubtype(Command.Data.class, "command");
        registerSubtype(Command.PerformData.class, "commandPerform");
        registerSubtype(Command.PerformStatusData.class, "commandPerformStatus");
        registerSubtype(Condition.Data.class, "condition");
        registerSubtype(Device.Data.class, "device");
        registerSubtype(Device.Connected.Data.class, "deviceConnected");
        registerSubtype(Device.Group.Data.class, "deviceGroup");
        registerSubtype(DeviceComponent.Data.class, "deviceComponent");
        registerSubtype(Hardware.Data.class, "hardware");
        registerSubtype(List.Data.class, "list");
        registerSubtype(Option.Data.class, "option");
        registerSubtype(Parameter.Data.class, "parameter");
        registerSubtype(Property.Data.class, "property");
        registerSubtype(Reference.Data.class, "reference");
        registerSubtype(Server.Data.class, "server");
        registerSubtype(SubType.Data.class, "subType");
        registerSubtype(Task.Data.class, "task");
        registerSubtype(Type.ChoiceData.class, "choiceType");
        registerSubtype(Type.CompositeData.class, "compoundType");
        registerSubtype(Type.Instance.class, "typeInstance");
        registerSubtype(Type.Instances.class, "typeInstances");
        registerSubtype(Type.InstanceMap.class, "typeInstanceMap");
        registerSubtype(Type.ObjectData.class, "objectType");
        registerSubtype(Type.PrimitiveData.class, "simpleType");
        registerSubtype(Type.RegexData.class, "regexType");
        registerSubtype(User.Data.class, "user");
        registerSubtype(Value.Data.class, "value");
    }
}
