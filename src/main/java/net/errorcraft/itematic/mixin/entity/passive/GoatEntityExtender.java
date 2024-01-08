package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.item.GoatHornItemAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GoatEntity.class)
public abstract class GoatEntityExtender extends AnimalEntity {
    protected GoatEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getGoatHornStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/GoatHornItem;getStackForInstrument(Lnet/minecraft/item/Item;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack getStackForInstrumentUseCreateStack(Item item, RegistryEntry<Instrument> instrument) {
        ItemStack stack = this.getWorld().itematic$createStack(ItemKeys.GOAT_HORN);
        GoatHornItemAccessor.setInstrument(stack, instrument);
        return stack;
    }
}
