package net.errorcraft.itematic.client.item.bar;

import net.errorcraft.itematic.client.item.bar.color.provider.FirstToPassConditionColorProvider;
import net.errorcraft.itematic.client.item.bar.color.provider.HueShiftColorProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.errorcraft.itematic.references.ItemBarStyleIds;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.BiConsumer;

public class ItemBarStyles {
    private ItemBarStyles() {}

    public static void bootstrap(BiConsumer<Identifier, ItemBarStyle> provider) {
        provider.accept(ItemBarStyleIds.DAMAGE, new ItemBarStyle(
            ProgressProvider.DAMAGE,
            new HueShiftColorProvider(120, 0),
            List.of(
                Identifier.withDefaultNamespace("item_bar/progress/13"),
                Identifier.withDefaultNamespace("item_bar/progress/12"),
                Identifier.withDefaultNamespace("item_bar/progress/11"),
                Identifier.withDefaultNamespace("item_bar/progress/10"),
                Identifier.withDefaultNamespace("item_bar/progress/9"),
                Identifier.withDefaultNamespace("item_bar/progress/8"),
                Identifier.withDefaultNamespace("item_bar/progress/7"),
                Identifier.withDefaultNamespace("item_bar/progress/6"),
                Identifier.withDefaultNamespace("item_bar/progress/5"),
                Identifier.withDefaultNamespace("item_bar/progress/4"),
                Identifier.withDefaultNamespace("item_bar/progress/3"),
                Identifier.withDefaultNamespace("item_bar/progress/2"),
                Identifier.withDefaultNamespace("item_bar/progress/1"),
                Identifier.withDefaultNamespace("item_bar/progress/0")
            )
        ));
        provider.accept(ItemBarStyleIds.BUNDLE, new ItemBarStyle(
            ProgressProvider.ITEM_HOLDER_OCCUPANCY,
            FirstToPassConditionColorProvider.of(
                BundleItemAccessor.itemBarColor(),
                FirstToPassConditionColorProvider.Entry.of(
                    BundleItemAccessor.fullItemBarColor(),
                    1.0f
                )
            ),
            List.of(
                Identifier.withDefaultNamespace("item_bar/progress/0"),
                Identifier.withDefaultNamespace("item_bar/progress/0"),
                Identifier.withDefaultNamespace("item_bar/progress/1"),
                Identifier.withDefaultNamespace("item_bar/progress/2"),
                Identifier.withDefaultNamespace("item_bar/progress/3"),
                Identifier.withDefaultNamespace("item_bar/progress/4"),
                Identifier.withDefaultNamespace("item_bar/progress/5"),
                Identifier.withDefaultNamespace("item_bar/progress/6"),
                Identifier.withDefaultNamespace("item_bar/progress/7"),
                Identifier.withDefaultNamespace("item_bar/progress/8"),
                Identifier.withDefaultNamespace("item_bar/progress/9"),
                Identifier.withDefaultNamespace("item_bar/progress/10"),
                Identifier.withDefaultNamespace("item_bar/progress/11"),
                Identifier.withDefaultNamespace("item_bar/progress/12")
            )
        ));
    }
}
