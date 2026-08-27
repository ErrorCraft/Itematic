package net.errorcraft.itematic.mixin.world.entity.animal.golem;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowGolem.class)
public abstract class SnowGolemExtender extends MobExtender {
    protected SnowGolemExtender(EntityType<? extends AbstractGolem> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isShearsCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.SHEARS);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.SNOW_GOLEM_SPAWN_EGG;
    }
}
