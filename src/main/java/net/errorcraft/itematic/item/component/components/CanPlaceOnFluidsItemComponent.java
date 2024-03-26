package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public record CanPlaceOnFluidsItemComponent(RaycastContext.FluidHandling handler, boolean allowOriginalPlacement, Vec3i offset) implements ItemComponent<CanPlaceOnFluidsItemComponent> {
    public static final Codec<CanPlaceOnFluidsItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StringIdentifiable.createCodec(RaycastContext.FluidHandling::values).fieldOf("handler").forGetter(CanPlaceOnFluidsItemComponent::handler),
        Codec.BOOL.fieldOf("allow_original_placement").forGetter(CanPlaceOnFluidsItemComponent::allowOriginalPlacement),
        Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(CanPlaceOnFluidsItemComponent::offset)
    ).apply(instance, CanPlaceOnFluidsItemComponent::new));

    @Override
    public ItemComponentType<CanPlaceOnFluidsItemComponent> type() {
        return ItemComponentTypes.CAN_PLACE_ON_FLUIDS;
    }

    @Override
    public Codec<CanPlaceOnFluidsItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        BlockHitResult blockHitResult = this.raycast(world, user);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ActionResult.PASS;
        }

        ItemUsageContext itemUsageContext = new ItemUsageContext(world, user, hand, stack, blockHitResult);
        itemUsageContext.itematic$setIgnoresPlacementComponent(true);
        return stack.useOnBlock(itemUsageContext);
    }

    public static CanPlaceOnFluidsItemComponent of(RaycastContext.FluidHandling handler, boolean allowOriginalPlacement) {
        return of(handler, allowOriginalPlacement, Vec3i.ZERO);
    }

    public static CanPlaceOnFluidsItemComponent of(RaycastContext.FluidHandling handler, boolean allowOriginalPlacement, Vec3i offset) {
        return new CanPlaceOnFluidsItemComponent(handler, allowOriginalPlacement, offset);
    }

    private BlockHitResult raycast(World world, PlayerEntity user) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.handler);
        return blockHitResult.withBlockPos(blockHitResult.getBlockPos().add(this.offset));
    }
}
