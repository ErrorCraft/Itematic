package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.DebugStickItemAccessor;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DebugStickState;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class DebugStickItemComponent implements ItemComponent<DebugStickItemComponent> {
    public static final DebugStickItemComponent INSTANCE = new DebugStickItemComponent();
    public static final Codec<DebugStickItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final DebugStickItemAccessor DUMMY = (DebugStickItemAccessor) new DebugStickItem(new Item.Properties());

    private DebugStickItemComponent() {}

    @Override
    public ItemComponentType<DebugStickItemComponent> type() {
        return ItemComponentTypes.DEBUG_STICK;
    }

    @Override
    public Codec<DebugStickItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        Level world = context.getLevel();
        if (world.isClientSide()) {
            return ItemResult.PASS;
        }

        Player player = context.getPlayer();
        if (player == null) {
            return ItemResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        if (!DUMMY.itematic$use(player, world.getBlockState(pos), world, pos, true, context.getItemInHand())) {
            return ItemResult.PASS;
        }

        return ItemResult.SUCCEED;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DEBUG_STICK_STATE, DebugStickState.EMPTY);
    }

    public void use(LivingEntity user, BlockState state, LevelAccessor world, BlockPos pos, ItemStack stack) {
        if (!world.isClientSide() && user instanceof Player playerUser) {
            DUMMY.itematic$use(playerUser, state, world, pos, false, stack);
        }
    }
}
