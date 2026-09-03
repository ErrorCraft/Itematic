package net.errorcraft.itematic.core.registries;

import net.errorcraft.itematic.mixin.core.registries.BuiltInRegistriesAccessor;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.world.level.modification.WorldModificationType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

public class ItematicBuiltInRegistries {
    public static final Registry<ItemBehaviorType<?>> ITEM_BEHAVIOR_TYPE = register(
        ItematicRegistries.ITEM_BEHAVIOR_TYPE,
        _ -> ItemBehaviorType.USEABLE
    );
    public static final Registry<ItemEvent> ITEM_EVENT = register(
        ItematicRegistries.ITEM_EVENT,
        _ -> ItemEvent.USE
    );
    public static final Registry<ActionType<?>> ACTION_TYPE = register(
        ItematicRegistries.ACTION_TYPE,
        _ -> ActionType.MODIFY_ITEM
    );
    public static final Registry<SequenceHandlerType<?>> SEQUENCE_HANDLER_TYPE = register(
        ItematicRegistries.SEQUENCE_HANDLER_TYPE,
        _ -> SequenceHandlerType.UNCHECKED
    );
    public static final Registry<SmithingTemplate> SMITHING_TEMPLATE = register(
        ItematicRegistries.SMITHING_TEMPLATE,
        _ -> SmithingTemplates.TRIM_PATTERN
    );
    public static final Registry<BlockPickerType<?>> BLOCK_PICKER_TYPE = register(
        ItematicRegistries.BLOCK_PICKER_TYPE,
        _ -> BlockPickerType.SIMPLE
    );
    public static final Registry<UseDurationProviderType<?>> USE_DURATION_PROVIDER_TYPE = register(
        ItematicRegistries.USE_DURATION_PROVIDER_TYPE,
        _ -> UseDurationProviderType.CONSTANT
    );
    public static final Registry<ItemHolderRuleType<?>> ITEM_HOLDER_RULE_TYPE = register(
        ItematicRegistries.ITEM_HOLDER_RULE_TYPE,
        _ -> ItemHolderRuleType.REJECT
    );
    public static final Registry<ShooterMethodType<?>> SHOOTER_METHOD_TYPE = register(
        ItematicRegistries.SHOOTER_METHOD_TYPE,
        _ -> ShooterMethodType.DIRECT
    );
    public static final Registry<WorldModificationType<?>> WORLD_MODIFICATION_TYPE = register(
        ItematicRegistries.WORLD_MODIFICATION_TYPE,
        _ -> WorldModificationType.DRAIN_FLUID
    );
    public static final Registry<EntitySpawnRuleType<?>> ENTITY_SPAWN_RULE_TYPE = register(
        ItematicRegistries.ENTITY_SPAWN_RULE_TYPE,
        _ -> EntitySpawnRuleType.DISCARD
    );
    public static final Registry<DataComponentType<?>> MELEE_WEAPON_COMPONENT_TYPE = register(
        ItematicRegistries.MELEE_WEAPON_COMPONENT_TYPE,
        _ -> MeleeWeaponComponents.SMASHING
    );
    public static final Registry<ItemGroupEntryType<?>> ITEM_GROUP_ENTRY_TYPE = register(
        ItematicRegistries.ITEM_GROUP_ENTRY_TYPE,
        _ -> ItemGroupEntryType.ITEM
    );

    private ItematicBuiltInRegistries() {}

    private static <T> Registry<T> register(ResourceKey<? extends Registry<T>> name, BuiltInRegistries.RegistryBootstrap<T> loader) {
        return BuiltInRegistriesAccessor.registerSimple(name, loader);
    }
}
