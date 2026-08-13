package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.tags.ItemGroupEntryProviderTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CreativeModeTabs.class)
public class ItemGroupsExtender {
    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;BUILDING_BLOCKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab buildingBlocksSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.BRICKS);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.BUILDING_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;COLORED_BLOCKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab coloredBlocksSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.CYAN_WOOL);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.COLORED_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;NATURAL_BLOCKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab naturalSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.GRASS_BLOCK);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.NATURAL_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;FUNCTIONAL_BLOCKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab functionalSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.OAK_SIGN);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;REDSTONE_BLOCKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab redstoneSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.REDSTONE);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.REDSTONE_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;HOTBAR:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab hotbarSetIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.BOOKSHELF);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;SEARCH:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab searchSetIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.COMPASS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;TOOLS_AND_UTILITIES:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab toolsSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.DIAMOND_PICKAXE);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;COMBAT:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab combatSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.NETHERITE_SWORD);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.COMBAT);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;FOOD_AND_DRINKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab foodAndDrinkSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.GOLDEN_APPLE);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.FOOD_AND_DRINKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;INGREDIENTS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab ingredientsSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.IRON_INGOT);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.INGREDIENTS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;SPAWN_EGGS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab spawnEggsSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.PIG_SPAWN_EGG);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.SPAWN_EGGS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;OP_BLOCKS:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab operatorSetEntryTagAndIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.COMMAND_BLOCK);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.OP_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$Builder;build()Lnet/minecraft/world/item/CreativeModeTab;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/CreativeModeTabs;INVENTORY:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static CreativeModeTab inventorySetIcon(CreativeModeTab original) {
        original.itematic$setIconKey(ItemIds.CHEST);
        return original;
    }
}
