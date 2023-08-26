package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviderTags;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemGroups.class)
public class ItemGroupsExtender {
    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;BUILDING_BLOCKS:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup buildingBlocksSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.BRICKS);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.BUILDING_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;COLORED_BLOCKS:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup coloredBlocksSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.CYAN_WOOL);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.COLORED_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;NATURAL:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup naturalSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.GRASS_BLOCK);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.NATURAL_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;FUNCTIONAL:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup functionalSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.OAK_SIGN);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;REDSTONE:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup redstoneSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.REDSTONE);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.REDSTONE_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;HOTBAR:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup hotbarSetIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.BOOKSHELF);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;SEARCH:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup searchSetIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.COMPASS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;TOOLS:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup toolsSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.DIAMOND_PICKAXE);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;COMBAT:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup combatSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.NETHERITE_SWORD);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.COMBAT);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;FOOD_AND_DRINK:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup foodAndDrinkSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.GOLDEN_APPLE);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.FOOD_AND_DRINKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;INGREDIENTS:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup ingredientsSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.IRON_INGOT);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.INGREDIENTS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;SPAWN_EGGS:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup spawnEggsSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.PIG_SPAWN_EGG);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.SPAWN_EGGS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;OPERATOR:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup operatorSetEntryTagAndIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.COMMAND_BLOCK);
        original.setEntryProviderTag(ItemGroupEntryProviderTags.OP_BLOCKS);
        return original;
    }

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$Builder;build()Lnet/minecraft/item/ItemGroup;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/ItemGroups;INVENTORY:Lnet/minecraft/registry/RegistryKey;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static ItemGroup inventorySetIcon(ItemGroup original) {
        original.setIconKey(ItemKeys.CHEST);
        return original;
    }
}
