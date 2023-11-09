package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.item.ItemStackConsumer;
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
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BucketItemComponent(Optional<RegistryEntry<Fluid>> fluid, Optional<EntityInitializer<?>> entity, Optional<RegistryEntry<Block>> block, Optional<RegistryEntry<SoundEvent>> emptyingSound) implements ItemComponent {
    public static final Codec<BucketItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.FLUID).optionalFieldOf("fluid").forGetter(BucketItemComponent::fluid),
        EntityInitializer.CODEC.optionalFieldOf("entity").forGetter(BucketItemComponent::entity),
        RegistryFixedCodec.of(RegistryKeys.BLOCK).optionalFieldOf("block").forGetter(BucketItemComponent::block),
        RegistryFixedCodec.of(RegistryKeys.SOUND_EVENT).optionalFieldOf("emptying_sound_event").forGetter(BucketItemComponent::emptyingSound)
    ).apply(instance, BucketItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.BUCKET;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.getFluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ActionResult.PASS;
        }
        return this.place(world, user, hand, stack, resultStackConsumer, blockHitResult);
    }

    public ActionResult place(World world, @Nullable PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer, BlockHitResult blockHitResult) {
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionResult result = ActionResult.PASS;
        if (this.fluid.isPresent()) {
            FluidPlacer fluidPlacer = FluidPlacer.of(stack, stackReference::set, world, blockHitResult, user, this.fluid.get(), this.emptyingSound.orElse(null));
            result = place(fluidPlacer, result);
        }
        if (this.block.isPresent() && result != ActionResult.FAIL) {
            ItemUsageContext context = new ItemUsageContext(world, user, hand, stack, blockHitResult);
            BlockPlacer blockPlacer = BlockPlacer.of(context, stackReference::set, this.block.get(), false, true);
            result = place(blockPlacer, result);
        }
        if (this.entity.isPresent() && !world.isClient() && result != ActionResult.FAIL) {
            EntityPlacer entityPlacer = EntityPlacer.bucket(stack, stackReference::set, world, blockHitResult, user, this.entity.get(), hand);
            result = place(entityPlacer, result);
        }
        resultStackConsumer.set(getResultStack(user, stack, stackReference.get()));
        return result;
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

    private static ActionResult place(Placer placer, ActionResult currentResult) {
        ActionResult result = placer.place();
        return ActionResultUtil.max(currentResult, result);
    }

    private static ItemStack getResultStack(@Nullable PlayerEntity player, ItemStack currentStack, ItemStack newStack) {
        if (player == null) {
            return newStack;
        }
        return ItemUsage.exchangeStack(currentStack, player, newStack);
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
        return new BucketItemComponent(Optional.of(fluid), Optional.of(new SimpleEntityInitializer<>(entity.value())), Optional.empty(), Optional.of(emptyingSound));
    }

    public static BucketItemComponent block(RegistryEntry<Block> block, RegistryEntry<SoundEvent> emptyingSound) {
        return new BucketItemComponent(Optional.empty(), Optional.empty(), Optional.of(block), Optional.of(emptyingSound));
    }
}
