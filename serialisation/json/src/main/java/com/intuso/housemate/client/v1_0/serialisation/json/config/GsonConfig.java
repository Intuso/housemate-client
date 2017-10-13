package com.intuso.housemate.client.v1_0.serialisation.json.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by tomc on 28/10/14.
 */
public class GsonConfig {

//    private final static TypeToken SCS_TYPE = new TypeToken<ConnectionStatus>() {};

    public static Gson createGson() {

        GsonBuilder builder = new GsonBuilder()
                // register main sub-class type adapters
                .registerTypeAdapterFactory(new DataAdapter())
                .registerTypeAdapterFactory(new ViewAdapter())
                .registerTypeAdapterFactory(new SerializableAdapter());

        // add fixes for enums
//        builder.registerTypeAdapter(SCS_TYPE.getType(), new EnumTypeAdapter<ConnectionStatus>(PayloadAdapter.TYPE_FIELD_NAME, "serverConnectionStatus", PayloadAdapter.ENUM_VALUE_FIELD_NAME));

        /*try {
            // there is a problem serialising ListData.class, because it is generic. Gson only finds the DATA parameter
            // of the Data class, which doesn't extend from HousemateData so doesn't get any type info in the json for
            // the child data, causing deserialisation problems
            Type untypedListType = $Gson$Types.resolve(new TypeToken<ListData>() {}.getType(), ListData.class, ListData.class.getGenericSuperclass());
            TypeToken untypedChildDataType = TypeToken.get($Gson$Types.resolve(untypedListType, ListData.class, Data.class.getDeclaredField("childData").getGenericType()));
            TypeToken typedChildDataType = new TypeToken<Map<String, SimpleObjectData<SimpleObjectData<?>>>>() {};

            // generic type fixes
            builder.registerTypeAdapterFactory(new TypeOverrideAdapterFactory(untypedChildDataType, typedChildDataType));
        } catch(NoSuchFieldException e) {
            // todo log that this happened, failure to register this fix will mean class type is not always added to data objects, breaking deserialisation
        }*/

        return builder.create();
    }
}
