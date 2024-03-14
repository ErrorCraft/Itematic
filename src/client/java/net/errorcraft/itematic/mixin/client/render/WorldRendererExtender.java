package net.errorcraft.itematic.mixin.client.render;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.RecordItemComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WorldRenderer.class)
public class WorldRendererExtender {
    @Shadow
    private ClientWorld world;

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private RecordItemComponent recordItemComponent;

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        ),
        slice = @Slice(
            from = @At(
                value = "NEW",
                target = "net/minecraft/particle/ItemStackParticleEffect",
                ordinal = 1
            )
        )
    )
    private ItemStack newItemStackForSplashPotionUseCreateStack(ItemConvertible item) {
        return this.world.itematic$createStack(ItemKeys.SPLASH_POTION);
    }

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;byRawId(I)Lnet/minecraft/item/Item;"
        )
    )
    private Item byRawIdUseDynamicRegistry(int id) {
        return this.world.itematic$getItemAccess().get(id);
    }

    @ModifyConstant(
        method = "processWorldEvent",
        constant = @Constant(
            classValue = MusicDiscItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfMusicDiscItemUseItemComponentCheck(Object reference, Class<MusicDiscItem> clazz) {
        Optional<RecordItemComponent> optionalComponent = ((Item) reference).itematic$getComponent(ItemComponentTypes.RECORD);
        optionalComponent.ifPresent(component -> this.recordItemComponent = component);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "processWorldEvent",
        at = @At("LOAD"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/Item;byRawId(I)Lnet/minecraft/item/Item;"
            )
        ),
        ordinal = 0
    )
    private Item castToMusicDiscItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MusicDiscItem;getSound()Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent getSoundUseItemComponent(MusicDiscItem instance) {
        return this.recordItemComponent.soundEvent().value();
    }

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/particle/ParticleTypes;ITEM:Lnet/minecraft/particle/ParticleType;",
                opcode = Opcodes.GETSTATIC,
                ordinal = 0
            )
        )
    )
    private ItemStack newItemStackForEnderEyeUseCreateStack(ItemConvertible item) {
        return this.world.itematic$createStack(ItemKeys.ENDER_EYE);
    }

    @Redirect(
        method = "playSong",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MusicDiscItem;bySound(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/item/MusicDiscItem;"
        )
    )
    private MusicDiscItem bySoundReturnNull(SoundEvent sound) {
        return null;
    }

    @Inject(
        method = "playSong",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/PositionedSoundInstance;record(Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/client/sound/PositionedSoundInstance;"
        )
    )
    private void setRecordPlayingOverlay(SoundEvent song, BlockPos songPosition, CallbackInfo info) {
        if (this.recordItemComponent != null) {
            this.client.inGameHud.setRecordPlayingOverlay(this.recordItemComponent.getDescription());
            this.recordItemComponent = null;
        }
    }
}
