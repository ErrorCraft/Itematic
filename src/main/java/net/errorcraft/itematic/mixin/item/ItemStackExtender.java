package net.errorcraft.itematic.mixin.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.item.ItemStackAccess;
import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements ItemStackAccess {
    @Shadow
    @Final
    public static ItemStack EMPTY;

    @Shadow
    @Final
    private static String UNBREAKABLE_KEY;

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
        method = "usageTick",
        at = @At("HEAD"),
        cancellable = true
    )
    public void usageTickCheckNullEntry(World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (this.isEmpty()) {
            info.cancel();
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry on the item stack instead of an intrusive registry entry.
     */
    @Overwrite
    public RegistryEntry<Item> getRegistryEntry() {
        return this.entry;
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
        return this.getKey().getValue();
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

    @Inject(
        method = "getRarity",
        at = @At("HEAD"),
        cancellable = true
    )
    public void getRarityCheckNullEntry(CallbackInfoReturnable<Rarity> info) {
        if (this.entry == null) {
            info.setReturnValue(Rarity.COMMON);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public int getMaxDamage() {
        if (this.entry == null) {
            return 0;
        }
        return this.entry.value().getMaxDamage();
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

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean isIn(TagKey<Item> tag) {
        if (this.entry == null) {
            return false;
        }
        return this.entry.isIn(tag);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean isDamageable() {
        if (this.entry == null) {
            return false;
        }
        if (!this.entry.value().isDamageable()) {
            return false;
        }
        return this.nbt == null || !this.nbt.getBoolean(UNBREAKABLE_KEY);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean itemMatches(Predicate<RegistryEntry<Item>> predicate) {
        if (this.entry == null) {
            return false;
        }
        return predicate.test(this.entry);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean itemMatches(RegistryEntry<Item> itemEntry) {
        return this.entry == itemEntry;
    }

    @Inject(
        method = "itemMatches(Lnet/minecraft/registry/entry/RegistryEntryList;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkNullRegistryEntry(RegistryEntryList<Item> registryEntryList, CallbackInfoReturnable<Boolean> info) {
        if (this.entry == null) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "isEnchantable",
        at = @At("HEAD"),
        cancellable = true
    )
    public void isEnchantableCheckNullEntry(CallbackInfoReturnable<Boolean> info) {
        if (this.entry == null) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "areItemsEqual",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean areItemsEqualIsOfUseRegistryEntryCheck(ItemStack instance, Item item, ItemStack left, ItemStack right) {
        return instance.itemMatches(right.getRegistryEntry());
    }

    @Inject(
        method = "hasGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hasGlintCheckNullEntry(CallbackInfoReturnable<Boolean> info) {
        if (this.entry == null) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V"
        )
    )
    private void getTooltipAppendTooltipUseRegistryEntry(Item instance, ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.entry != null) {
            this.entry.value().appendTooltip(stack, world, tooltip, context);
        }
    }

    @Redirect(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getTooltipGetIdUseRegistryEntry(DefaultedRegistry<T> instance, T t) {
        if (this.entry == null) {
            return ItemKeys.AIR.getValue();
        }
        return this.entry.getKey().map(RegistryKey::getValue).orElse(ItemKeys.AIR.getValue());
    }

    @Inject(
        method = "postMine",
        at = @At("HEAD"),
        cancellable = true
    )
    private void postMineUseRegistryEntryNullCheck(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo info) {
        if (this.entry == null) {
            info.cancel();
        }
    }

    @Inject(
        method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;setDamage(I)V",
            shift = At.Shift.AFTER
        )
    )
    private void invokeDamageToolEvent(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        ActionContext.Builder builder = ActionContext.builder(player.getServerWorld(), (ItemStack)(Object) this)
            .entityPosition(ActionContextParameter.THIS, player);
        this.invokeEvent(ItemEvents.DAMAGE_ITEM, builder);
    }

    @Inject(
        method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
        )
    )
    private <T extends LivingEntity> void invokeBreakToolEvent(int amount, T entity, Consumer<T> breakCallback, CallbackInfo info) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext.Builder builder = ActionContext.builder(serverWorld, (ItemStack)(Object) this)
                .entityPosition(ActionContextParameter.THIS, entity);
            this.invokeEvent(ItemEvents.BREAK_ITEM, builder);
        }
    }

    @Override
    public RegistryKey<Item> getKey() {
        if (this.entry == null) {
            return ItemKeys.AIR;
        }
        return this.entry.getKey().orElse(ItemKeys.AIR);
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
    public void damage(int amount, LivingEntity entity, Hand hand) {
        this.damage(amount, entity, e -> e.sendToolBreakStatus(hand));
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

    @Override
    public void invokeEvent(ItemEvent event, ActionContext.Builder builder) {
        if (this.entry == null) {
            return;
        }
        this.entry.value().invokeEvent(event, builder);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        if (this.entry == null) {
            return true;
        }
        return this.entry.value().canMine(state, world, pos, miner);
    }

    @Override
    public boolean itemKeyMatches(Predicate<RegistryKey<Item>> predicate) {
        if (this.entry == null) {
            return false;
        }
        return this.entry.matches(predicate);
    }

    @Override
    public boolean isNetworkSynced() {
        if (this.entry == null) {
            return false;
        }
        return this.entry.value().isNetworkSynced();
    }

    @Override
    public boolean mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (this.entry == null) {
            return false;
        }
        return this.entry.value().mayStartUsing(world, user, hand, stack);
    }
}
