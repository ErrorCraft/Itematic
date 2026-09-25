package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

public record EquipmentItemBehavior(Equippable equippable) implements ItemBehavior<EquipmentItemBehavior> {
    public static final Codec<EquipmentItemBehavior> CODEC = Equippable.CODEC.xmap(EquipmentItemBehavior::new, EquipmentItemBehavior::equippable);

    public static EquipmentItemBehavior of(Equippable equippable) {
        return new EquipmentItemBehavior(equippable);
    }

    @Override
    public ItemBehaviorType<EquipmentItemBehavior> type() {
        return ItemBehaviorType.EQUIPMENT;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) {
            return ItemResult.PASS;
        }

        if (!equippable.swappable()) {
            return ItemResult.PASS;
        }

        InteractionResult result = equippable.swapWithEquipmentSlot(stack, user);
        if (result == InteractionResult.FAIL) {
            return ItemResult.PASS;
        }

        if (result instanceof InteractionResult.Success success) {
            this.tryExchangeResultStack(stackExchanger, success.heldItemTransformedTo());
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextKeys.HAND, hand)
                .build();
            stack.itematic$invokeEvent(ItemEvent.EQUIP_ITEM, context);
        }

        return result.consumesAction() ? ItemResult.SUCCEED : ItemResult.PASS;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.EQUIPPABLE, this.equippable);
    }

    private void tryExchangeResultStack(ItemStackExchanger stackExchanger, @Nullable ItemStack stack) {
        if (stack != null) {
            stackExchanger.exchange(stack);
        }
    }
}
