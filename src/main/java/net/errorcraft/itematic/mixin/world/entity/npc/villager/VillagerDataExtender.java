package net.errorcraft.itematic.mixin.world.entity.npc.villager;

import net.errorcraft.itematic.access.world.entity.npc.villager.VillagerDataAccess;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VillagerData.class)
public class VillagerDataExtender implements VillagerDataAccess {
    @Shadow
    @Final
    private Holder<VillagerProfession> profession;

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

        Identifier tag = this.profession.unwrapKey()
            .orElseThrow()
            .identifier()
            .withPath(path -> path + "_" + this.levelName());
        return TagKey.create(ItematicRegistries.TRADE, tag);
    }

    @Unique
    private String levelName() {
        int index = Mth.clamp(this.level - 1, 0, LEVELS.length - 1);
        return LEVELS[index];
    }
}
