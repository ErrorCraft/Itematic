package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.component.type.ToolComponentAccess;
import net.errorcraft.itematic.access.component.type.ToolComponentRuleAccess;
import net.errorcraft.itematic.component.type.ToolComponentRuleExtraFields;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntryList;
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

@Mixin(ToolComponent.class)
public class ToolComponentExtender implements ToolComponentAccess {
    @Shadow
    @Final
    private List<ToolComponent.Rule> rules;

    @Shadow
    @Final
    private float defaultMiningSpeed;

    @Override
    public float itematic$getSpeed(ItemStack stack, BlockState state) {
        for (ToolComponent.Rule rule : this.rules) {
            if (rule.speed().isPresent() && rule.itematic$matches(stack, state)) {
                return rule.speed().get();
            }
        }

        return this.defaultMiningSpeed;
    }

    @Override
    public boolean itematic$isCorrectForDrops(ItemStack stack, BlockState state) {
        for (ToolComponent.Rule rule : this.rules) {
            if (rule.correctForDrops().isPresent() && rule.itematic$matches(stack, state)) {
                return rule.correctForDrops().get();
            }
        }

        return false;
    }

    @Mixin(ToolComponent.Rule.class)
    public static class RuleExtender implements ToolComponentRuleAccess {
        @Shadow
        @Final
        RegistryEntryList<Block> blocks;

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
        private static MapCodec<Optional<RegistryEntryList<Block>>> fieldOfBlocksMakeFieldOptional(Codec<RegistryEntryList<Block>> instance, String name) {
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
        private static Function<ToolComponent.Rule, Optional<RegistryEntryList<Block>>> forGetterBlocksFieldReturnOptional(Function<ToolComponent.Rule, RegistryEntryList<Block>> getter) {
            return tool -> Optional.ofNullable(getter.apply(tool));
        }

        @Redirect(
            method = "method_58430",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/dynamic/Codecs;POSITIVE_FLOAT:Lcom/mojang/serialization/Codec;",
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
        private static Function3<Optional<RegistryEntryList<Block>>, Optional<Float>, Optional<Boolean>, ToolComponent.Rule> applyCodecUseOptional(Function3<RegistryEntryList<Block>, Optional<Float>, Optional<Boolean>, ToolComponent.Rule> instance) {
            return RuleExtender::create;
        }

        @ModifyArg(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;"
            )
        )
        private static Function3<Optional<RegistryEntryList<Block>>, Optional<Float>, Optional<Boolean>, ToolComponent.Rule> applyPacketCodecUseOptional(Function3<RegistryEntryList<Block>, Optional<Float>, Optional<Boolean>, ToolComponent.Rule> to) {
            return RuleExtender::create;
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/PacketCodecs;registryEntryList(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;"
            )
        )
        private static PacketCodec<RegistryByteBuf, Optional<RegistryEntryList<Block>>> makeBlocksFieldOptional(PacketCodec<RegistryByteBuf, RegistryEntryList<Block>> original) {
            return original.collect(PacketCodecs::optional);
        }

        @ModifyArg(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;",
                ordinal = 0
            ),
            index = 1
        )
        private static Function<ToolComponent.Rule, Optional<RegistryEntryList<Block>>> getBlocksReturnOptional(Function<ToolComponent.Rule, RegistryEntryList<Block>> from1) {
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
        private static Codec<ToolComponent.Rule> createCodecAddExtraFields(Function<RecordCodecBuilder.Instance<ToolComponent.Rule>, ? extends App<RecordCodecBuilder.Mu<ToolComponent.Rule>, ToolComponent.Rule>> builder) {
            MapCodec<ToolComponent.Rule> mapCodec = RecordCodecBuilder.mapCodec(builder);
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
                target = "Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;"
            )
        )
        private static PacketCodec<RegistryByteBuf, ToolComponent.Rule> createPacketCodecAddExtraFields(PacketCodec<RegistryByteBuf, ToolComponent.Rule> original) {
            return PacketCodec.tuple(
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
            if (this.blocks != null && !state.isIn(this.blocks)) {
                return false;
            }

            return this.extraFields.item()
                .map(item -> item.test(stack))
                .orElse(true);
        }

        @Unique
        private static ToolComponent.Rule create(Optional<RegistryEntryList<Block>> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
            return new ToolComponent.Rule(blocks.orElse(null), speed, correctForDrops);
        }
    }
}
