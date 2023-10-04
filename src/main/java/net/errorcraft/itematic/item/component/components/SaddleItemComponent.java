package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public record SaddleItemComponent() implements ItemComponent {
    public static final SaddleItemComponent INSTANCE = new SaddleItemComponent();
    public static final Codec<SaddleItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.SADDLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> useOnEntity(PlayerEntity user, LivingEntity target, Hand hand, ItemStack stack) {
        if (this.trySaddle(target, user.getWorld(), stack, SoundCategory.NEUTRAL)) {
            return TypedActionResult.success(stack, user.getWorld().isClient());
        }
        return TypedActionResult.pass(stack);
    }

    public boolean trySaddle(LivingEntity target, World world, ItemStack stack, SoundCategory soundCategory) {
        if (!(target instanceof Saddleable saddleable)) {
            return false;
        }
        if (!target.isAlive() || saddleable.isSaddled() || !saddleable.canBeSaddled()) {
            return false;
        }
        if (!world.isClient()) {
            saddleable.saddle(soundCategory);
            target.getWorld().emitGameEvent(target, GameEvent.EQUIP, target.getPos());
            stack.decrement(1);
        }
        return true;
    }
}
