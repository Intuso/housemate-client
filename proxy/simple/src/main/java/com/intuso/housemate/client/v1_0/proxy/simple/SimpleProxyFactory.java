package com.intuso.housemate.client.v1_0.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 18:47
* To change this template use File | Settings | File Templates.
*/
public class SimpleProxyFactory implements ProxyObject.Factory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Log log;
    private final Provider<ProxyObject.Factory<ApplicationData, SimpleProxyApplication>> applicationFactory;
    private final Provider<ProxyObject.Factory<ApplicationInstanceData, SimpleProxyApplicationInstance>> applicationInstanceFactory;
    private final Provider<ProxyObject.Factory<AutomationData, SimpleProxyAutomation>> automationFactory;
    private final Provider<ProxyObject.Factory<CommandData, SimpleProxyCommand>> commandFactory;
    private final Provider<ProxyObject.Factory<ConditionData, SimpleProxyCondition>> conditionFactory;
    private final Provider<ProxyObject.Factory<DeviceData, SimpleProxyDevice>> deviceFactory;
    private final Provider<ProxyObject.Factory<FeatureData, SimpleProxyFeature>> featureFactory;
    private final Provider<ProxyObject.Factory<HardwareData, SimpleProxyHardware>> hardwareFactory;
    private final Provider<ProxyObject.Factory<ListData<HousemateData<?>>, SimpleProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>> listFactory;
    private final Provider<ProxyObject.Factory<OptionData, SimpleProxyOption>> optionFactory;
    private final Provider<ProxyObject.Factory<ParameterData, SimpleProxyParameter>> parameterFactory;
    private final Provider<ProxyObject.Factory<PropertyData, SimpleProxyProperty>> propertyFactory;
    private final Provider<ProxyObject.Factory<ServerData, SimpleProxyServer>> serverFactory;
    private final Provider<ProxyObject.Factory<SubTypeData, SimpleProxySubType>> subTypeFactory;
    private final Provider<ProxyObject.Factory<TaskData,SimpleProxyTask>> taskFactory;
    private final Provider<ProxyObject.Factory<TypeData<HousemateData<?>>, SimpleProxyType>> typeFactory;
    private final Provider<ProxyObject.Factory<UserData, SimpleProxyUser>> userFactory;
    private final Provider<ProxyObject.Factory<ValueData, SimpleProxyValue>> valueFactory;

    @Inject
    public SimpleProxyFactory(Log log,
                              Provider<ProxyObject.Factory<ApplicationData, SimpleProxyApplication>> applicationFactory,
                              Provider<ProxyObject.Factory<ApplicationInstanceData, SimpleProxyApplicationInstance>> applicationInstanceFactory,
                              Provider<ProxyObject.Factory<AutomationData, SimpleProxyAutomation>> automationFactory,
                              Provider<ProxyObject.Factory<CommandData, SimpleProxyCommand>> commandFactory,
                              Provider<ProxyObject.Factory<ConditionData, SimpleProxyCondition>> conditionFactory,
                              Provider<ProxyObject.Factory<DeviceData, SimpleProxyDevice>> deviceFactory,
                              Provider<ProxyObject.Factory<FeatureData, SimpleProxyFeature>> featureFactory,
                              Provider<ProxyObject.Factory<HardwareData, SimpleProxyHardware>> hardwareFactory,
                              Provider<ProxyObject.Factory<ListData<HousemateData<?>>, SimpleProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>> listFactory,
                              Provider<ProxyObject.Factory<OptionData, SimpleProxyOption>> optionFactory,
                              Provider<ProxyObject.Factory<ParameterData, SimpleProxyParameter>> parameterFactory,
                              Provider<ProxyObject.Factory<PropertyData, SimpleProxyProperty>> propertyFactory,
                              Provider<ProxyObject.Factory<ServerData, SimpleProxyServer>> serverFactory,
                              Provider<ProxyObject.Factory<SubTypeData, SimpleProxySubType>> subTypeFactory,
                              Provider<ProxyObject.Factory<TaskData,SimpleProxyTask>> taskFactory,
                              Provider<ProxyObject.Factory<TypeData<HousemateData<?>>, SimpleProxyType>> typeFactory,
                              Provider<ProxyObject.Factory<UserData, SimpleProxyUser>> userFactory,
                              Provider<ProxyObject.Factory<ValueData, SimpleProxyValue>> valueFactory) {
        this.log = log;
        this.applicationFactory = applicationFactory;
        this.applicationInstanceFactory = applicationInstanceFactory;
        this.automationFactory = automationFactory;
        this.commandFactory = commandFactory;
        this.conditionFactory = conditionFactory;
        this.deviceFactory = deviceFactory;
        this.featureFactory = featureFactory;
        this.hardwareFactory = hardwareFactory;
        this.listFactory = listFactory;
        this.optionFactory = optionFactory;
        this.parameterFactory = parameterFactory;
        this.propertyFactory = propertyFactory;
        this.serverFactory = serverFactory;
        this.subTypeFactory = subTypeFactory;
        this.taskFactory = taskFactory;
        this.typeFactory = typeFactory;
        this.userFactory = userFactory;
        this.valueFactory = valueFactory;
    }

    @Override
    public ProxyObject<?, ?, ?, ?, ?> create(HousemateData<?> data) {
        if(data instanceof ParameterData)
            return parameterFactory.get().create((ParameterData) data);
        else if(data instanceof CommandData)
            return commandFactory.get().create((CommandData) data);
        else if(data instanceof ConditionData)
            return conditionFactory.get().create((ConditionData) data);
        else if(data instanceof ApplicationData)
            return applicationFactory.get().create((ApplicationData) data);
        else if(data instanceof ApplicationInstanceData)
            return applicationInstanceFactory.get().create((ApplicationInstanceData) data);
        else if(data instanceof UserData)
            return userFactory.get().create((UserData) data);
        else if(data instanceof TaskData)
            return taskFactory.get().create((TaskData) data);
        else if(data instanceof DeviceData)
            return deviceFactory.get().create((DeviceData) data);
        else if(data instanceof FeatureData)
            return featureFactory.get().create((FeatureData) data);
        else if(data instanceof HardwareData)
            return hardwareFactory.get().create((HardwareData) data);
        else if(data instanceof ListData)
            return listFactory.get().create((ListData<HousemateData<?>>) data);
        else if(data instanceof OptionData)
            return optionFactory.get().create((OptionData) data);
        else if(data instanceof PropertyData)
            return propertyFactory.get().create((PropertyData) data);
        else if(data instanceof AutomationData)
            return automationFactory.get().create((AutomationData) data);
        else if(data instanceof ServerData)
            return serverFactory.get().create((ServerData) data);
        else if(data instanceof SubTypeData)
            return subTypeFactory.get().create((SubTypeData) data);
        else if(data instanceof TypeData)
            return typeFactory.get().create((TypeData) data);
        else if(data instanceof ValueData)
            return valueFactory.get().create((ValueData) data);
        log.e("Don't know how to create an object from " + data.getClass().getName());
        return null;
    }
}
