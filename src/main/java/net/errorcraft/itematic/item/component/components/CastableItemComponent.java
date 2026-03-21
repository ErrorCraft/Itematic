package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.SharedConstants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class CastableItemComponent implements ItemComponent<CastableItemComponent> {
    public static final CastableItemComponent INSTANCE = new CastableItemComponent();
    public static final Codec<CastableItemComponent> CODEC = Codec.unit(INSTANCE);

    private CastableItemComponent() {}

    @Override
    public ItemComponentType<CastableItemComponent> type() {
        return ItemComponentTypes.CASTABLE;
    }

    @Override
    public Codec<CastableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (!this.tryRetract(world, user, stack, stackExchanger)) {
            this.cast(world, user, stack);
        }

        return ItemResult.SUCCEED;
    }

    private boolean tryRetract(World world, PlayerEntity user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (user.fishHook == null) {
            return false;
        }

        if (world instanceof ServerWorld serverWorld) {
            NewActionContext context = NewActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParameters.THIS_ENTITY, user)
                .add(LootContextParameters.ORIGIN, user.getPos())
                .add(LootContextParameters.TOOL, stack)
                .build();
            stack.itematic$damage(user.fishHook.use(stack), context);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        return true;
    }

    private void cast(World world, PlayerEntity user, ItemStack stack) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (world instanceof ServerWorld serverWorld) {
            int luck = EnchantmentHelper.getFishingLuckBonus(serverWorld, stack, user);
            int speed = (int) (EnchantmentHelper.getFishingTimeReduction(serverWorld, stack, user) * SharedConstants.TICKS_PER_SECOND);
            ProjectileEntity.spawn(new FishingBobberEntity(user, world, luck, speed), serverWorld, stack);
        }

        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }
}
