package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

public class BookScreenExtender {
    @Mixin(BookScreen.Contents.class)
    public interface ContentsExtender {
        @Redirect(
            method = "create",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            )
        )
        private static boolean isOfForWrittenBookUseItemComponentCheck(ItemStack instance, Item item) {
            return instance.itematic$hasComponent(ItemComponentTypes.TEXT_HOLDER);
        }

        @Redirect(
            method = "create",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;WRITABLE_BOOK:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private static boolean isOfForWritableBookUseItemComponentCheck(ItemStack instance, Item item) {
            return instance.itematic$hasComponent(ItemComponentTypes.WRITABLE);
        }
    }

    @Mixin(BookScreen.WrittenBookContents.class)
    public static class WrittenBookContentsExtender {
        @Unique
        private static final JsonElement EMPTY = new JsonPrimitive("");

        @Redirect(
            method = "getPages",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
            )
        )
        private static String toJsonStringUseDynamicRegistry(Text text) {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) {
                return EMPTY.toString();
            }
            return TextUtil.toJsonString(text, world.getRegistryManager());
        }

        @Redirect(
            method = "getPageUnchecked",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
            )
        )
        private MutableText fromJsonUseDynamicRegistry(String json) {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) {
                return Text.empty();
            }
            return TextUtil.fromJsonString(json, world.getRegistryManager());
        }
    }
}
