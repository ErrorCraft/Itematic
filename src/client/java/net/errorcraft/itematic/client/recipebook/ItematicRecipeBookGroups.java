package net.errorcraft.itematic.client.recipebook;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.recipebook.RecipeBookGroup;

import java.util.List;

public class ItematicRecipeBookGroups {
    public static final RecipeBookGroup BREWING_SEARCH = ClassTinkerers.getEnum(RecipeBookGroup.class, "ITEMATIC$BREWING_SEARCH");
    public static final RecipeBookGroup BREWING_MODIFY = ClassTinkerers.getEnum(RecipeBookGroup.class, "ITEMATIC$BREWING_MODIFY");
    public static final RecipeBookGroup BREWING_AMPLIFY = ClassTinkerers.getEnum(RecipeBookGroup.class, "ITEMATIC$BREWING_AMPLIFY");

    public static final List<RecipeBookGroup> BREWING = ImmutableList.of(
        BREWING_SEARCH,
        BREWING_MODIFY,
        BREWING_AMPLIFY
    );
}
