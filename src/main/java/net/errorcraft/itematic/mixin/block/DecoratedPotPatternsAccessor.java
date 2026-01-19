package net.errorcraft.itematic.mixin.block;

import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DecoratedPotPatterns.class)
public interface DecoratedPotPatternsAccessor {
    @Accessor("DECORATED_POT_SIDE_KEY")
    @Contract
    static RegistryKey<String> decoratedPotSide() {
        throw new AssertionError();
    }

    @Accessor("ANGLER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> angler() {
        throw new AssertionError();
    }

    @Accessor("ARCHER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> archer() {
        throw new AssertionError();
    }

    @Accessor("ARMS_UP_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> armsUp() {
        throw new AssertionError();
    }

    @Accessor("BLADE_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> blade() {
        throw new AssertionError();
    }

    @Accessor("BREWER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> brewer() {
        throw new AssertionError();
    }

    @Accessor("BURN_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> burn() {
        throw new AssertionError();
    }

    @Accessor("DANGER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> danger() {
        throw new AssertionError();
    }

    @Accessor("EXPLORER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> explorer() {
        throw new AssertionError();
    }

    @Accessor("FLOW_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> flow() {
        throw new AssertionError();
    }

    @Accessor("FRIEND_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> friend() {
        throw new AssertionError();
    }

    @Accessor("GUSTER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> guster() {
        throw new AssertionError();
    }

    @Accessor("HEART_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> heart() {
        throw new AssertionError();
    }

    @Accessor("HEARTBREAK_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> heartbreak() {
        throw new AssertionError();
    }

    @Accessor("HOWL_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> howl() {
        throw new AssertionError();
    }

    @Accessor("MINER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> miner() {
        throw new AssertionError();
    }

    @Accessor("MOURNER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> mourner() {
        throw new AssertionError();
    }

    @Accessor("PLENTY_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> plenty() {
        throw new AssertionError();
    }

    @Accessor("POTTERY_PATTERN_PRIZE_KEY")
    @Contract
    static RegistryKey<String> prize() {
        throw new AssertionError();
    }

    @Accessor("SCRAPE_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> scrape() {
        throw new AssertionError();
    }

    @Accessor("SHEAF_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> sheaf() {
        throw new AssertionError();
    }

    @Accessor("SHELTER_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> shelter() {
        throw new AssertionError();
    }

    @Accessor("SKULL_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> skull() {
        throw new AssertionError();
    }

    @Accessor("SNORT_POTTERY_PATTERN_KEY")
    @Contract
    static RegistryKey<String> snort() {
        throw new AssertionError();
    }
}
