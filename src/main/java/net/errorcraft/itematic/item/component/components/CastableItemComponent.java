package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public record CastableItemComponent() implements ItemComponent<CastableItemComponent> {
    public static final CastableItemComponent INSTANCE = new CastableItemComponent();
    public static final Codec<CastableItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<CastableItemComponent> type() {
        return ItemComponentTypes.CASTABLE;
    }

    @Override
    public Codec<CastableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (!this.tryRetract(world, user, stack, resultStackConsumer)) {
            this.cast(world, user, stack);
        }
        return ActionResult.success(world.isClient());
    }

    private boolean tryRetract(World world, PlayerEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (user.fishHook == null) {
            return false;
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            stack.itematic$damage(user.fishHook.use(stack), context);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        return true;
    }

    private void cast(World world, PlayerEntity user, ItemStack stack) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient()) {
            world.spawnEntity(new FishingBobberEntity(user, world, EnchantmentHelper.getLuckOfTheSea(stack), EnchantmentHelper.getLure(stack)));
        }

        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }
}
