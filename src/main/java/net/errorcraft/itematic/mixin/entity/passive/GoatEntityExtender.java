package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(Goat.class)
public abstract class GoatEntityExtender extends MobEntityExtender {
    protected GoatEntityExtender(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyArg(
        method = "createHorn",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"
        )
    )
    private Function<? super Holder<Instrument>, ? extends ItemStack> getStackForInstrumentUseCreateStack(Function<? super Holder<Instrument>, ? extends ItemStack> mapper) {
        return instrument -> {
            ItemStack stack = this.level().itematic$createStack(ItemKeys.GOAT_HORN);
            stack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
            return stack;
        };
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.GOAT_SPAWN_EGG;
    }
}
