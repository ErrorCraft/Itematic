package net.errorcraft.itematic.world;

import net.minecraft.world.InteractionResult;

public class InteractionResults {
    private InteractionResults() {}

    public static InteractionResult.Success pickSuccess(InteractionResult.Success left, InteractionResult.Success right) {
        // Priority order: PREDICTED > SERVER_ONLY > NONE
        // PREDICTED is prioritized over SERVER_ONLY as the client initiates the action
        return switch (left.swingSource()) {
            case NONE -> right;
            case SERVER_ONLY -> right.swingSource() == InteractionResult.SwingSource.PREDICTED
                ? right
                : left;
            case PREDICTED -> left;
        };
    }
}
