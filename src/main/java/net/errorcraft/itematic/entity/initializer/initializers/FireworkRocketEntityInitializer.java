package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.Vec3d;

public class FireworkRocketEntityInitializer implements EntityInitializer<FireworkRocketEntity> {
    public static final FireworkRocketEntityInitializer INSTANCE = new FireworkRocketEntityInitializer();

    private FireworkRocketEntityInitializer() {}

    @Override
    public FireworkRocketEntity create(NewActionContext context, SpawnReason reason) {
        Vec3d pos = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        return new FireworkRocketEntity(
            context.world(),
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY).copyWithCount(1)
        );
    }
}
