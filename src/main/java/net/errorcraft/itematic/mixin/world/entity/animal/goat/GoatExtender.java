package net.errorcraft.itematic.mixin.world.entity.animal.goat;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(Goat.class)
public abstract class GoatExtender extends MobExtender {
    protected GoatExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @ModifyArg(
        method = "createHorn",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"
        )
    )
    private Function<? super Holder<Instrument>, ? extends ItemStack> newItemStackForGoatHornWithInstrumentUseCreateStack(Function<? super Holder<Instrument>, ? extends ItemStack> mapper) {
        return instrument -> {
            ItemStack stack = this.level().itematic$createStack(ItemIds.GOAT_HORN);
            stack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
            return stack;
        };
    }

    @ModifyArg(
        method = "createHorn",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;orElseGet(Ljava/util/function/Supplier;)Ljava/lang/Object;"
        )
    )
    private Supplier<? extends ItemStack> newItemStackForGoatHornUseCreateStack(Supplier<? extends ItemStack> supplier) {
        return () -> this.level().itematic$createStack(ItemIds.GOAT_HORN);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.GOAT_SPAWN_EGG;
    }
}
