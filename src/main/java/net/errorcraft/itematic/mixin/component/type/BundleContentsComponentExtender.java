package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentAccess;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentBuilderAccess;
import net.errorcraft.itematic.component.type.BundleContentsComponentExtraFields;
import net.errorcraft.itematic.component.type.BundleContentsComponentUtil;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Mixin(BundleContentsComponent.class)
public class BundleContentsComponentExtender implements BundleContentsComponentAccess {
    @Shadow
    @Final
    List<ItemStack> stacks;

    @Shadow
    @Final
    @Mutable
    Fraction occupancy;

    @Unique
    private BundleContentsComponentExtraFields extraFields;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<BundleContentsComponent> createCodecAddExtraFields(Codec<List<ItemStack>> instance, Function<List<ItemStack>, BundleContentsComponent> to, Function<BundleContentsComponent, List<ItemStack>> from) {
        Codec<BundleContentsComponent> fullCodec = RecordCodecBuilder.create(i -> i.group(
            BundleContentsComponentExtraFields.CODEC.optionalFieldOf("rules", BundleContentsComponentExtraFields.DEFAULT).forGetter(BundleContentsComponent::itematic$extraFields),
            instance.fieldOf("items").forGetter(BundleContentsComponent::itematic$stacks)
        ).apply(i, BundleContentsComponentUtil::create));
        return Codec.withAlternative(
            fullCodec,
            instance.xmap(BundleContentsComponentExtender::create, BundleContentsComponent::itematic$stacks)
        );
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;"
        )
    )
    private static PacketCodec<RegistryByteBuf, BundleContentsComponent> createPacketCodecAddExtraFields(PacketCodec<RegistryByteBuf, List<ItemStack>> instance, Function<List<ItemStack>, BundleContentsComponent> to, Function<BundleContentsComponent, List<ItemStack>> from) {
        return PacketCodec.tuple(
            BundleContentsComponentExtraFields.PACKET_CODEC, BundleContentsComponent::itematic$extraFields,
            instance, BundleContentsComponent::itematic$stacks,
            BundleContentsComponentUtil::create
        );
    }

    @Redirect(
        method = "<init>(Ljava/util/List;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/BundleContentsComponent;calculateOccupancy(Ljava/util/List;)Lorg/apache/commons/lang3/math/Fraction;"
        )
    )
    private static Fraction doNotCalculateOccupancy(List<ItemStack> stacks) {
        return Fraction.ZERO;
    }

    @ModifyReturnValue(
        method = "hashCode",
        at = @At("RETURN"),
        remap = false
    )
    private int calculateHashCodeFromExtraFields(int original) {
        return Objects.hash(original, this.extraFields);
    }

    @ModifyReturnValue(
        method = "equals",
        at = @At(
            value = "RETURN",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lorg/apache/commons/lang3/math/Fraction;equals(Ljava/lang/Object;)Z"
            )
        ),
        remap = false
    )
    private boolean checkExtraFields(boolean original, @Local(argsOnly = true) Object that) {
        return original && Objects.equals(this.extraFields, ((BundleContentsComponentExtender) that).extraFields);
    }

    @Override
    public List<ItemStack> itematic$stacks() {
        return this.stacks;
    }

    @Override
    public BundleContentsComponentExtraFields itematic$extraFields() {
        return this.extraFields;
    }

    @Override
    public void itematic$setExtraFields(BundleContentsComponentExtraFields extraFields) {
        this.extraFields = extraFields;
    }

    @Override
    public void itematic$calculateOccupancy() {
        Fraction occupancy = Fraction.ZERO;
        var rules = this.extraFields.rules();
        for (ItemStack stack : this.stacks) {
            occupancy = occupancy.add(rules.occupancy(stack)
                .multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
        }
        this.occupancy = occupancy;
    }

    @Unique
    private static BundleContentsComponent create(List<ItemStack> stacks) {
        return BundleContentsComponentUtil.create(BundleContentsComponentExtraFields.DEFAULT, stacks);
    }

    @Mixin(BundleContentsComponent.Builder.class)
    public static class BuilderExtender implements BundleContentsComponentBuilderAccess {
        @Shadow
        @Final
        private List<ItemStack> stacks;

        @Unique
        private Fraction capacity;

        @Unique
        private ItemHolderRules rules;

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setRules(BundleContentsComponent base, CallbackInfo info) {
            this.rules = base.itematic$extraFields().rules();
        }

        @Redirect(
            method = "getMaxAllowed",
            at = @At(
                value = "FIELD",
                target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
                opcode = Opcodes.GETSTATIC,
                remap = false
            )
        )
        private Fraction getCapacity() {
            return this.capacity;
        }

        @Inject(
            method = "getMaxAllowed",
            at = @At("HEAD"),
            cancellable = true
        )
        private void checkCanOccupy(ItemStack stack, CallbackInfoReturnable<Integer> info) {
            if (!this.rules.canOccupy(stack)) {
                info.setReturnValue(0);
            }
        }

        @Redirect(
            method = { "getMaxAllowed", "add(Lnet/minecraft/item/ItemStack;)I", "removeFirst" },
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/BundleContentsComponent;getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"
            )
        )
        private Fraction calculateFromDataComponent(ItemStack stack) {
            return this.rules.occupancy(stack);
        }

        @Redirect(
            method = "add(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/Item;canBeNested()Z"
            )
        )
        private boolean checkFromDataComponent(Item instance, ItemStack stack) {
            return this.rules.canOccupy(stack);
        }

        @Inject(
            method = "add(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;addInternal(Lnet/minecraft/item/ItemStack;)I"
            ),
            cancellable = true
        )
        private void splitOverMultipleItemStacks(ItemStack stack, CallbackInfoReturnable<Integer> info, @Local int countToAdd) {
            info.setReturnValue(countToAdd);
            for (ItemStack heldStack : this.stacks) {
                if (!ItemStack.areItemsAndComponentsEqual(heldStack, stack)) {
                    continue;
                }
                int count = Math.min(heldStack.getMaxCount() - heldStack.getCount(), countToAdd);
                heldStack.increment(count);
                stack.decrement(count);
                countToAdd -= count;
                if (countToAdd <= 0) {
                    return;
                }
            }
            this.stacks.addFirst(stack.split(countToAdd));
        }

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private BundleContentsComponent setExtraFields(BundleContentsComponent original) {
            original.itematic$setExtraFields(new BundleContentsComponentExtraFields(this.rules));
            return original;
        }

        @Override
        public void itematic$setCapacity(Fraction capacity) {
            this.capacity = capacity;
        }
    }
}
