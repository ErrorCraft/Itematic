package errorcraft.itematic.mixin.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.access.item.ItemStackAccess;
import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.ItemKeys;
import errorcraft.itematic.item.ItemStackUtil;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements ItemStackAccess {
    @Shadow
    @Final
    public static ItemStack EMPTY;

    @Shadow
    @Final
    private Item item;

    @Shadow
    private int count;

    @Shadow
    @Nullable
    private NbtCompound nbt;

    @Shadow
    public abstract <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback);

    private RegistryEntry<Item> entry;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<ItemStack> useCustomItemStackCodec(Function<RecordCodecBuilder.Instance<ItemStack>, ? extends App<RecordCodecBuilder.Mu<ItemStack>, ItemStack>> builder) {
        return ItemStackUtil.CODEC;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V",
        at = @At("TAIL")
    )
    private void constructorSetRegistryEntry(RegistryEntry<Item> entry, CallbackInfo info) {
        this.entry = entry;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V",
        at = @At("TAIL")
    )
    private void constructorSetRegistryEntry(RegistryEntry<Item> entry, int count, CallbackInfo info) {
        this.entry = entry;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;isDamageable()Z"
        )
    )
    private boolean constructorIsNeverDamageable(Item instance) {
        return false;
    }

    @Inject(
        method = "getRegistryEntry",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getRegistryEntryUseField(CallbackInfoReturnable<RegistryEntry<Item>> info) {
        info.setReturnValue(this.entry);
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getIdUseEntry(DefaultedRegistry<T> instance, T value) {
        return ItemStackUtil.getId(this.entry);
    }

    @Redirect(
        method = "copy",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack copyUseEntry(ItemConvertible item, int count) {
        if (this.entry == null) {
            return new ItemStack(item, count);
        }
        return new ItemStack(this.entry, count);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public int getMaxCount() {
        return this.item == null ? ItemBase.MAX_MAX_COUNT : this.item.getMaxCount();
    }

    /**
     * @author ErrorCraft
     * @reason Uses an item key check instead of a default air item.
     */
    @Overwrite
    @SuppressWarnings("ConstantConditions")
    public boolean isEmpty() {
        if ((Object)this == EMPTY) {
            return true;
        }
        if (this.isOf(ItemKeys.AIR)) {
            return true;
        }
        return this.count <= 0;
    }

    @Redirect(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getTooltipUseRegistryEntry(DefaultedRegistry<T> instance, T t) {
        if (this.entry == null) {
            return ItemKeys.AIR.getValue();
        }
        return this.entry.getKey().map(RegistryKey::getValue).orElse(ItemKeys.AIR.getValue());
    }

    @Override
    public Optional<NbtCompound> getOptionalNbt() {
        return Optional.ofNullable(this.nbt);
    }

    @Override
    public boolean isOf(RegistryKey<Item> key) {
        return this.entry != null && this.entry.matchesKey(key);
    }

    @Override
    public void damage(int amount, LivingEntity entity) {
        this.damage(amount, entity, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
    }

    @Override
    public <T extends ItemComponent> boolean hasComponent(ItemComponentType<T> type) {
        return this.entry != null && this.entry.value().hasComponent(type);
    }

    @Override
    public <T extends ItemComponent> Optional<T> getComponent(ItemComponentType<T> type) {
        if (this.entry == null) {
            return Optional.empty();
        }
        return this.entry.value().getComponent(type);
    }
}
