package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.item.component.components.MapHolderItemComponent;
import net.errorcraft.itematic.item.data.InventoryTickListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapId.class)
public class MapIdComponentExtender implements InventoryTickListener {
    @Override
    public void itematic$onInventoryTick(ServerLevel world, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        MapItemSavedData mapState = world.getMapData((MapId)(Object) this);
        if (mapState == null) {
            return;
        }

        if (owner instanceof Player playerOwner) {
            mapState.tickCarriedBy(playerOwner, stack);
        }

        if (!mapState.locked && slot != null && slot.getType() == EquipmentSlot.Type.HAND) {
            MapHolderItemComponent.DUMMY.update(world, owner, mapState);
        }
    }
}
