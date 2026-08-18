package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.SharedConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class CastableItemBehavior implements ItemBehavior<CastableItemBehavior> {
    public static final CastableItemBehavior INSTANCE = new CastableItemBehavior();
    public static final Codec<CastableItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private CastableItemBehavior() {}

    @Override
    public ItemBehaviorType<CastableItemBehavior> type() {
        return ItemBehaviorType.CASTABLE;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (!this.tryRetract(world, user, stack, stackExchanger)) {
            this.cast(world, user, stack);
        }

        return ItemResult.SUCCEED;
    }

    private boolean tryRetract(Level world, Player user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (user.fishing == null) {
            return false;
        }

        if (world instanceof ServerLevel serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .build();
            stack.itematic$damage(user.fishing.retrieve(stack), context);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        return true;
    }

    private void cast(Level world, Player user, ItemStack stack) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (world instanceof ServerLevel serverWorld) {
            int luck = EnchantmentHelper.getFishingLuckBonus(serverWorld, stack, user);
            int speed = (int) (EnchantmentHelper.getFishingTimeReduction(serverWorld, stack, user) * SharedConstants.TICKS_PER_SECOND);
            Projectile.spawnProjectile(new FishingHook(user, world, luck, speed), serverWorld, stack);
        }

        user.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        user.gameEvent(GameEvent.ITEM_INTERACT_START);
    }
}
