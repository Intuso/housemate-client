package com.intuso.housemate.client.v1_0.real.api;

import com.intuso.housemate.comms.v1_0.api.payload.AutomationData;
import com.intuso.housemate.object.v1_0.api.Automation;
import com.intuso.housemate.object.v1_0.api.Condition;
import org.slf4j.Logger;

public interface RealAutomation
        extends Automation<RealCommand,
        RealCommand,
        RealCommand,
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealCondition<?>,
        RealList<RealCondition<?>>,
        RealTask<?>,
        RealList<RealTask<?>>,
        RealAutomation>,
        Condition.Listener<RealCondition<?>>,
        RealCondition.Container {

    boolean isRunning();

    interface Container extends Automation.Container<RealList<RealAutomation>>, RemoveCallback {
        RealAutomation createAndAddAutomation(AutomationData data);
        void addAutomation(RealAutomation automation);
    }

    interface RemoveCallback {
        void removeAutomation(RealAutomation automation);
    }

    interface Factory {
        RealAutomation create(Logger logger, AutomationData data, RemoveCallback removeCallback);
    }
}
