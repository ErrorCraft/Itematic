package net.errorcraft.itematic.mixin.gametest;

import net.errorcraft.itematic.access.gametest.GameTestStateAccess;
import net.minecraft.test.GameTestState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GameTestState.class)
public class GameTestStateExtender implements GameTestStateAccess {
    @Unique
    private final List<Runnable> whenFinished = new ArrayList<>();

    @Inject(
        method = "complete",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/test/GameTestState;completed:Z",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void callWhenFinishedActions(CallbackInfo info) {
        for (Runnable runnable : this.whenFinished) {
            runnable.run();
        }
    }

    @Override
    public void itematic$whenFinished(Runnable action) {
        this.whenFinished.add(action);
    }
}
