package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.item.placement.FluidPlacer;
import net.errorcraft.itematic.item.placement.Placer;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.util.ActionResultUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BucketItemComponent(Optional<RegistryEntry<Fluid>> fluid, Optional<RegistryEntry<EntityType<?>>> entity, Optional<RegistryEntry<Block>> block, Optional<RegistryEntry<SoundEvent>> emptyingSound) implements ItemComponent {
    public static final Codec<BucketItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.FLUID).optionalFieldOf("fluid").forGetter(BucketItemComponent::fluid),
        RegistryFixedCodec.of(RegistryKeys.ENTITY_TYPE).optionalFieldOf("entity").forGetter(BucketItemComponent::entity),
        RegistryFixedCodec.of(RegistryKeys.BLOCK).optionalFieldOf("block").forGetter(BucketItemComponent::block),
        RegistryFixedCodec.of(RegistryKeys.SOUND_EVENT).optionalFieldOf("emptying_sound_event").forGetter(BucketItemComponent::emptyingSound)
    ).apply(instance, BucketItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.BUCKET;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.getFluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(stack);
        }
        return this.place(world, user, hand, stack, blockHitResult);
    }

    public TypedActionResult<ItemStack> place(World world, @Nullable PlayerEntity user, Hand hand, ItemStack stack, BlockHitResult blockHitResult) {
        TypedActionResult<ItemStack> result = TypedActionResult.pass(stack);
        if (this.fluid.isPresent()) {
            FluidPlacer fluidPlacer = FluidPlacer.of(stack, world, blockHitResult, user, this.fluid.get(), this.emptyingSound.orElse(null));
            result = place(fluidPlacer, result);
        }
        if (this.block.isPresent() && result.getResult() != ActionResult.FAIL) {
            ItemUsageContext context = new ItemUsageContext(world, user, hand, stack, blockHitResult);
            BlockPlacer blockPlacer = BlockPlacer.of(context, this.block.get());
            result = place(blockPlacer, result);
        }
        if (this.entity.isPresent() && !world.isClient() && result.getResult() != ActionResult.FAIL) {
            EntityPlacer entityPlacer = EntityPlacer.bucket(stack, world, blockHitResult, user, this.entity.get());
            result = place(entityPlacer, result);
        }

        if (user == null) {
            return result;
        }
        ActionResult actionResult = result.getResult();
        if (!actionResult.isAccepted()) {
            return result;
        }
        return new TypedActionResult<>(actionResult, ItemUsage.exchangeStack(stack, user, result.getValue()));
    }

    private RaycastContext.FluidHandling getFluidHandling() {
        if (this.fluid.isEmpty()) {
            return RaycastContext.FluidHandling.NONE;
        }
        if (this.fluid.get().matchesKey(FluidKeys.EMPTY)) {
            return RaycastContext.FluidHandling.SOURCE_ONLY;
        }
        return RaycastContext.FluidHandling.NONE;
    }

    private static TypedActionResult<ItemStack> place(Placer placer, TypedActionResult<ItemStack> currentResult) {
        TypedActionResult<ItemStack> result = placer.place();
        return ActionResultUtil.max(result, currentResult.getResult());
    }

    public static void initializeBucketEntity(Entity entity, ItemStack stack) {
        if (entity instanceof Bucketable bucketable) {
            bucketable.copyDataFromNbt(stack.getOrCreateNbt());
            bucketable.setFromBucket(true);
        }
    }

    public static BucketItemComponent fluid(RegistryEntry<Fluid> fluid) {
        return new BucketItemComponent(Optional.of(fluid), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static BucketItemComponent fluid(RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> emptyingSound) {
        return new BucketItemComponent(Optional.of(fluid), Optional.empty(), Optional.empty(), Optional.of(emptyingSound));
    }

    public static BucketItemComponent entity(RegistryEntry<Fluid> fluid, RegistryEntry<EntityType<?>> entity, RegistryEntry<SoundEvent> emptyingSound) {
        return new BucketItemComponent(Optional.of(fluid), Optional.of(entity), Optional.empty(), Optional.of(emptyingSound));
    }

    public static BucketItemComponent block(RegistryEntry<Block> block, RegistryEntry<SoundEvent> emptyingSound) {
        return new BucketItemComponent(Optional.empty(), Optional.empty(), Optional.of(block), Optional.of(emptyingSound));
    }
}
