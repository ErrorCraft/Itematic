package net.errorcraft.itematic.mixin.gametest.framework;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screens.inventory.TestInstanceBlockEditScreen;
import net.minecraft.gametest.framework.StructureUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TestInstanceBlockEditScreen.class)
public class TestInstanceBlockEditScreenExtender {
    @ModifyExpressionValue(
        method = "init",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/SharedConstants;IS_RUNNING_IN_IDE:Z",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static boolean addExportButtonIfStructureTargetDirectoryIsSet(boolean original) {
        return StructureUtils.testStructuresTargetDir != null;
    }
}
