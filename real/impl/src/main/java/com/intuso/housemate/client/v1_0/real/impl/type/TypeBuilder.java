package com.intuso.housemate.client.v1_0.real.impl.type;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.housemate.client.v1_0.real.impl.RealSubTypeImpl;
import com.intuso.housemate.client.v1_0.real.impl.RealTypeImpl;
import com.intuso.housemate.client.v1_0.real.impl.ioc.Types;
import com.intuso.housemate.plugin.v1_0.api.annotations.Composite;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.annotations.Regex;
import com.intuso.housemate.plugin.v1_0.api.type.RegexType;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by tomc on 16/05/16.
 */
public class TypeBuilder {

    private final Logger typesLogger;
    private final EnumChoiceType.Factory enumChoiceTypeFactory;
    private final RealRegexType.Factory regexTypeFactory;
    private final RealCompositeType.Factory compositeTypeFactory;

    @Inject
    public TypeBuilder(@Types Logger typesLogger, RealCompositeType.Factory compositeTypeFactory, EnumChoiceType.Factory enumChoiceTypeFactory, RealRegexType.Factory regexTypeFactory) {
        this.typesLogger = typesLogger;
        this.regexTypeFactory = regexTypeFactory;
        this.compositeTypeFactory = compositeTypeFactory;
        this.enumChoiceTypeFactory = enumChoiceTypeFactory;
    }

    public RealTypeImpl<?> buildType(Injector injector, Class<?> typeClass) {
        if(Enum.class.isAssignableFrom(typeClass)) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            return enumChoiceTypeFactory.create(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    typeClass);
        } else if(RegexType.class.isAssignableFrom(typeClass)) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            String regex = getRegex(typeClass);
            RegexType.Factory<?> factory = getRegexTypeFactory(injector, typeClass);
            return regexTypeFactory.create(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    regex,
                    factory);
        } else if(typeClass.getAnnotation(Composite.class) != null) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            return compositeTypeFactory.create(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    parseSubTypes(typeClass));
        }
        throw new HousemateException("Unable to determine type of typeClass " + typeClass.getName());
    }

    private String getId(Class<?> typeClass) {
        Id id = typeClass.getAnnotation(Id.class);
        if(id == null)
            throw new HousemateException(typeClass.getName() + " is missing " + Id.class + " annotation");
        return id.value();
    }

    private String getName(Class<?> typeClass, String id) {
        Id typeInfo = typeClass.getAnnotation(Id.class);
        if(typeInfo == null)
            throw new HousemateException(typeClass.getName() + " is missing " + Id.class + " annotation");
        return typeInfo.name().length() == 0 ? id : typeInfo.name();
    }

    private String getDescription(Class<?> typeClass, String name) {
        Id id = typeClass.getAnnotation(Id.class);
        if(id == null)
            throw new HousemateException(typeClass.getName() + " is missing " + Id.class + " annotation");
        return id.description().length() == 0 ? name : id.description();
    }

    private String getRegex(Class<?> typeClass) {
        Regex regex = typeClass.getAnnotation(Regex.class);
        if(regex == null)
            throw new HousemateException(typeClass.getName() + " is missing " + Id.class + " annotation");
        return regex.regex();
    }

    private RegexType.Factory<?> getRegexTypeFactory(Injector injector, Class<?> typeClass) {
        Regex regex = typeClass.getAnnotation(Regex.class);
        if(regex == null)
            throw new HousemateException(typeClass.getName() + " is missing " + Id.class + " annotation");
        return injector.getInstance(regex.factory());
    }

    private List<RealSubTypeImpl<?>> parseSubTypes(Class<?> typeClass) {
        return Lists.newArrayList(); // todo
    }
}
