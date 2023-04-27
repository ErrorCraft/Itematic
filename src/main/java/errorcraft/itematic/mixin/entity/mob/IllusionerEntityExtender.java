package errorcraft.itematic.mixin.entity.mob;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IllusionerEntity.class)
public abstract class IllusionerEntityExtender extends SpellcastingIllagerEntity {
    protected IllusionerEntityExtender(EntityType<? extends SpellcastingIllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initialize",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack initializeNewItemStackUseRegistryEntry(ItemConvertible item) {
        RegistryEntry<Item> entry = this.world.getRegistryManager().get(RegistryKeys.ITEM).entryOf(ItemKeys.BOW);
        return new ItemStack(entry);
    }
}
