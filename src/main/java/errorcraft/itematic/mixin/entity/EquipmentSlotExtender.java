package errorcraft.itematic.mixin.entity;

import errorcraft.itematic.access.entity.EquipmentSlotAccess;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EquipmentSlot.class)
public class EquipmentSlotExtender implements EquipmentSlotAccess {
    @Shadow
    @Final
    private String name;

    @Override
    public String asString() {
        return this.name;
    }
}
