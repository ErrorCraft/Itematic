package net.errorcraft.itematic.mixin.entity.boss;

import net.errorcraft.itematic.access.entity.boss.CommandBossBarAccess;
import net.errorcraft.itematic.entity.boss.CommandBossBarUtil;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBossBar.class)
public abstract class CommandBossBarExtender implements CommandBossBarAccess {
    @Shadow
    public abstract NbtCompound toNbt();

    @Unique
    private RegistryWrapper.WrapperLookup lookup;

    @Redirect(
        method = "toNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
        )
    )
    private String toJsonStringUseDynamicRegistry(Text text) {
        return TextUtil.toJsonString(text, this.lookup);
    }

    @Redirect(
        method = "fromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private static MutableText fromJsonUseDynamicRegistry(String json) {
        return TextUtil.fromJsonString(json, CommandBossBarUtil.lookup());
    }

    @Override
    public NbtCompound itematic$toNbt(RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
        NbtCompound nbt = this.toNbt();
        this.lookup = null;
        return nbt;
    }
}
