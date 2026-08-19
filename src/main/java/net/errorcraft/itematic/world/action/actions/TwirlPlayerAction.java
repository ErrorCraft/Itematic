package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.network.protocol.game.ClientboundTwirlPacket;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class TwirlPlayerAction implements Action<TwirlPlayerAction> {
    public static final TwirlPlayerAction INSTANCE = new TwirlPlayerAction();
    public static final MapCodec<TwirlPlayerAction> CODEC = MapCodec.unit(INSTANCE);

    private TwirlPlayerAction() {}

    @Override
    public ActionType<TwirlPlayerAction> type() {
        return ActionType.TWIRL_PLAYER;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = context.get(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Player player)) {
            return false;
        }

        ItemStack stack = context.get(LootContextParams.TOOL);
        if (ItemStacks.isNullOrEmpty(stack)) {
            return false;
        }

        float spinAttackStrength = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
        if (spinAttackStrength <= 0.0f) {
            return false;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundTwirlPacket(spinAttackStrength));
        }

        execute(spinAttackStrength, player, context.level(), stack);
        return true;
    }

    public static void execute(float spinAttackStrength, Player player, Level world, ItemStack usedStack) {
        float yaw = player.getYRot();
        float pitch = player.getXRot();
        double x = -Math.sin(yaw * (Math.PI / 180.0d)) * Math.cos(pitch * (Math.PI / 180.0d));
        double y = -Math.sin(pitch * (Math.PI / 180.0d));
        double z = Math.cos(yaw * (Math.PI / 180)) * Math.cos(pitch * (Math.PI / 180.0d));
        double distance = Math.sqrt(x * x + y * y + z * z);
        player.push(x * spinAttackStrength / distance, y * spinAttackStrength / distance, z * spinAttackStrength / distance);
        player.startAutoSpinAttack(20, TridentItem.BASE_DAMAGE, usedStack);
        if (player.onGround()) {
            player.move(MoverType.SELF, new Vec3(0.0d, 1.2d, 0.0d));
        }

        Holder<SoundEvent> sound = EnchantmentHelper.pickHighestLevel(usedStack, EnchantmentEffectComponents.TRIDENT_SOUND)
            .orElse(SoundEvents.TRIDENT_THROW);
        world.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}
