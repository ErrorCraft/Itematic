package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class FireworkRocketEntityInitializer implements EntityInitializer<FireworkRocketEntity> {
    public static final FireworkRocketEntityInitializer INSTANCE = new FireworkRocketEntityInitializer();

    private FireworkRocketEntityInitializer() {}

    @Override
    public @Nullable FireworkRocketEntity create(ActionContext context, EntitySpawnReason reason) {
        Vec3 pos = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        return new FireworkRocketEntity(
            context.world(),
            pos.x(),
            pos.y(),
            pos.z(),
            context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY).copyWithCount(1)
        );
    }
}
