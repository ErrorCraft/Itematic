package errorcraft.itematic.mixin.advancement;

import com.google.gson.JsonObject;
import errorcraft.itematic.util.JsonUtil;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class AdvancementExtender {
    @Mixin(Advancement.Builder.class)
    public static class BuilderExtender {
        @Redirect(
            method = "fromJson",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/advancement/AdvancementDisplay;fromJson(Lcom/google/gson/JsonObject;)Lnet/minecraft/advancement/AdvancementDisplay;"
            )
        )
        private static AdvancementDisplay fromJsonSetIconUseDynamicRegistry(JsonObject json, JsonObject obj, AdvancementEntityPredicateDeserializer predicateDeserializer) {
            AdvancementDisplay display = AdvancementDisplay.fromJson(json);
            JsonObject iconObject = JsonHelper.getObject(json, "icon");
            display.setIcon(getItemStack(iconObject, predicateDeserializer.getRegistryManager()));
            return display;
        }

        private static ItemStack getItemStack(JsonObject json, DynamicRegistryManager registryManager) {
            RegistryEntry<Item> item = JsonUtil.getItem(json, "item", registryManager);
            NbtCompound nbt = JsonUtil.getNbtCompound(json, "nbt", null);

            ItemStack itemStack = new ItemStack(item);
            itemStack.setNbt(nbt);
            return itemStack;
        }
    }
}
