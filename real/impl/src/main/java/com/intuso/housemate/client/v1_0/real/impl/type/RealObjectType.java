package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.type.TypeSerialiser;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.real.impl.RealNodeImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Type for an object from the object tree
 */
public abstract class RealObjectType<O extends Object<?>>
        extends RealTypeImpl<RealObjectType.Reference<O>> {

    private final static Joiner JOINER = Joiner.on("/");
    private final static Splitter SPLITTER = Splitter.on("/");

    private final Serialiser<O> serialiser;

    /**
     * @param listenersFactory
     * @param root the root to get the object from
     */
    @Inject
    public RealObjectType(Logger logger, String id, String name, String description, TypeSpec typeSpec, ListenersFactory listenersFactory, RealNodeImpl root) {
        super(logger, new ObjectData(id, name, description), typeSpec, listenersFactory);
        serialiser = new Serialiser<>(root);
    }

    @Override
    public Instance serialise(Reference<O> o) {
        return serialiser.serialise(o);
    }

    @Override
    public Reference<O> deserialise(Instance instance) {
        return serialiser.deserialise(instance);
    }

    /**
     * Reference for an object containing the object's path, and the object if it exists
     * @param <O>
     */
    public static class Reference<O extends Object<?>> {

        private final String[] path;
        private O object;

        /**
         * @param path the path to the object
         */
        public Reference(String[] path) {
            this(path, null);
        }

        /**
         * @param object the object
         */
        public Reference(O object) {
            this(object == null ? null : /* todo object.getPath()*/null, object);
        }

        /**
         * @param path the object's path
         * @param object the object
         */
        private Reference(String[] path, O object) {
            this.path = path;
            this.object = object;
        }

        /**
         * Gets the path
         * @return the path
         */
        public String[] getPath() {
            return path;
        }

        /**
         * Gets the object
         * @return the object
         */
        public O getObject() {
            return object;
        }

        /**
         * Sets the object
         * @param object the object
         */
        public void setObject(O object) {
            // todo check the object's path matches the path inside this reference
            this.object = object;
        }
    }

    /**
     * Serialiser for an object reference
     * @param <O> the type of the object to serialise
     */
    public static class Serialiser<O extends Object<?>> implements TypeSerialiser<Reference<O>> {

        private final RealNodeImpl root;

        /**
         * @param root the root to get the object from
         */
        @Inject
        public Serialiser(RealNodeImpl root) {
            this.root = root;
        }

        @Override
        public Instance serialise(Reference<O> o) {
            if(o == null)
                return null;
            return new Instance(JOINER.join(o.getPath()));
        }

        @Override
        public Reference<O> deserialise(Instance value) {
            if(value == null || value.getValue() == null)
                return null;
            List<String> pathList = Lists.newArrayList(SPLITTER.split(value.getValue()));
            String[] path = pathList.toArray(new String[pathList.size()]);
            return new Reference(path, /* todo (O) RemoteObject.getChild((RemoteObject) root, path, 1)*/null);
        }
    }
}
