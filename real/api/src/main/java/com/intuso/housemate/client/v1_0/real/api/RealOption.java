package com.intuso.housemate.client.v1_0.real.api;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Option;
import org.slf4j.Logger;

public interface RealOption<SUB_TYPES extends RealList<? extends RealSubType<?, ?, ?>>,
        OPTION extends RealOption<SUB_TYPES, OPTION>>
        extends Option<SUB_TYPES,
        OPTION> {

    interface Factory<OPTION extends RealOption<?, ?>> {
        OPTION create(Logger logger, @Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description);
    }
}
