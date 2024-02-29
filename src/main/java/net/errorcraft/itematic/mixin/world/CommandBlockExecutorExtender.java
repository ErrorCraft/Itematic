package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.CommandBlockExecutorAccess;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlockExecutor.class)
public abstract class CommandBlockExecutorExtender implements CommandBlockExecutorAccess {
    @Shadow
    public abstract void readNbt(NbtCompound nbt);

    @Shadow
    public abstract NbtCompound writeNbt(NbtCompound nbt);

    @Unique
    private RegistryWrapper.WrapperLookup lookup;

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
        )
    )
    private String toJsonStringUseDynamicRegistry(Text text) {
        return TextUtil.toJsonString(text, this.lookup);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private MutableText fromJsonUseDynamicRegistry(String json) {
        return TextUtil.fromJsonString(json, this.lookup);
    }

    @Override
    public void itematic$readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
        this.readNbt(nbt);
        this.lookup = null;
    }

    @Override
    public NbtCompound itematic$writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
        nbt = this.writeNbt(nbt);
        this.lookup = null;
        return nbt;
    }
}
