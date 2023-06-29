package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.StemBlock;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksExtender {
    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "NEW",
            target = "net/minecraft/block/AttachedStemBlock",
            ordinal = 0
        )
    )
    private static AttachedStemBlock staticNewAttachedStemBlockForAttachedPumpkinStemSetPickBlockItemKey(AttachedStemBlock original) {
        original.setPickBlockItemKey(ItemKeys.PUMPKIN_SEEDS);
        return original;
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "NEW",
            target = "net/minecraft/block/AttachedStemBlock",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;PUMPKIN:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static AttachedStemBlock staticNewAttachedStemBlockForAttachedMelonStemSetPickBlockItemKey(AttachedStemBlock original) {
        original.setPickBlockItemKey(ItemKeys.MELON_SEEDS);
        return original;
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "NEW",
            target = "net/minecraft/block/StemBlock",
            ordinal = 0
        )
    )
    private static StemBlock staticNewStemBlockForPumpkinStemSetPickBlockItemKey(StemBlock original) {
        original.setPickBlockItemKey(ItemKeys.PUMPKIN_SEEDS);
        return original;
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "NEW",
            target = "net/minecraft/block/StemBlock",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;PUMPKIN:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC,
                ordinal = 1
            )
        )
    )
    private static StemBlock staticNewStemBlockForMelonStemSetPickBlockItemKey(StemBlock original) {
        original.setPickBlockItemKey(ItemKeys.MELON_SEEDS);
        return original;
    }
}
