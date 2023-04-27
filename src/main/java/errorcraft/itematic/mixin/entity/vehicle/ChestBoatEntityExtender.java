package errorcraft.itematic.mixin.entity.vehicle;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChestBoatEntity.class)
public class ChestBoatEntityExtender extends BoatEntity {
    public ChestBoatEntityExtender(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public RegistryKey<Item> asItemKey() {
        return switch (this.getVariant()) {
            default -> ItemKeys.OAK_CHEST_BOAT;
            case SPRUCE -> ItemKeys.SPRUCE_CHEST_BOAT;
            case BIRCH -> ItemKeys.BIRCH_CHEST_BOAT;
            case JUNGLE -> ItemKeys.JUNGLE_CHEST_BOAT;
            case ACACIA -> ItemKeys.ACACIA_CHEST_BOAT;
            case CHERRY -> ItemKeys.CHERRY_CHEST_BOAT;
            case DARK_OAK -> ItemKeys.DARK_OAK_CHEST_BOAT;
            case MANGROVE -> ItemKeys.MANGROVE_CHEST_BOAT;
            case BAMBOO -> ItemKeys.BAMBOO_CHEST_RAFT;
        };
    }
}
