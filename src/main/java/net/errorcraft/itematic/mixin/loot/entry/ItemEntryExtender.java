package net.errorcraft.itematic.mixin.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.loot.entry.ItemEntryAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ItemEntry.class)
public class ItemEntryExtender implements ItemEntryAccess {
    private RegistryKey<Item> itemKey;

    @Inject(
        method = "generateLoot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void generateLootStoreRegistryEntryUseDynamicRegistry(Consumer<ItemStack> lootConsumer, LootContext context, CallbackInfo info, @Share("entry") LocalRef<RegistryEntry<Item>> entry) {
        ItemAccess itemAccess = context.getWorld().getItemAccess();
        Optional<RegistryEntry.Reference<Item>> optional = itemAccess.getOptionalEntry(this.itemKey);
        if (optional.isEmpty()) {
            info.cancel();
            return;
        }
        entry.set(optional.get());
    }

    @Redirect(
        method = "generateLoot",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack generateLootNewItemStackUseRegistryEntry(ItemConvertible item, @Share("entry") LocalRef<RegistryEntry<Item>> entry) {
        return new ItemStack(entry.get());
    }

    @Override
    public RegistryKey<Item> getItemKey() {
        return this.itemKey;
    }

    @Override
    public void setItemKey(RegistryKey<Item> entry) {
        this.itemKey = entry;
    }

    @Mixin(ItemEntry.Serializer.class)
    public static class SerializerExtender {
        @Redirect(
            method = "addEntryFields(Lcom/google/gson/JsonObject;Lnet/minecraft/loot/entry/ItemEntry;Lcom/google/gson/JsonSerializationContext;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
            )
        )
        private <T> Identifier addEntryFieldsGetIdUseRegistryKey(DefaultedRegistry<T> instance, T t, JsonObject jsonObject, ItemEntry itemEntry) {
            return itemEntry.getItemKey().getValue();
        }

        @Redirect(
            method = "fromJson(Lcom/google/gson/JsonObject;Lcom/google/gson/JsonDeserializationContext;II[Lnet/minecraft/loot/condition/LootCondition;[Lnet/minecraft/loot/function/LootFunction;)Lnet/minecraft/loot/entry/ItemEntry;",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/util/JsonHelper;getItem(Lcom/google/gson/JsonObject;Ljava/lang/String;)Lnet/minecraft/item/Item;"
            )
        )
        private Item fromJsonGetItemReturnNull(JsonObject object, String key) {
            return null;
        }

        @Inject(
            method = "fromJson(Lcom/google/gson/JsonObject;Lcom/google/gson/JsonDeserializationContext;II[Lnet/minecraft/loot/condition/LootCondition;[Lnet/minecraft/loot/function/LootFunction;)Lnet/minecraft/loot/entry/ItemEntry;",
            at = @At("TAIL")
        )
        private void fromJsonSetItemKey(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, int i, int j, LootCondition[] lootConditions, LootFunction[] lootFunctions, CallbackInfoReturnable<ItemEntry> info) {
            Identifier id = new Identifier(JsonHelper.getString(jsonObject, "name"));
            info.getReturnValue().setItemKey(RegistryKey.of(RegistryKeys.ITEM, id));
        }
    }
}
