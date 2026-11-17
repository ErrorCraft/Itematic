package net.errorcraft.itematic.access.core.component;

import net.minecraft.core.component.BlockTransformer;

public interface BlockTransformerAccess {
    interface BlockTransformDataAccess {
        default boolean itematic$pushEntitiesUpwards() {
            return false;
        }
        default void itematic$setPushEntitiesUpwards(boolean pushEntitiesUpwards) {}

        interface BuilderAccess {
            default BlockTransformer.BlockTransformData.Builder itematic$pushEntitiesUpwards() {
                throw new AssertionError("Implemented via mixin");
            }
        }
    }
}
