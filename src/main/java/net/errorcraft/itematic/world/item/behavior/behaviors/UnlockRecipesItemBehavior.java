package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.level.Level;

import java.util.List;

public class UnlockRecipesItemBehavior implements ItemBehavior<UnlockRecipesItemBehavior> {
    public static final UnlockRecipesItemBehavior INSTANCE = new UnlockRecipesItemBehavior();
    public static final Codec<UnlockRecipesItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final KnowledgeBookItem DUMMY = new KnowledgeBookItem(new Item.Properties());

    private UnlockRecipesItemBehavior() {}

    @Override
    public ItemBehaviorType<UnlockRecipesItemBehavior> type() {
        return ItemBehaviorType.UNLOCK_RECIPES;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return DUMMY.use(world, user, hand).consumesAction() ? ItemResult.SUCCEED : ItemResult.PASS;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.RECIPES, List.of());
    }
}
