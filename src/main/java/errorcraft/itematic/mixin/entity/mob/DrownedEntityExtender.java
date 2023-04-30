package errorcraft.itematic.mixin.entity.mob;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DrownedEntity.class)
public class DrownedEntityExtender extends ZombieEntity {
    public DrownedEntityExtender(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 1
        )
    )
    private ItemStack initEquipmentNewItemStackUseRegistryEntry(ItemConvertible item) {
        RegistryEntry<Item> entry = this.getWorld().getRegistryManager().get(RegistryKeys.ITEM).entryOf(ItemKeys.FISHING_ROD);
        return new ItemStack(entry);
    }
}
