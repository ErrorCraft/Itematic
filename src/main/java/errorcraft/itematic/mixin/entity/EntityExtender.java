package errorcraft.itematic.mixin.entity;

import errorcraft.itematic.access.entity.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityExtender implements EntityAccess {
    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ItemStack stack, float yOffset);

    @Override
    public ItemEntity dropItem(RegistryEntry<Item> entry, int yOffset) {
        return this.dropStack(new ItemStack(entry), yOffset);
    }
}
