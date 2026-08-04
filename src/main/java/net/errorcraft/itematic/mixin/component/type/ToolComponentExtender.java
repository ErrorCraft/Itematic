package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.component.type.ToolComponentAccess;
import net.errorcraft.itematic.component.type.ToolComponentRuleExtraFields;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(Tool.class)
public class ToolComponentExtender implements ToolComponentAccess {
    @Shadow
    @Final
    private List<Tool.Rule> rules;

    @Shadow
    @Final
    private float defaultMiningSpeed;

    @Override
    public float itematic$getSpeed(ItemStack stack, BlockState state) {
        for (Tool.Rule rule : this.rules) {
            if (rule.speed().isPresent() && rule.itematic$matches(stack, state)) {
                return rule.speed().get();
            }
        }

        return this.defaultMiningSpeed;
    }

    @Override
    public boolean itematic$isCorrectForDrops(ItemStack stack, BlockState state) {
        for (Tool.Rule rule : this.rules) {
            if (rule.correctForDrops().isPresent() && rule.itematic$matches(stack, state)) {
                return rule.correctForDrops().get();
            }
        }

        return false;
    }

    @Mixin(Tool.Rule.class)
    public static class RuleExtender implements RuleAccess {
        @Shadow
        @Final
        HolderSet<Block> blocks;

        @Unique
        private ToolComponentRuleExtraFields extraFields = new ToolComponentRuleExtraFields(Optional.empty());

        @Redirect(
            method = "method_58430",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/Codec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;",
                remap = false
            )
        )
        private static MapCodec<Optional<HolderSet<Block>>> fieldOfBlocksMakeFieldOptional(Codec<HolderSet<Block>> instance, String name) {
            return instance.optionalFieldOf(name);
        }

        @ModifyArg(
            method = "method_58430",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
                ordinal = 0,
                remap = false
            ),
            slice = @Slice(
                from = @At(
                    value = "CONSTANT",
                    args = "stringValue=blocks"
                )
            )
        )
        private static Function<Tool.Rule, Optional<HolderSet<Block>>> forGetterBlocksFieldReturnOptional(Function<Tool.Rule, HolderSet<Block>> getter) {
            return tool -> Optional.ofNullable(getter.apply(tool));
        }

        @Redirect(
            method = "method_58430",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/ExtraCodecs;POSITIVE_FLOAT:Lcom/mojang/serialization/Codec;",
                opcode = Opcodes.GETSTATIC
            )
        )
        private static Codec<Float> speedFieldAllowZero() {
            return ItematicCodecs.NON_NEGATIVE_FLOAT;
        }

        @ModifyArg(
            method = "method_58430",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/datafixers/Products$P3;apply(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function3;)Lcom/mojang/datafixers/kinds/App;",
                remap = false
            )
        )
        private static Function3<Optional<HolderSet<Block>>, Optional<Float>, Optional<Boolean>, Tool.Rule> applyCodecUseOptional(Function3<HolderSet<Block>, Optional<Float>, Optional<Boolean>, Tool.Rule> instance) {
            return RuleExtender::create;
        }

        @ModifyArg(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/StreamCodec;"
            )
        )
        private static Function3<Optional<HolderSet<Block>>, Optional<Float>, Optional<Boolean>, Tool.Rule> applyPacketCodecUseOptional(Function3<HolderSet<Block>, Optional<Float>, Optional<Boolean>, Tool.Rule> to) {
            return RuleExtender::create;
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/ByteBufCodecs;holderSet(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/network/codec/StreamCodec;"
            )
        )
        private static StreamCodec<RegistryFriendlyByteBuf, Optional<HolderSet<Block>>> makeBlocksFieldOptional(StreamCodec<RegistryFriendlyByteBuf, HolderSet<Block>> original) {
            return original.apply(ByteBufCodecs::optional);
        }

        @ModifyArg(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/StreamCodec;",
                ordinal = 0
            ),
            index = 1
        )
        private static Function<Tool.Rule, Optional<HolderSet<Block>>> getBlocksReturnOptional(Function<Tool.Rule, HolderSet<Block>> from1) {
            return rule -> Optional.ofNullable(from1.apply(rule));
        }

        @Redirect(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
                remap = false
            )
        )
        private static Codec<Tool.Rule> createCodecAddExtraFields(Function<RecordCodecBuilder.Instance<Tool.Rule>, ? extends App<RecordCodecBuilder.Mu<Tool.Rule>, Tool.Rule>> builder) {
            MapCodec<Tool.Rule> mapCodec = RecordCodecBuilder.mapCodec(builder);
            return mapCodec.dependent(ToolComponentRuleExtraFields.CODEC, rule -> Pair.of(
                ((RuleExtender)(Object) rule).extraFields,
                ToolComponentRuleExtraFields.CODEC
            ), (rule, extraFields) -> {
                ((RuleExtender)(Object) rule).extraFields = extraFields;
                return rule;
            }).codec();
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/StreamCodec;"
            )
        )
        private static StreamCodec<RegistryFriendlyByteBuf, Tool.Rule> createPacketCodecAddExtraFields(StreamCodec<RegistryFriendlyByteBuf, Tool.Rule> original) {
            return StreamCodec.composite(
                original, Function.identity(),
                ToolComponentRuleExtraFields.PACKET_CODEC, rule -> ((RuleExtender)(Object) rule).extraFields,
                (rule, extraFields) -> {
                    ((RuleExtender)(Object) rule).extraFields = extraFields;
                    return rule;
                }
            );
        }

        @Override
        public boolean itematic$matches(ItemStack stack, BlockState state) {
            if (this.blocks != null && !state.is(this.blocks)) {
                return false;
            }

            return this.extraFields.item()
                .map(item -> item.test(stack))
                .orElse(true);
        }

        @Unique
        private static Tool.Rule create(Optional<HolderSet<Block>> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
            return new Tool.Rule(blocks.orElse(null), speed, correctForDrops);
        }
    }
}
