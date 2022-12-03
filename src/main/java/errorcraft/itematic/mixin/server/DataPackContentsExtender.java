package errorcraft.itematic.mixin.server;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.CommandManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataPackContents.class)
public class DataPackContentsExtender {
    @Shadow
    @Final
    private ServerAdvancementLoader serverAdvancementLoader;

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/DataPackContents;serverAdvancementLoader:Lnet/minecraft/server/ServerAdvancementLoader;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void useDynamicRegistryManager(DynamicRegistryManager.Immutable dynamicRegistryManager, FeatureSet enabledFeatures, CommandManager.RegistrationEnvironment environment, int functionPermissionLevel, CallbackInfo ci) {
        this.serverAdvancementLoader.setRegistryManager(dynamicRegistryManager);
    }
}
