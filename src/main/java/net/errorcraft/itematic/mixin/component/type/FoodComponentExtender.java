package net.errorcraft.itematic.mixin.component.type;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(FoodComponent.class)
public class FoodComponentExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Function<RecordCodecBuilder.Instance<FoodComponent>, ? extends App<RecordCodecBuilder.Mu<FoodComponent>, FoodComponent>> removeUnusedFields(Function<RecordCodecBuilder.Instance<FoodComponent>, ? extends App<RecordCodecBuilder.Mu<FoodComponent>, FoodComponent>> builder) {
        return instance -> instance.group(
            Codecs.NONNEGATIVE_INT.fieldOf("nutrition").forGetter(FoodComponent::nutrition),
            Codec.FLOAT.fieldOf("saturation").forGetter(FoodComponent::saturation),
            Codec.BOOL.optionalFieldOf("can_always_eat", false).forGetter(FoodComponent::canAlwaysEat)
        ).apply(instance, (FoodComponentExtender::create));
    }

    @Unique
    private static FoodComponent create(int nutrition, float saturation, boolean alwaysEdible) {
        return new FoodComponent(nutrition, saturation, alwaysEdible, 1.0f, Optional.empty(), List.of());
    }

    @Mixin(FoodComponent.Builder.class)
    public static class BuilderExtender {
        @Redirect(
            method = "usingConvertsTo",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }
    }
}
