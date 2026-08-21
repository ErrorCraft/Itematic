package net.errorcraft.itematic.mixin.world.level.saveddata.maps;

import net.errorcraft.itematic.world.item.behavior.behaviors.MapHolderItemBehavior;
import net.errorcraft.itematic.world.item.component.InventoryTickListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapId.class)
public class MapIdExtender implements InventoryTickListener {
    @Override
    public void itematic$onInventoryTick(ServerLevel level, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        MapItemSavedData mapState = level.getMapData((MapId)(Object) this);
        if (mapState == null) {
            return;
        }

        if (owner instanceof Player playerOwner) {
            mapState.tickCarriedBy(playerOwner, stack);
        }

        if (!mapState.locked && slot != null && slot.getType() == EquipmentSlot.Type.HAND) {
            MapHolderItemBehavior.DUMMY.update(level, owner, mapState);
        }
    }
}
