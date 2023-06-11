package net.errorcraft.itematic.mixin.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityExtender extends BlockEntity {
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
    private int startPlayingGetRawIdUseDynamicRegistry(Item item) {
        return this.world.getRegistryManager().get(RegistryKeys.ITEM).getRawId(item);
    }
}
