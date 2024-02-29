package net.errorcraft.itematic.mixin.entity.boss;

import net.errorcraft.itematic.access.entity.boss.BossBarManagerAccess;
import net.errorcraft.itematic.access.entity.boss.CommandBossBarAccess;
import net.errorcraft.itematic.entity.boss.CommandBossBarUtil;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarManager.class)
public class BossBarManagerExtender implements BossBarManagerAccess {
    @Unique
    private RegistryWrapper.WrapperLookup lookup;

    @Redirect(
        method = "toNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/boss/CommandBossBar;toNbt()Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound toNbtUseDynamicRegistry(CommandBossBar instance) {
        return ((CommandBossBarAccess) instance).itematic$toNbt(this.lookup);
    }

    @Inject(
        method = "readNbt",
        at = @At("HEAD")
    )
    private void readNbtSetLookup(NbtCompound nbt, CallbackInfo info) {
        CommandBossBarUtil.setLookup(this.lookup);
    }

    @Inject(
        method = "readNbt",
        at = @At("TAIL")
    )
    private void readNbtResetLookup(NbtCompound nbt, CallbackInfo info) {
        CommandBossBarUtil.setLookup(null);
    }

    @Override
    public void itematic$setLookup(RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
    }
}
