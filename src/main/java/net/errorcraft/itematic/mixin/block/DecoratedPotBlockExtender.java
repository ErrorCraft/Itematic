package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.block.BlockAccess;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockExtender implements BlockAccess {
    @ModifyExpressionValue(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/Sherds;equals(Ljava/lang/Object;)Z"
        )
    )
    private boolean defaultSherdsAlwaysTrue(boolean original, @Local(argsOnly = true) List<Text> tooltip, @Local(argsOnly = true) DynamicRegistryManager registryManager, @Local Sherds sherds) {
        if (!original && registryManager != null) {
            tooltip.add(ScreenTexts.EMPTY);
            for (RegistryEntry<Item> entry : sherds.itematic$entriesForwards(registryManager)) {
                tooltip.add(new ItemStack(entry).getName().copyContentOnly().formatted(Formatting.GRAY));
            }
        }
        return true;
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/Sherds;stream()Ljava/util/List;"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static List<RegistryEntry<Item>> streamSherdsUseRegistryEntry(Sherds instance, DecoratedPotBlockEntity blockEntity) {
        return instance.itematic$entries(blockEntity.getWorld().getRegistryManager());
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
        )
    )
    private static <E> E nextItemReturnNull(Iterator<E> instance) {
        return null;
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getDefaultStack()Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackUseRegistryEntry(Item instance, @Local Iterator<RegistryEntry<Item>> iterator) {
        return new ItemStack(iterator.next());
    }

    @Override
    public void itematic$addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.POT_DECORATIONS, Sherds.DEFAULT);
    }
}
