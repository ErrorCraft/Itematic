package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public record CanPlaceOnFluidsItemComponent(RaycastContext.FluidHandling handler, Vec3i offset) implements ItemComponent {
    public static final Codec<CanPlaceOnFluidsItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StringIdentifiable.createCodec(RaycastContext.FluidHandling::values).fieldOf("handler").forGetter(CanPlaceOnFluidsItemComponent::handler),
        Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(CanPlaceOnFluidsItemComponent::offset)
    ).apply(instance, CanPlaceOnFluidsItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.CAN_PLACE_ON_FLUIDS;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        ItemUsageContext itemUsageContext = new ItemUsageContext(world, user, hand, stack, this.raycast(world, user));
        itemUsageContext.setIgnoresPlacementComponent(true);
        ActionResult result = stack.useOnBlock(itemUsageContext);
        return new TypedActionResult<>(result, stack);
    }

    private BlockHitResult raycast(World world, PlayerEntity user) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.handler);
        return blockHitResult.withBlockPos(blockHitResult.getBlockPos().add(this.offset));
    }
}
