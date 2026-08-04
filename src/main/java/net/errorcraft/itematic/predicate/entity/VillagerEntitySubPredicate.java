package net.errorcraft.itematic.predicate.entity;

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

public record VillagerEntitySubPredicate(HolderSet<VillagerType> variant) implements EntitySubPredicate {
    public static final MapCodec<VillagerEntitySubPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.VILLAGER_TYPE).fieldOf("variant").forGetter(VillagerEntitySubPredicate::variant)
    ).apply(instance, VillagerEntitySubPredicate::new));

    public static VillagerEntitySubPredicate of(HolderSet<VillagerType> variant) {
        return new VillagerEntitySubPredicate(variant);
    }

    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel world, @Nullable Vec3 pos) {
        if (entity instanceof VillagerDataHolder villagerDataContainer) {
            return this.variant.contains(villagerDataContainer.getVillagerData().type());
        }

        return false;
    }
}
