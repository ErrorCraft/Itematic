package net.errorcraft.itematic.world.item.component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.level.Level;

public class RecipesComponent {
    private static final KnowledgeBookItem DUMMY = new KnowledgeBookItem(new Item.Properties());

    private RecipesComponent() {}

    public static InteractionResult grantRecipes(Level level, Player user, InteractionHand hand) {
        return DUMMY.use(level, user, hand);
    }
}
