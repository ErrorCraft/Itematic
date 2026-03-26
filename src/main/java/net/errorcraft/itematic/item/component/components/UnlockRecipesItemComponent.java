package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.KnowledgeBookItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public record UnlockRecipesItemComponent() implements ItemComponent<UnlockRecipesItemComponent> {
    public static final UnlockRecipesItemComponent INSTANCE = new UnlockRecipesItemComponent();
    public static final Codec<UnlockRecipesItemComponent> CODEC = Codec.unit(INSTANCE);
    private static final KnowledgeBookItem DUMMY = new KnowledgeBookItem(new Item.Settings());

    @Override
    public ItemComponentType<UnlockRecipesItemComponent> type() {
        return ItemComponentTypes.UNLOCK_RECIPES;
    }

    @Override
    public Codec<UnlockRecipesItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return DUMMY.use(world, user, hand).isAccepted() ? ItemResult.SUCCEED : ItemResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.RECIPES, List.of());
    }
}
