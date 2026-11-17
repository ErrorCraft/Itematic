package net.errorcraft.itematic.mixin.core.component;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.core.component.BlockTransformerAccess;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.Function;

@Mixin(BlockTransformer.class)
public class BlockTransformerExtender {
    @Inject(
        method = "transformBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/context/UseOnContext;getPlayer()Lnet/minecraft/world/entity/player/Player;"
        )
    )
    private void pushEntitiesUpwards(UseOnContext context, CallbackInfoReturnable<InteractionResult> info, @Local(name = "level") Level level, @Local(name = "pos") BlockPos pos, @Local(name = "transformData") BlockTransformer.BlockTransformData transformData, @Local(name = "updatedShape") BlockState updatedShape) {
        if (transformData.itematic$pushEntitiesUpwards()) {
            Block.pushEntitiesUp(level.getBlockState(pos), updatedShape, level, pos);
        }
    }

    @ModifyExpressionValue(
        method = "transformBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/context/UseOnContext;getItemInHand()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack storeItemInHand(ItemStack original, @Share("itemInHandCopy") LocalRef<ItemStack> itemInHandCopy) {
        itemInHandCopy.set(original.copy());
        return original;
    }

    @Inject(
        method = "transformBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            shift = At.Shift.AFTER
        )
    )
    private void invokeTransformedBlockItemEvent(UseOnContext context, CallbackInfoReturnable<InteractionResult> info, @Local(name = "pos") BlockPos pos, @Local(name = "level") Level level, @Local(name = "player") Player player, @Local(name = "itemInHand") ItemStack itemInHand, @Local(name = "oldBlockState") BlockState oldBlockState, @Share("itemInHandCopy") LocalRef<ItemStack> itemInHandCopy) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ActionContext actionContext = ActionContext.builder(serverLevel)
            .add(LootContextParams.INTERACTING_ENTITY, player)
            .add(ItematicContextKeys.INTERACTED_POSITION, Vec3.atCenterOf(pos))
            .add(LootContextParams.BLOCK_STATE, oldBlockState)
            .addOptional(LootContextParams.BLOCK_ENTITY, serverLevel.getBlockEntity(pos))
            .add(LootContextParams.TOOL, itemInHand)
            .build();
        itemInHandCopy.get().itematic$invokeEvent(ItemEvent.TRANSFORMED_BLOCK, actionContext);
    }

    @Mixin(BlockTransformer.BlockTransformData.class)
    public static class BlockTransformDataExtender implements BlockTransformerAccess.BlockTransformDataAccess {
        @Unique
        private boolean pushEntitiesUpwards = false;

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
            )
        )
        private static Codec<BlockTransformer.BlockTransformData> addExtraMapCodecFields(Codec<BlockTransformer.BlockTransformData> original) {
            return RecordCodecBuilder.create(instance -> instance.group(
                MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
                Codec.BOOL.optionalFieldOf("push_entities_upwards", false).forGetter(BlockTransformer.BlockTransformData::itematic$pushEntitiesUpwards)
            ).apply(instance, BlockTransformDataExtender::setPushEntitiesUpwards));
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function10;)Lnet/minecraft/network/codec/StreamCodec;"
            )
        )
        private static StreamCodec<RegistryFriendlyByteBuf, BlockTransformer.BlockTransformData> addExtraCompositeStreamCodecEntries(StreamCodec<RegistryFriendlyByteBuf, BlockTransformer.BlockTransformData> original) {
            return StreamCodec.composite(
                original, Function.identity(),
                ByteBufCodecs.BOOL, BlockTransformer.BlockTransformData::itematic$pushEntitiesUpwards,
                BlockTransformDataExtender::setPushEntitiesUpwards
            );
        }

        @ModifyReturnValue(
            method = "equals",
            at = @At("TAIL")
        )
        private boolean checkExtraFields(boolean original, Object o) {
            BlockTransformDataExtender that = ((BlockTransformDataExtender) o);
            return original && this.pushEntitiesUpwards == that.pushEntitiesUpwards;
        }

        @ModifyReturnValue(
            method = "hashCode",
            at = @At("TAIL")
        )
        private int hashExtraFields(int original) {
            return Objects.hash(original, this.pushEntitiesUpwards);
        }

        @Override
        public boolean itematic$pushEntitiesUpwards() {
            return this.pushEntitiesUpwards;
        }

        @Override
        public void itematic$setPushEntitiesUpwards(boolean pushEntitiesUpwards) {
            this.pushEntitiesUpwards = pushEntitiesUpwards;
        }

        @Unique
        private static BlockTransformer.BlockTransformData setPushEntitiesUpwards(BlockTransformer.BlockTransformData blockTransformData, boolean pushEntitiesUpwards) {
            blockTransformData.itematic$setPushEntitiesUpwards(pushEntitiesUpwards);
            return blockTransformData;
        }

        @Mixin(BlockTransformer.BlockTransformData.Builder.class)
        public static class BuilderExtender implements BuilderAccess {
            @Unique
            private boolean pushEntitiesUpwards = false;

            @ModifyReturnValue(
                method = "build",
                at = @At("TAIL")
            )
            private BlockTransformer.BlockTransformData hihi(BlockTransformer.BlockTransformData original) {
                original.itematic$setPushEntitiesUpwards(this.pushEntitiesUpwards);
                return original;
            }

            @Override
            public BlockTransformer.BlockTransformData.Builder itematic$pushEntitiesUpwards() {
                this.pushEntitiesUpwards = true;
                return (BlockTransformer.BlockTransformData.Builder)(Object) this;
            }
        }
    }
}
