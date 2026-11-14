package net.errorcraft.itematic.mixin.world.level.saveddata.maps;

import net.errorcraft.itematic.world.item.component.InventoryTickListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapId.class)
public class MapIdExtender implements InventoryTickListener {
    @Override
    public void itematic$onInventoryTick(ServerLevel level, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        MapItemSavedData data = level.getMapData((MapId)(Object) this);
        if (data == null) {
            return;
        }

        if (owner instanceof Player playerOwner) {
            data.tickCarriedBy(playerOwner, stack, null);
        }

        if (!data.locked && slot != null && slot.getType() == EquipmentSlot.Type.HAND) {
            ((MapItem) Items.FILLED_MAP).update(level, owner, data);
        }
    }
}
