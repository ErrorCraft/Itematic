package net.errorcraft.itematic.access.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public interface ItemStackRenderStateAccess {
    default boolean itematic$trySubmitUnloadable(PoseStack poseStack, boolean shift, boolean shrink, @Nullable Quaternionf facingOrientation, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        return false;
    }
    default void itematic$setSuccessfullyLoaded(boolean successfullyLoaded) {}
}
