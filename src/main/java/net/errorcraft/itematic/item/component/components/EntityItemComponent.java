package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.mixin.item.DecorationItemAccessor;
import net.errorcraft.itematic.mixin.item.SpawnEggItemAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;

public record EntityItemComponent(EntityInitializer<?> entity, boolean allowItemData) implements ItemComponent<EntityItemComponent> {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityInitializer.CODEC.fieldOf("entity").forGetter(EntityItemComponent::entity),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntityItemComponent::allowItemData)
    ).apply(instance, EntityItemComponent::new));
    private static final MapCodec<EntityType<?>> ENTITY_TYPE_MAP_CODEC = SpawnEggItemAccessor.entityTypeMapCodec();
    private static final Text RANDOM_TEXT = DecorationItemAccessor.randomText();

    @Override
    public ItemComponentType<EntityItemComponent> type() {
        return ItemComponentTypes.ENTITY;
    }

    @Override
    public Codec<EntityItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.entity.type() != EntityType.PAINTING) {
            return;
        }

        RegistryWrapper.WrapperLookup lookup = context.getRegistryLookup();
        if (lookup == null) {
            return;
        }

        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
        if (!entityData.isEmpty()) {
            entityData.get(lookup.getOps(NbtOps.INSTANCE), PaintingEntity.VARIANT_MAP_CODEC).result().ifPresentOrElse(
                variant -> {
                    variant.getKey().ifPresent((key) -> {
                        tooltip.add(Text.translatable(key.getValue().toTranslationKey("painting", "title")).formatted(Formatting.YELLOW));
                        tooltip.add(Text.translatable(key.getValue().toTranslationKey("painting", "author")).formatted(Formatting.GRAY));
                    });
                    tooltip.add(Text.translatable("painting.dimensions", variant.value().width(), variant.value().height()));
                },
                () -> tooltip.add(RANDOM_TEXT)
            );
            return;
        }

        if (type.isCreative()) {
            tooltip.add(RANDOM_TEXT);
        }
    }

    public static EntityItemComponent of(EntityInitializer<?> entity) {
        return of(entity, false);
    }

    public static EntityItemComponent of(EntityInitializer<?> entity, boolean allowItemData) {
        return new EntityItemComponent(entity, allowItemData);
    }

    public static ItemComponent<?>[] from(EntityInitializer<?> entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return from(entity, false, dispenseBehaviors);
    }

    public static ItemComponent<?>[] from(EntityInitializer<?> entity, boolean allowItemData, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            of(entity, allowItemData),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
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
        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
        Optional<EntityInitializer<?>> initializer = entityData.get(ENTITY_TYPE_MAP_CODEC)
            .result()
            .map(SimpleEntityInitializer::new);
        return initializer.orElse(this.entity);
    }
}
