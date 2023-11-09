package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.SaddleItemComponent;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Saddleable;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Optional;

public class SaddleItemComponentDispenserBehavior extends ItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Optional<SaddleItemComponent> optionalComponent = stack.itematic$getComponent(ItemComponentTypes.SADDLE);
        if (optionalComponent.isEmpty()) {
            return super.dispenseSilently(pointer, stack);
        }
        SaddleItemComponent component = optionalComponent.get();
        Direction side = pointer.state().get(DispenserBlock.FACING);
        BlockPos position = pointer.pos().offset(side);
        List<LivingEntity> targets = pointer.world().getEntitiesByClass(LivingEntity.class, new Box(position), entity -> entity instanceof Saddleable);
        for (LivingEntity target : targets) {
            if (component.trySaddle(target, pointer.world(), stack, SoundCategory.BLOCKS)) {
                return stack;
            }
        }
        return super.dispenseSilently(pointer, stack);
    }
}
