package errorcraft.itematic.mixin.entity.ai.goal;

import com.llamalad7.mixinextras.sugar.Local;
import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StepAndDestroyBlockGoal.class)
public class StepAndDestroyBlockGoalExtender {
    @Redirect(
        method = "tick",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack tickNewItemStackUseRegistryEntry(ItemConvertible item, @Local World world) {
        RegistryEntry<Item> entry = world.getRegistryManager().get(RegistryKeys.ITEM).entryOf(ItemKeys.EGG);
        return new ItemStack(entry);
    }
}
