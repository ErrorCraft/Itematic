package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.level.Level;
import java.util.List;

public class UnlockRecipesItemComponent implements ItemComponent<UnlockRecipesItemComponent> {
    public static final UnlockRecipesItemComponent INSTANCE = new UnlockRecipesItemComponent();
    public static final Codec<UnlockRecipesItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final KnowledgeBookItem DUMMY = new KnowledgeBookItem(new Item.Properties());

    private UnlockRecipesItemComponent() {}

    @Override
    public ItemComponentType<UnlockRecipesItemComponent> type() {
        return ItemComponentTypes.UNLOCK_RECIPES;
    }

    @Override
    public Codec<UnlockRecipesItemComponent> codec() {
        return CODEC;
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
