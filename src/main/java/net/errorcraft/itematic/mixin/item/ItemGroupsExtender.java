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
        original.itematic$setIconKey(ItemKeys.BRICKS);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.BUILDING_BLOCKS);
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
        original.itematic$setIconKey(ItemKeys.CYAN_WOOL);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.COLORED_BLOCKS);
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
        original.itematic$setIconKey(ItemKeys.GRASS_BLOCK);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.NATURAL_BLOCKS);
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
        original.itematic$setIconKey(ItemKeys.OAK_SIGN);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS);
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
        original.itematic$setIconKey(ItemKeys.REDSTONE);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.REDSTONE_BLOCKS);
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
        original.itematic$setIconKey(ItemKeys.BOOKSHELF);
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
        original.itematic$setIconKey(ItemKeys.COMPASS);
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
        original.itematic$setIconKey(ItemKeys.DIAMOND_PICKAXE);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES);
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
        original.itematic$setIconKey(ItemKeys.NETHERITE_SWORD);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.COMBAT);
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
        original.itematic$setIconKey(ItemKeys.GOLDEN_APPLE);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.FOOD_AND_DRINKS);
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
        original.itematic$setIconKey(ItemKeys.IRON_INGOT);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.INGREDIENTS);
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
        original.itematic$setIconKey(ItemKeys.PIG_SPAWN_EGG);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.SPAWN_EGGS);
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
        original.itematic$setIconKey(ItemKeys.COMMAND_BLOCK);
        original.itematic$setEntryProviderTag(ItemGroupEntryProviderTags.OP_BLOCKS);
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
        original.itematic$setIconKey(ItemKeys.CHEST);
        return original;
    }
}
