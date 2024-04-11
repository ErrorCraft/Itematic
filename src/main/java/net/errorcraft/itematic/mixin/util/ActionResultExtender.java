package net.errorcraft.itematic.mixin.util;

import net.errorcraft.itematic.access.util.ActionResultAccess;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Function;

@SuppressWarnings({"DataFlowIssue", "UnreachableCode"})
@Mixin(ActionResult.class)
public class ActionResultExtender implements ActionResultAccess {
    @Shadow
    @Final
    public static ActionResult SUCCESS;

    @Shadow
    @Final
    public static ActionResult SUCCESS_NO_ITEM_USED;

    @Shadow
    @Final
    public static ActionResult CONSUME;

    @Shadow
    @Final
    public static ActionResult CONSUME_PARTIAL;

    @Shadow
    @Final
    public static ActionResult PASS;

    @Shadow
    @Final
    public static ActionResult FAIL;

    @Unique
    private Function<ActionResult, ActionResult> merger;

    static {
        ((ActionResultExtender)(Object) SUCCESS).merger = other -> SUCCESS;
        ((ActionResultExtender)(Object) SUCCESS_NO_ITEM_USED).merger = other -> other.shouldIncrementStat() ? SUCCESS : SUCCESS_NO_ITEM_USED;
        ((ActionResultExtender)(Object) CONSUME).merger = other -> other.shouldSwingHand() ? SUCCESS : CONSUME;
        ((ActionResultExtender)(Object) CONSUME_PARTIAL).merger = other -> other.isAccepted() ? other : CONSUME_PARTIAL;
        ((ActionResultExtender)(Object) PASS).merger = other -> other;
        ((ActionResultExtender)(Object) FAIL).merger = other -> FAIL;
    }

    @Override
    public ActionResult itematic$merge(ActionResult other) {
        if (other == FAIL) {
            return FAIL;
        }
        return this.merger.apply(other);
    }
}
