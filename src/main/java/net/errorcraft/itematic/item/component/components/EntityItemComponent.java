package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviorKeys;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.ActionResult;

import java.util.Optional;

public record EntityItemComponent(EntityInitializer<?> entity, boolean allowItemData) implements ItemComponent<EntityItemComponent> {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityInitializer.CODEC.fieldOf("entity").forGetter(EntityItemComponent::entity),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntityItemComponent::allowItemData)
    ).apply(instance, EntityItemComponent::new));

    public EntityItemComponent(EntityInitializer<?> entity) {
        this(entity, false);
    }

    @Override
    public ItemComponentType<EntityItemComponent> type() {
        return ItemComponentTypes.ENTITY;
    }

    @Override
    public Codec<EntityItemComponent> codec() {
        return CODEC;
    }

    public static ItemComponent<?>[] from(EntityInitializer<?> entity, RegistryEntryLookup<DispenserBehavior> dispenseBehaviors) {
        return from(entity, false, dispenseBehaviors);
    }

    public static ItemComponent<?>[] from(EntityInitializer<?> entity, boolean allowItemData, RegistryEntryLookup<DispenserBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            new EntityItemComponent(entity, allowItemData),
            new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ENTITY))
        };
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        ItemStack stack = context.getStack();
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }
        EntityPlacer placer = EntityPlacer.spawned(context, stack, resultStackConsumer, this);
        return placer.place();
    }

    public EntityInitializer<?> getEntityInitializer(ItemStack stack) {
        if (!this.allowItemData) {
            return this.entity;
        }
        NbtCompound nbt = stack.getSubNbt(EntityType.ENTITY_TAG_KEY);
        if (nbt == null) {
            return this.entity;
        }
        if (!nbt.contains(Entity.ID_KEY)) {
            return this.entity;
        }
        Optional<EntityInitializer<?>> initializer = EntityType.get(nbt.getString(Entity.ID_KEY))
            .map(SimpleEntityInitializer::new);
        return initializer.orElse(this.entity);
    }
}
