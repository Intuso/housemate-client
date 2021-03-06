package com.intuso.housemate.client.v1_0.serialisation.json;

import com.intuso.housemate.client.v1_0.api.object.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by tomc on 27/10/14.
 */
public class TestJsonSerialisation {

    private JsonSerialiser serialiser;

    @Before
    public void init() throws IOException {
        serialiser = new JsonSerialiser();
    }

    @Test
    public void testCommandData() {
        Command.Data startData = new Command.Data("testCommand", "Test Command", "A command for testing");
        String json = serialiser.serialise(startData);
        Command.Data endData = serialiser.deserialise(json, Command.Data.class);
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
    }

    @Test
    public void testValueData() {
        Value.Data startData = new Value.Data("testValue", "Test Value", "A value for testing", "type", 1, 1, new Type.Instances());
        String json = serialiser.serialise(startData);
        Value.Data endData = serialiser.deserialise(json, Value.Data.class);
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
    }

    @Test
    public void testPropertyData() {
        Property.Data startData = new Property.Data("testProperty", "Test Property", "A property for testing", "type", 1, 1, new Type.Instances());
        String json = serialiser.serialise(startData);
        Property.Data endData = serialiser.deserialise(json, Property.Data.class);
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
    }

    @Test
    public void testDeviceData() {
        Device.Connected.Data startData = new Device.Connected.Data("testDevice", "Test Device", "A device for testing");
        String json = serialiser.serialise(startData);
        Device.Data endData = serialiser.deserialise(json, Device.Data.class);
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
    }

    @Test
    public void testTypeInstancesData() {
        Type.Instances startTypeInstances = new Type.Instances(new Type.Instance("value"));
        String json = serialiser.serialise(startTypeInstances);
        Type.Instances endTypeInstances = serialiser.deserialise(json, Type.Instances.class);
        assertEquals(startTypeInstances.size(), endTypeInstances.size());
        assertEquals(startTypeInstances.getFirstValue(), endTypeInstances.getFirstValue());
    }
}
