package net.errorcraft.itematic.world.item;

import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionEventMap;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorSet;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public class ItemBuilder {
    private final ResourceKey<Item> item;
    private final BiConsumer<ResourceKey<Item>, Item> bootstrapper;
    private final ItemDisplay.Builder display = ItemDisplay.builder();
    private final ItemAttributeModifiers.Builder attributeModifiers = ItemAttributeModifiers.builder();
    private final ItemBehaviorSet.Builder behavior = ItemBehaviorSet.builder();
    private final ActionEventMap.Builder<ItemEvent> events = ItemEvent.mapBuilder();

    private ItemBuilder(ResourceKey<Item> item, BiConsumer<ResourceKey<Item>, Item> bootstrapper) {
        this.item = item;
        this.bootstrapper = bootstrapper;
    }

    public static ItemBuilder create(ResourceKey<Item> item, BootstrapContext<Item> registerable) {
        return new ItemBuilder(item, registerable::register);
    }

    public void register() {
        this.bootstrapper.accept(
            this.item,
            Items.create(
                this.display.build(this.item),
                this.attributeModifiers.build(),
                this.behavior.build(),
                this.events.build()
            )
        );
    }

    public ItemBuilder display(UnaryOperator<ItemDisplay.Builder> display) {
        display.apply(this.display);
        return this;
    }

    public ItemBuilder attributeModifiers(UnaryOperator<ItemAttributeModifiers.Builder> attributeModifiers) {
        attributeModifiers.apply(this.attributeModifiers);
        return this;
    }

    public ItemBuilder behavior(boolean condition, ItemBehavior<?> behavior) {
        if (condition) {
            this.behavior.add(behavior);
        }

        return this;
    }

    public ItemBuilder behavior(ItemBehavior<?> behavior) {
        this.behavior.add(behavior);
        return this;
    }

    public ItemBuilder event(ItemEvent event, ActionEntry action) {
        this.events.add(event, action);
        return this;
    }

    public ItemBuilder event(ItemEvent event, Holder<ActionEntry> action) {
        this.events.add(event, action);
        return this;
    }

    public ItemBuilder cancellableEvent(ItemEvent event, ActionEntry action) {
        this.events.addCancellable(event, action);
        return this;
    }
}
