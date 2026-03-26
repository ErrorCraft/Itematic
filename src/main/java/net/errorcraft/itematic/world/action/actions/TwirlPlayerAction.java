package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TwirlPlayerAction implements Action<TwirlPlayerAction> {
    public static final TwirlPlayerAction INSTANCE = new TwirlPlayerAction();
    public static final MapCodec<TwirlPlayerAction> CODEC = MapCodec.unit(INSTANCE);

    private TwirlPlayerAction() {}

    @Override
    public ActionType<TwirlPlayerAction> type() {
        return ActionTypes.TWIRL_PLAYER;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        float spinAttackStrength = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
        if (spinAttackStrength <= 0.0f) {
            return false;
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(new TwirlS2CPacket(spinAttackStrength));
        }

        execute(spinAttackStrength, player, context.world(), stack);
        return true;
    }

    public static void execute(float spinAttackStrength, PlayerEntity player, World world, ItemStack usedStack) {
        float yaw = player.getYaw();
        float pitch = player.getPitch();
        double x = -Math.sin(yaw * (Math.PI / 180.0d)) * Math.cos(pitch * (Math.PI / 180.0d));
        double y = -Math.sin(pitch * (Math.PI / 180.0d));
        double z = Math.cos(yaw * (Math.PI / 180)) * Math.cos(pitch * (Math.PI / 180.0d));
        double distance = Math.sqrt(x * x + y * y + z * z);
        player.addVelocity(x * spinAttackStrength / distance, y * spinAttackStrength / distance, z * spinAttackStrength / distance);
        player.useRiptide(20, TridentItem.ATTACK_DAMAGE, usedStack);
        if (player.isOnGround()) {
            player.move(MovementType.SELF, new Vec3d(0.0d, 1.2d, 0.0d));
        }

        RegistryEntry<SoundEvent> sound = EnchantmentHelper.getEffect(usedStack, EnchantmentEffectComponentTypes.TRIDENT_SOUND)
            .orElse(SoundEvents.ITEM_TRIDENT_THROW);
        world.playSoundFromEntity(null, player, sound.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}
