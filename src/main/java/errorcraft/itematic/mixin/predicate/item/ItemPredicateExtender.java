package errorcraft.itematic.mixin.predicate.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemPredicate.class)
public class ItemPredicateExtender implements ItemPredicateAccess {
    private static final String ITEMS_KEY = "items";

    private Set<RegistryKey<Item>> itemKeys;

    @Inject(
        method = "test",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/predicate/NumberRange$IntRange;test(I)Z",
            ordinal = 0
        ),
        cancellable = true
    )
    private void testCheckItemKeys(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (this.itemKeys != null && stack.itemKeyMatches(this.itemKeys::contains)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "fromJson",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/JsonHelper;getArray(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonArray;)Lcom/google/gson/JsonArray;"
        )
    )
    private static JsonArray fromJsonGetArrayReturnNull(JsonObject object, String name, JsonArray defaultArray) {
        return null;
    }

    @Inject(
        method = "toJson",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/gson/JsonObject;<init>()V",
            shift = At.Shift.BY,
            by = 2,
            remap = false
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void toJsonAddEntries(CallbackInfoReturnable<JsonElement> info, JsonObject jsonObject) {
        if (this.itemKeys == null) {
            return;
        }
        JsonArray jsonArray = new JsonArray();
        for (RegistryKey<Item> itemKey : this.itemKeys) {
            jsonArray.add(itemKey.getValue().toString());
        }
        jsonObject.add(ITEMS_KEY, jsonArray);
    }

    @Inject(
        method = "fromJson",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void fromJsonSetItemKeys(JsonElement el, CallbackInfoReturnable<ItemPredicate> info, JsonObject jsonObject) {
        JsonArray jsonArray = JsonHelper.getArray(jsonObject, ITEMS_KEY, null);
        if (jsonArray == null) {
            return;
        }
        Set<RegistryKey<Item>> itemKeys = new HashSet<>();
        for (JsonElement jsonElement : jsonArray) {
            Identifier id = new Identifier(JsonHelper.asString(jsonElement, "item"));
            itemKeys.add(RegistryKey.of(RegistryKeys.ITEM, id));
        }

        info.getReturnValue().setItemKeys(itemKeys);
    }

    @Override
    public void setItemKeys(Set<RegistryKey<Item>> itemKeys) {
        this.itemKeys = itemKeys;
    }
}
