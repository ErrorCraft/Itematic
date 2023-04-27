package errorcraft.itematic.mixin.block;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BambooSaplingBlock.class)
public class BambooSaplingBlockExtender {
    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getPickStackNewItemStackUseRegistryEntry(ItemConvertible item, BlockView world) {
        RegistryEntry<Item> entry = ((World) world).getRegistryManager()
            .get(RegistryKeys.ITEM)
            .entryOf(ItemKeys.BAMBOO);
        return new ItemStack(entry);
    }
}
