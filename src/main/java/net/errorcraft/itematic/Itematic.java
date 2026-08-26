package net.errorcraft.itematic;

import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.world.inventory.ItematicMenuTypes;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeBookCategories;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeSerializers;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeTypes;
import net.errorcraft.itematic.world.item.crafting.display.ItematicRecipeDisplays;
import net.errorcraft.itematic.world.item.crafting.display.ItematicSlotDisplays;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.world.level.modification.WorldModificationType;
import net.errorcraft.itematic.world.level.storage.loot.functions.ItematicItemModifiers;
import net.errorcraft.itematic.world.level.storage.loot.predicates.ItematicPredicates;
import net.fabricmc.api.ModInitializer;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemBehaviorType.init();
        ItemEvent.init();
        ActionType.init();
        SequenceHandlerType.init();
        ItematicPredicates.init();
        ItematicContextKeys.init();
        SmithingTemplates.init();
        BlockPickerType.init();
        ItematicItemModifiers.init();
        ItematicDataComponents.init();
        UseDurationProviderType.init();
        ItemHolderRuleType.init();
        ShooterMethodType.init();
        ItematicRecipeTypes.init();
        ItematicRecipeSerializers.init();
        ItematicRecipeBookCategories.init();
        ItematicRecipeDisplays.init();
        ItematicSlotDisplays.init();
        WorldModificationType.init();
        EntitySpawnRuleType.init();
        MeleeWeaponComponents.init();
        ItematicMenuTypes.init();
    }
}
