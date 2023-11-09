package net.errorcraft.itematic.util;

import net.minecraft.util.ActionResult;

public class ActionResultUtil {
    private ActionResultUtil() {}

    public static ActionResult max(ActionResult left, ActionResult right) {
        if (getPoints(left) >= getPoints(right)) {
            return left;
        }
        return right;
    }

    public static int getPoints(ActionResult actionResult) {
        return switch (actionResult) {
            case SUCCESS -> 3;
            case CONSUME -> 2;
            case CONSUME_PARTIAL -> 1;
            case PASS -> 0;
            case FAIL -> -1;
        };
    }
}
