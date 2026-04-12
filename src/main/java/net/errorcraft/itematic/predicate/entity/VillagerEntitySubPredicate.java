package net.errorcraft.itematic.predicate.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntitySubPredicate;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;
import org.jetbrains.annotations.Nullable;

public record VillagerEntitySubPredicate(RegistryEntryList<VillagerType> variant) implements EntitySubPredicate {
    public static final MapCodec<VillagerEntitySubPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.entryList(RegistryKeys.VILLAGER_TYPE).fieldOf("variant").forGetter(VillagerEntitySubPredicate::variant)
    ).apply(instance, VillagerEntitySubPredicate::new));

    public static VillagerEntitySubPredicate of(RegistryEntryList<VillagerType> variant) {
        return new VillagerEntitySubPredicate(variant);
    }

    @Override
    public MapCodec<? extends EntitySubPredicate> getCodec() {
        return CODEC;
    }

    @Override
    public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
        if (entity instanceof VillagerDataContainer villagerDataContainer) {
            return this.variant.contains(villagerDataContainer.getVillagerData().type());
        }

        return false;
    }
}
