package errorcraft.itematic.util;

import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class ActionResultUtil {
    private ActionResultUtil() {}

    public static <T> TypedActionResult<T> max(TypedActionResult<T> newResult, ActionResult currentResult) {
        if (getPoints(newResult.getResult()) >= getPoints(currentResult)) {
            return newResult;
        }
        return new TypedActionResult<>(currentResult, newResult.getValue());
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
