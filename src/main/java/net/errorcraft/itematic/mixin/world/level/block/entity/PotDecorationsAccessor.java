package net.errorcraft.itematic.mixin.world.level.block.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;

@Mixin(PotDecorations.class)
public interface PotDecorationsAccessor {
    @Invoker("<init>")
    static PotDecorations create(List<Optional<Holder<Item>>> items) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static PotDecorations create(Optional<Holder<Item>> back, Optional<Holder<Item>> left, Optional<Holder<Item>> right, Optional<Holder<Item>> front) {
        throw new AssertionError();
    }
}
