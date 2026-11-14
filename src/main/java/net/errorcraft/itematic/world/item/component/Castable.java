package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

public record Castable(Holder<SoundEvent> castSound, Holder<SoundEvent> retrieveSound, ResourceKey<LootTable> fishLootTable, ResolvableInt fishExperience) {
    public static final Codec<Castable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SoundEvent.CODEC.fieldOf("cast_sound").forGetter(Castable::castSound),
        SoundEvent.CODEC.fieldOf("retrieve_sound").forGetter(Castable::retrieveSound),
        LootTable.KEY_CODEC.fieldOf("fish_loot_table").forGetter(Castable::fishLootTable),
        ResolvableInt.CODEC.fieldOf("fish_experience").forGetter(Castable::fishExperience)
    ).apply(instance, Castable::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Castable> STREAM_CODEC = StreamCodec.composite(
        SoundEvent.STREAM_CODEC, Castable::castSound,
        SoundEvent.STREAM_CODEC, Castable::retrieveSound,
        ResourceKey.streamCodec(Registries.LOOT_TABLE), Castable::fishLootTable,
        ResolvableInt.STREAM_CODEC, Castable::fishExperience,
        Castable::new
    );
    public static final ScopedValue<Castable> CASTABLE = ScopedValue.newInstance();

    public void use(Level level, Player user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (!this.retrieve(level, user, stack, stackExchanger)) {
            this.cast(level, user, stack);
        }
    }

    private void cast(Level level, Player user, ItemStack stack) {
        level.playSound(
            null,
            user.getX(),
            user.getY(),
            user.getZ(),
            this.castSound,
            SoundSource.NEUTRAL,
            0.5f,
            0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f)
        );
        if (level instanceof ServerLevel serverLevel) {
            int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, stack, user);
            int speed = (int) (EnchantmentHelper.getFishingTimeReduction(serverLevel, stack, user) * SharedConstants.TICKS_PER_SECOND);
            Projectile.spawnProjectile(new FishingHook(user, level, luck, speed), serverLevel, stack);
        }

        user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        user.gameEvent(GameEvent.ITEM_INTERACT_START);
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean retrieve(Level level, Player user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (user.fishing == null) {
            return false;
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .build();
            stack.itematic$damage(
                ScopedValue.where(CASTABLE, this)
                    .call(() -> user.fishing.retrieve(stack)),
                context
            );
        }

        level.playSound(
            null,
            user.getX(),
            user.getY(),
            user.getZ(),
            this.retrieveSound.value(),
            SoundSource.NEUTRAL,
            1.0f,
            0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f)
        );
        user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        return true;
    }
}
