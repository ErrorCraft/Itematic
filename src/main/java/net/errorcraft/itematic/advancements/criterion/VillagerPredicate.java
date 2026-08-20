package net.errorcraft.itematic.advancements.criterion;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record VillagerPredicate(HolderSet<VillagerType> variant) implements EntitySubPredicate {
    public static final MapCodec<VillagerPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.VILLAGER_TYPE).fieldOf("variant").forGetter(VillagerPredicate::variant)
    ).apply(instance, VillagerPredicate::new));

    public static VillagerPredicate of(HolderSet<VillagerType> variant) {
        return new VillagerPredicate(variant);
    }

    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 pos) {
        if (entity instanceof VillagerDataHolder villagerDataHolder) {
            return this.variant.contains(villagerDataHolder.getVillagerData().type());
        }

        return false;
    }
}
