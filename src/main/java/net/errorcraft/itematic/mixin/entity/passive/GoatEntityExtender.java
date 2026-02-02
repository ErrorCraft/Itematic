package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(GoatEntity.class)
public abstract class GoatEntityExtender extends MobEntityExtender {
    protected GoatEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
        method = "getGoatHornStack",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"
        )
    )
    private Function<? super RegistryEntry<Instrument>, ? extends ItemStack> getStackForInstrumentUseCreateStack(Function<? super RegistryEntry<Instrument>, ? extends ItemStack> mapper) {
        return instrument -> {
            ItemStack stack = this.getWorld().itematic$createStack(ItemKeys.GOAT_HORN);
            stack.set(DataComponentTypes.INSTRUMENT, instrument);
            return stack;
        };
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.GOAT_SPAWN_EGG;
    }
}
