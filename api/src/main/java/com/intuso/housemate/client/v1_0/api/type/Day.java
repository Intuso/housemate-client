package com.intuso.housemate.client.v1_0.api.type;

import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Enumeration of the days of the week
 */
@Id(value = "day", name = "Day", description = "Day of the week")
public enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;
}
