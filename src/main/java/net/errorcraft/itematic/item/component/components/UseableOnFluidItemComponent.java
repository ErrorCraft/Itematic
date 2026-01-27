package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public record UseableOnFluidItemComponent() implements ItemComponent<UseableOnFluidItemComponent> {
    public static final UseableOnFluidItemComponent INSTANCE = new UseableOnFluidItemComponent();
    public static final Codec<UseableOnFluidItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<UseableOnFluidItemComponent> type() {
        return ItemComponentTypes.USEABLE_ON_FLUID;
    }

    @Override
    public Codec<UseableOnFluidItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ItemUsageContext itemUsageContext = new ItemUsageContext(world, user, hand, stack, blockHitResult);
        return stack.useOnBlock(itemUsageContext); // todo fix
    }
}
