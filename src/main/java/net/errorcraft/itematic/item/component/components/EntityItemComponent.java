package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.mixin.item.DecorationItemAccessor;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.serialization.SetCodec;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public record EntityItemComponent(RegistryEntry<EntityType<?>> entity, boolean allowItemData, Set<Pass> passes) implements ItemComponent<EntityItemComponent> {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.ENTITY_TYPE.getEntryCodec().fieldOf("entity").forGetter(EntityItemComponent::entity),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntityItemComponent::allowItemData),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(EntityItemComponent::passes)
    ).apply(instance, EntityItemComponent::new));
    private static final Text RANDOM_TEXT = DecorationItemAccessor.randomText();

    public static EntityItemComponent of(RegistryEntry<EntityType<?>> entity) {
        return new EntityItemComponent(entity, false, Pass.DEFAULT_PASSES);
    }

    public static EntityItemComponent of(RegistryEntry<EntityType<?>> entity, boolean allowItemData, Pass... passes) {
        return new EntityItemComponent(entity, allowItemData, Set.of(passes));
    }

    public static ItemComponent<?>[] from(RegistryEntry<EntityType<?>> entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.FLUID)) {
            return ItemResult.PASS;
        }

        if (world.isClient()) {
            return ItemResult.SUCCEED;
        }

        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ItemUsageContext itemUsageContext = new ItemUsageContext(world, user, hand, stack, blockHitResult);
        this.place(itemUsageContext, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        if (context.getWorld().isClient()) {
            return ItemResult.SUCCEED;
        }

        this.place(context, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.entity.value() != EntityType.PAINTING) {
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

    public EntityType<?> entityType(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        if (!this.allowItemData) {
            return this.entity.value();
        }

        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
        EntityType<?> entityType = entityData.getRegistryValueOfId(registries, RegistryKeys.ENTITY_TYPE);
        if (entityType == null) {
            return this.entity.value();
        }

        return entityType;
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private void place(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (!(context.getWorld() instanceof ServerWorld world)) {
            return;
        }

        NewActionContext actionContext = NewActionContext.builder(world)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParameters.THIS_ENTITY, context.getPlayer())
            .addOptional(LootContextParameters.ORIGIN, context.getPlayer(), Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, context.getBlockPos().toCenterPos())
            .add(LootContextParameters.TOOL, context.getStack())
            .add(ItematicContextParameters.HAND, context.getHand())
            .add(ItematicContextParameters.SIDE, context.getSide())
            .build();
        this.place(actionContext);
    }

    public Entity place(NewActionContext context) {
        return EntityPlacer.of(
            this.entityType(
                context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY),
                context.world().getRegistryManager()
            ),
            context,
            true,
            SpawnReason.SPAWN_ITEM_USE,
            null,
            this.allowItemData,
            PositionTarget.INTERACTED_POSITION
        ).place();
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
