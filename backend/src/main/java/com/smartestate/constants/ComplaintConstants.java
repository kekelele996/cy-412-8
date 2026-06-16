package com.smartestate.constants;

import java.util.List;

public final class ComplaintConstants {
    public static final String PENDING = "pending";
    public static final String REPLIED = "replied";
    public static final String RESOLVED = "resolved";

    public static final List<String> ALL_STATUSES = List.of(PENDING, REPLIED, RESOLVED);
    public static final List<String> ALL_CATEGORIES = List.of("complaint", "suggestion");

    private ComplaintConstants() {
    }
}
