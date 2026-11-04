package net.errorcraft.itematic.mixin.world.item.component;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.world.item.component.UseCooldownAccess;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.component.UseCooldowns;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(UseCooldown.class)
public class UseCooldownExtender implements UseCooldownAccess {
    @Unique
    @Nullable
    private UseDurationProvider ticks;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static Codec<UseCooldown> useUseDurationProvider(Codec<UseCooldown> original) {
        Codec<UseCooldown> durationProviderCodec = RecordCodecBuilder.create(instance -> instance.group(
            UseDurationProvider.CODEC.fieldOf("ticks").forGetter(UseCooldown::itematic$ticks),
            Identifier.CODEC.optionalFieldOf("cooldown_group").forGetter(UseCooldown::cooldownGroup)
        ).apply(instance, UseCooldowns::create));
        return ItematicCodecs.either(
            durationProviderCodec,
            original,
            useCooldown -> {
                if (useCooldown.itematic$ticks() == null) {
                    return Either.right(useCooldown);
                }

                return Either.left(useCooldown);
            }
        );
    }

    @WrapOperation(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/UseCooldown;ticks()I"
        )
    )
    private int useUseDurationProvider(UseCooldown instance, Operation<Integer> original, ItemStack stack, LivingEntity user) {
        if (this.ticks == null) {
            return original.call(instance);
        }

        return this.ticks.get(stack, user)
            .orElse(0);
    }

    @WrapWithCondition(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/ItemStack;I)V"
        )
    )
    private boolean checkForPositiveTime(ItemCooldowns instance, ItemStack item, int time) {
        return time > 0;
    }

    @Override
    public @Nullable UseDurationProvider itematic$ticks() {
        return this.ticks;
    }

    @Override
    public void itematic$setTicks(UseDurationProvider ticks) {
        this.ticks = ticks;
    }
}
