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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    private ItemStack processWorldEventNewItemStackForSplashPotionUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.world.getItem(ItemKeys.SPLASH_POTION));
    }

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;byRawId(I)Lnet/minecraft/item/Item;"
        )
    )
    private Item processWorldEventByRawIdUseDynamicRegistry(int id) {
        return this.world.getItemAccess().get(id);
    }

    @ModifyConstant(
        method = "processWorldEvent",
        constant = @Constant(
            classValue = MusicDiscItem.class,
            ordinal = 0
        )
    )
    private boolean processWorldEventInstanceOfMusicDiscItemUseItemComponentCheck(Object reference, Class<MusicDiscItem> clazz) {
        Optional<RecordItemComponent> optionalComponent = ((Item) reference).getComponent(ItemComponentTypes.RECORD);
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
    private Item processWorldEventCastToMusicDiscItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MusicDiscItem;getSound()Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent processWorldEventGetSoundUseItemComponent(MusicDiscItem instance) {
        return this.recordItemComponent.soundEvent().value();
    }

    @Redirect(
        method = "playSong",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MusicDiscItem;bySound(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/item/MusicDiscItem;"
        )
    )
    private MusicDiscItem playSongBySoundReturnNull(SoundEvent sound) {
        return null;
    }

    @Inject(
        method = "playSong",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/PositionedSoundInstance;record(Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/client/sound/PositionedSoundInstance;"
        )
    )
    private void playSongSetRecordPlayingOverlay(SoundEvent song, BlockPos songPosition, CallbackInfo info) {
        if (this.recordItemComponent != null) {
            this.client.inGameHud.setRecordPlayingOverlay(this.recordItemComponent.getDescription());
            this.recordItemComponent = null;
        }
    }
}
