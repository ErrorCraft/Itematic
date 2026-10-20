package net.errorcraft.itematic.mixin.world.item.enchantment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.world.item.enchantment.EnchantableAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Function;

@Mixin(Enchantable.class)
public class EnchantableExtender implements EnchantableAccess {
    @Unique
    private Optional<Holder<Item>> transformsInto = Optional.empty();

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<Enchantable> addExtraMapCodecFields(Codec<Enchantable> original) {
        return RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
            Item.CODEC.optionalFieldOf("transforms_into").forGetter(Enchantable::itematic$transformsInto)
        ).apply(instance, EnchantableExtender::setTransformsInto));
    }

    @Override
    public Optional<Holder<Item>> itematic$transformsInto() {
        return this.transformsInto;
    }

    @Override
    public void itematic$setTransformsInto(Optional<Holder<Item>> transformsInto) {
        this.transformsInto = transformsInto;
    }

    @Unique
    private static Enchantable setTransformsInto(Enchantable enchantable, Optional<Holder<Item>> transformsInto) {
        enchantable.itematic$setTransformsInto(transformsInto);
        return enchantable;
    }
}
