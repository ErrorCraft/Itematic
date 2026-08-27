package net.errorcraft.itematic.mixin.world.entity.animal.golem;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CopperGolem.class)
public abstract class CopperGolemExtender extends MobExtender {
    protected CopperGolemExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;SHEARS:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isShearsCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.SHEARS);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;HONEYCOMB:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isHoneycombCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.HONEYCOMB);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.COPPER_GOLEM_SPAWN_EGG;
    }
}
