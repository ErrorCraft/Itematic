package net.errorcraft.itematic.mixin.village;

import net.errorcraft.itematic.access.village.VillagerDataAccess;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VillagerData.class)
public class VillagerDataExtender implements VillagerDataAccess {
    @Shadow
    @Final
    private RegistryEntry<VillagerProfession> profession;

    @Shadow
    @Final
    private int level;

    @Unique
    private static final String[] LEVELS = {
        "novice",
        "apprentice",
        "journeyman",
        "expert",
        "master"
    };

    public @Nullable TagKey<Trade> itematic$tradeTag() {
        if (this.profession.value().workSound() == null) {
            return null;
        }

        Identifier tag = this.profession.getKey().orElseThrow().getValue().withPath(path -> path + "_" + this.levelName());
        return TagKey.of(ItematicRegistryKeys.TRADE, tag);
    }

    @Unique
    private String levelName() {
        int index = MathHelper.clamp(this.level - 1, 0, LEVELS.length - 1);
        return LEVELS[index];
    }
}
