package errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonObject;
import errorcraft.itematic.access.recipe.IngredientAccess;
import errorcraft.itematic.access.recipe.IngredientEntryAccess;
import errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Mixin(Ingredient.class)
public class IngredientExtender implements IngredientAccess {
    @Shadow
    @Final
    private Ingredient.Entry[] entries;

    @Shadow
    @Nullable
    private ItemStack[] matchingStacks;

    @Inject(
        method = "entryFromJson",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/ShapedRecipe;getItem(Lcom/google/gson/JsonObject;)Lnet/minecraft/item/Item;"
        ),
        cancellable = true
    )
    private static void entryFromJsonUseRegistryEntry(JsonObject json, CallbackInfoReturnable<Ingredient.Entry> info) {
        RegistryEntry<Item> item = RecipeSerializerUtil.getItem(json);
        info.setReturnValue(StackEntryAccess.create(new ItemStack(item)));
    }

    @Override
    public void initMatchingStacks(Registry<Item> registry) {
        this.matchingStacks = Arrays.stream(this.entries).flatMap(entry -> entry.getStacks(registry).stream()).distinct().toArray(ItemStack[]::new);
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$TagEntry")
    public static class TagEntryExtender implements IngredientEntryAccess {
        @Shadow
        @Final
        private TagKey<Item> tag;

        @Override
        public Collection<ItemStack> getStacks(Registry<Item> registry) {
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for (RegistryEntry<Item> entry : registry.iterateEntries(this.tag)) {
                itemStacks.add(new ItemStack(entry));
            }
            return itemStacks;
        }
    }

    @Mixin(Ingredient.StackEntry.class)
    public static abstract class StackEntryExtender implements IngredientEntryAccess {
        @Shadow
        public abstract Collection<ItemStack> getStacks();

        @Override
        public Collection<ItemStack> getStacks(Registry<Item> registry) {
            return this.getStacks();
        }
    }

    @Mixin(Ingredient.StackEntry.class)
    public interface StackEntryAccess {
        @Invoker("<init>")
        static Ingredient.StackEntry create(ItemStack stack) {
            throw new AssertionError();
        }
    }
}
