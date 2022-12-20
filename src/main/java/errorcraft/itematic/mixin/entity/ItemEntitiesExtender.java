package errorcraft.itematic.mixin.entity;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ItemEntity.class, EyeOfEnderEntity.class, ArmorStandEntity.class, ItemFrameEntity.class, MobEntity.class, AbstractDonkeyEntity.class, AbstractHorseEntity.class, HorseEntity.class, LlamaEntity.class, AbstractFireballEntity.class, FireworkRocketEntity.class, TridentEntity.class, ThrownItemEntity.class })
public abstract class ItemEntitiesExtender extends Entity {
    private ItemEntitiesExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "readCustomDataFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack readCustomDataFromNbtUseDynamicRegistry(NbtCompound nbt) {
        return ItemStackUtil.readFromNbt(nbt, this.world.getRegistryManager());
    }
}
