package net.errorcraft.itematic.mixin.gametest;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTestState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.test.StructureTestListener")
public class StructureTestListenerExtender {
    @Unique
    private static ServerWorld world;

    @Inject(
        method = "createTestOutputLectern",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/test/StructureTestListener;createBookWithText(Ljava/lang/String;ZLjava/lang/String;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static void storeWorld(GameTestState test, String output, CallbackInfo info) {
        world = test.getWorld();
    }

    @Inject(
        method = "createTestOutputLectern",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/test/StructureTestListener;createBookWithText(Ljava/lang/String;ZLjava/lang/String;)Lnet/minecraft/item/ItemStack;",
            shift = At.Shift.AFTER
        )
    )
    private static void resetWorld(GameTestState test, String output, CallbackInfo info) {
        world = null;
    }

    @Redirect(
        method = "createBookWithText",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForWritableBookUseCreateStack(ItemConvertible item) {
        return world.itematic$createStack(ItemKeys.WRITABLE_BOOK);
    }
}
