package com.intuso.housemate.client.v1_0.real.api.type;

import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;

/**
 * Enumeration of the days of the week
 */
@TypeInfo(id = "day", name = "Day", description = "Day of the week")
public enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;
}
