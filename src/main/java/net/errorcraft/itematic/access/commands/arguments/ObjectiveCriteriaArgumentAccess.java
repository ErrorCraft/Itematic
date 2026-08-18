package net.errorcraft.itematic.access.commands.arguments;

import net.minecraft.commands.CommandBuildContext;

public interface ObjectiveCriteriaArgumentAccess {
    default void itematic$setContext(CommandBuildContext context) {}
}
