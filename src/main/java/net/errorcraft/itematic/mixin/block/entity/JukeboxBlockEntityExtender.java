package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityExtender extends BlockEntity {
    @Shadow
    public abstract ItemStack getStack();

    public JukeboxBlockEntityExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = "startPlaying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getRawId(Lnet/minecraft/item/Item;)I"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private int getRawIdUseDynamicRegistry(Item item) {
        return this.world.getRegistryManager().get(RegistryKeys.ITEM).getRawId(item);
    }

    @ModifyConstant(
        method = "tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
        constant = @Constant(
            classValue = MusicDiscItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfMusicDiscItemUseItemComponentCheck(Object reference, Class<MusicDiscItem> clazz) {
        return this.getStack().itematic$hasComponent(ItemComponentTypes.RECORD);
    }

    @ModifyVariable(
        method = "tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToMusicDiscItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "isSongFinished",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MusicDiscItem;getSongLengthInTicks()I"
        )
    )
    private int getSongLengthInTicksUseItemComponent(MusicDiscItem instance) {
        return this.getStack()
            .itematic$getComponent(ItemComponentTypes.RECORD)
            .orElseThrow()
            .duration();
    }
}
