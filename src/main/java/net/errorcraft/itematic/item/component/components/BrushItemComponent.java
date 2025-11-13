package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public record BrushItemComponent(int brushTicks) implements ItemComponent<BrushItemComponent> {
    public static final Codec<BrushItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("ticks").forGetter(BrushItemComponent::brushTicks)
    ).apply(instance, BrushItemComponent::new));
    private static final BrushItem DUMMY = new BrushItem(new Item.Settings());

    public static BrushItemComponent of(int brushTicks) {
        return new BrushItemComponent(brushTicks);
    }

    @Override
    public ItemComponentType<BrushItemComponent> type() {
        return ItemComponentTypes.BRUSH;
    }

    @Override
    public Codec<BrushItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        PlayerEntity player = context.getPlayer();
        if (player != null && hitResultType(player) == HitResult.Type.BLOCK) {
            player.itematic$startUsingHand(context.getHand(), this.brushTicks);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        DUMMY.itematic$setUsedTicks(usedTicks);
        DUMMY.usageTick(world, user, stack, remainingUseTicks);
    }

    private static HitResult.Type hitResultType(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), user.getBlockInteractionRange())
            .getType();
    }
}
