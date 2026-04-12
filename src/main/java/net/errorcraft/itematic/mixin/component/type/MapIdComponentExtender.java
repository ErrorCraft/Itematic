package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.item.component.components.MapHolderItemComponent;
import net.errorcraft.itematic.item.data.InventoryTickListener;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapIdComponent.class)
public class MapIdComponentExtender implements InventoryTickListener {
    @Override
    public void itematic$onInventoryTick(ServerWorld world, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        MapState mapState = world.getMapState((MapIdComponent)(Object) this);
        if (mapState == null) {
            return;
        }

        if (owner instanceof PlayerEntity playerOwner) {
            mapState.update(playerOwner, stack);
        }

        if (!mapState.locked && slot != null && slot.getType() == EquipmentSlot.Type.HAND) {
            MapHolderItemComponent.DUMMY.updateColors(world, owner, mapState);
        }
    }
}
