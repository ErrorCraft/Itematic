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
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.mixin.item.SpawnEggItemAccessor;
import net.errorcraft.itematic.serialization.SetCodec;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public record EntityItemComponent(EntityInitializer<?> entity, boolean allowItemData, Set<Pass> passes) implements ItemComponent<EntityItemComponent> {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityInitializer.CODEC.fieldOf("entity").forGetter(EntityItemComponent::entity),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntityItemComponent::allowItemData),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(EntityItemComponent::passes)
    ).apply(instance, EntityItemComponent::new));
    private static final MapCodec<EntityType<?>> ENTITY_TYPE_MAP_CODEC = SpawnEggItemAccessor.entityTypeMapCodec();
    private static final Text RANDOM_TEXT = DecorationItemAccessor.randomText();

    public static EntityItemComponent of(EntityInitializer<?> entity) {
        return new EntityItemComponent(entity, false, Pass.DEFAULT_PASSES);
    }

    public static EntityItemComponent of(EntityInitializer<?> entity, boolean allowItemData, Pass... passes) {
        return new EntityItemComponent(entity, allowItemData, Set.of(passes));
    }

    public static ItemComponent<?>[] from(EntityInitializer<?> entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            of(entity),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
        };
    }

    @Override
    public ItemComponentType<EntityItemComponent> type() {
        return ItemComponentTypes.ENTITY;
    }

    @Override
    public Codec<EntityItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.FLUID)) {
            return ActionResult.PASS;
        }

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ActionResult.PASS;
        }

        ItemUsageContext itemUsageContext = new ItemUsageContext(world, user, hand, stack, blockHitResult);
        return this.place(itemUsageContext, resultStackConsumer);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ActionResult.PASS;
        }

        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        return this.place(context, resultStackConsumer);
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

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private ActionResult place(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        return EntityPlacer.spawned(context, context.getStack(), resultStackConsumer, this)
            .place();
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

    public enum Pass implements StringIdentifiable {
        BLOCK("block"),
        FLUID("fluid");

        public static final Set<Pass> DEFAULT_PASSES = Set.of(BLOCK);
        public static final Codec<Pass> CODEC = StringIdentifiable.createCodec(Pass::values);

        private final String name;

        Pass(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
