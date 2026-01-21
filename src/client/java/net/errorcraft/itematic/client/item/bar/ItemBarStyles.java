package net.errorcraft.itematic.client.item.bar;

import net.errorcraft.itematic.client.item.bar.color.provider.ConstantColorProvider;
import net.errorcraft.itematic.client.item.bar.color.provider.HueShiftColorProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.item.ItemBarStyleKeys;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiConsumer;

public class ItemBarStyles {
    private ItemBarStyles() {}

    public static void bootstrap(BiConsumer<Identifier, ItemBarStyle> provider) {
        provider.accept(ItemBarStyleKeys.DAMAGE, new ItemBarStyle(
            ProgressProvider.DAMAGE,
            new HueShiftColorProvider(120, 0),
            List.of(
                Identifier.ofVanilla("item_bar/progress/13"),
                Identifier.ofVanilla("item_bar/progress/12"),
                Identifier.ofVanilla("item_bar/progress/11"),
                Identifier.ofVanilla("item_bar/progress/10"),
                Identifier.ofVanilla("item_bar/progress/9"),
                Identifier.ofVanilla("item_bar/progress/8"),
                Identifier.ofVanilla("item_bar/progress/7"),
                Identifier.ofVanilla("item_bar/progress/6"),
                Identifier.ofVanilla("item_bar/progress/5"),
                Identifier.ofVanilla("item_bar/progress/4"),
                Identifier.ofVanilla("item_bar/progress/3"),
                Identifier.ofVanilla("item_bar/progress/2"),
                Identifier.ofVanilla("item_bar/progress/1"),
                Identifier.ofVanilla("item_bar/progress/0")
            )
        ));
        provider.accept(ItemBarStyleKeys.BUNDLE, new ItemBarStyle(
            ProgressProvider.ITEM_HOLDER_OCCUPANCY,
            new ConstantColorProvider(BundleItemAccessor.itemBarColor()),
            List.of(
                Identifier.ofVanilla("item_bar/progress/0"),
                Identifier.ofVanilla("item_bar/progress/0"),
                Identifier.ofVanilla("item_bar/progress/1"),
                Identifier.ofVanilla("item_bar/progress/2"),
                Identifier.ofVanilla("item_bar/progress/3"),
                Identifier.ofVanilla("item_bar/progress/4"),
                Identifier.ofVanilla("item_bar/progress/5"),
                Identifier.ofVanilla("item_bar/progress/6"),
                Identifier.ofVanilla("item_bar/progress/7"),
                Identifier.ofVanilla("item_bar/progress/8"),
                Identifier.ofVanilla("item_bar/progress/9"),
                Identifier.ofVanilla("item_bar/progress/10"),
                Identifier.ofVanilla("item_bar/progress/11"),
                Identifier.ofVanilla("item_bar/progress/12")
            )
        ));
    }
}
