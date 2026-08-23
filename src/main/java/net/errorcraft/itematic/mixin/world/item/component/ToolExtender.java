package net.errorcraft.itematic.mixin.world.item.component;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.world.item.component.ToolAccess;
import net.errorcraft.itematic.advancements.criterion.ItemPredicates;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
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
public class ToolExtender implements ToolAccess {
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
        @Nullable
        private HolderSet<Block> blocks;

        @Unique
        private Optional<ItemPredicate> item = Optional.empty();

        @Redirect(
            method = "lambda$static$0",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/Codec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;"
            )
        )
        private static MapCodec<Optional<HolderSet<Block>>> makeBlocksFieldOptional(Codec<HolderSet<Block>> instance, String name) {
            return instance.optionalFieldOf(name);
        }

        @ModifyArg(
            method = "lambda$static$0",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "CONSTANT",
                    args = "stringValue=blocks"
                )
            )
        )
        private static Function<Tool.Rule, Optional<HolderSet<Block>>> returnOptionalBlocksForCodec(Function<Tool.Rule, @Nullable HolderSet<Block>> getter) {
            return tool -> Optional.ofNullable(getter.apply(tool));
        }

        @Redirect(
            method = "lambda$static$0",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/ExtraCodecs;POSITIVE_FLOAT:Lcom/mojang/serialization/Codec;",
                opcode = Opcodes.GETSTATIC
            )
        )
        private static Codec<Float> allowZeroForSpeedField() {
            return ItematicCodecs.NON_NEGATIVE_FLOAT;
        }

        @ModifyArg(
            method = "lambda$static$0",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/datafixers/Products$P3;apply(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function3;)Lcom/mojang/datafixers/kinds/App;"
            )
        )
        private static Function3<Optional<HolderSet<Block>>, Optional<Float>, Optional<Boolean>, Tool.Rule> createRuleUseOptionalBlocksForCodec(Function3<HolderSet<Block>, Optional<Float>, Optional<Boolean>, Tool.Rule> instance) {
            return RuleExtender::create;
        }

        @ModifyArg(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/StreamCodec;"
            )
        )
        private static Function3<Optional<HolderSet<Block>>, Optional<Float>, Optional<Boolean>, Tool.Rule> createRuleUseOptionalBlocksForStreamCodec(Function3<HolderSet<Block>, Optional<Float>, Optional<Boolean>, Tool.Rule> to) {
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
        private static Function<Tool.Rule, Optional<HolderSet<Block>>> returnOptionalBlocksForStreamCodec(Function<Tool.Rule, @Nullable HolderSet<Block>> codec1) {
            return rule -> Optional.ofNullable(codec1.apply(rule));
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
            )
        )
        private static Codec<Tool.Rule> addExtraMapCodecFields(Codec<Tool.Rule> original) {
            return RecordCodecBuilder.create(instance -> instance.group(
                MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Tool.Rule::itematic$item)
            ).apply(instance, RuleExtender::setItem));
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/StreamCodec;"
            )
        )
        private static StreamCodec<RegistryFriendlyByteBuf, Tool.Rule> addExtraCompositeStreamCodecEntries(StreamCodec<RegistryFriendlyByteBuf, Tool.Rule> original) {
            return StreamCodec.composite(
                original, Function.identity(),
                ItemPredicates.STREAM_CODEC.apply(ByteBufCodecs::optional), Tool.Rule::itematic$item,
                RuleExtender::setItem
            );
        }

        @Override
        public Optional<ItemPredicate> itematic$item() {
            return this.item;
        }

        @Override
        public void itematic$setItem(Optional<ItemPredicate> item) {
            this.item = item;
        }

        @Override
        public boolean itematic$matches(ItemStack stack, BlockState state) {
            if (this.blocks != null && !state.is(this.blocks)) {
                return false;
            }

            return this.item.map(item -> item.test(stack))
                .orElse(true);
        }

        @Unique
        @SuppressWarnings("DataFlowIssue")
        private static Tool.Rule create(Optional<HolderSet<Block>> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
            return new Tool.Rule(blocks.orElse(null), speed, correctForDrops);
        }

        @Unique
        private static Tool.Rule setItem(Tool.Rule rule, Optional<ItemPredicate> item) {
            rule.itematic$setItem(item);
            return rule;
        }
    }
}
