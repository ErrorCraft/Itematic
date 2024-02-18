package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ItemAccess {
    default ItemBase itematic$itemBase() {
        return null;
    }
    default void itematic$setItemBase(ItemBase base) {}
    default ItemComponentSet itematic$components() {
        return null;
    }
    default void itematic$setComponents(ItemComponentSet components) {}
    default <T extends ItemComponent<T>> boolean itematic$hasComponent(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent<T>> Optional<T> itematic$getComponent(ItemComponentType<T> type) {
        return Optional.empty();
    }
    default ItemEventMap itematic$events() {
        return null;
    }
    default void itematic$setEvents(ItemEventMap events) {}
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$isItemBarVisible(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        return false;
    }
    default int itematic$itemBarStep(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        return 0;
    }
    default boolean itematic$mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return true;
    }
    default Optional<TooltipData> itematic$tooltipData(ItemStack stack, @Nullable RegistryWrapper.WrapperLookup lookup) {
        return Optional.empty();
    }
}
