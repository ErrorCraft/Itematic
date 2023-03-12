package errorcraft.itematic.item.component;

import errorcraft.itematic.item.component.components.*;
import errorcraft.itematic.mixin.registry.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItemComponentTypes {
    public static final RegistryKey<Registry<ItemComponentType<?>>> ITEM_COMPONENT_TYPE_KEY = RegistryKey.ofRegistry(new Identifier("item_component_type"));
    public static final Registry<ItemComponentType<?>> ITEM_COMPONENT_TYPE = RegistriesAccessor.create(ITEM_COMPONENT_TYPE_KEY, r -> ItemComponentTypes.TEST);

    public static final ItemComponentType<TestItemComponent> TEST = register("test", new ItemComponentType<>(TestItemComponent.CODEC));
    public static final ItemComponentType<UseDurationItemComponent> USE_DURATION = register("use_duration", new ItemComponentType<>(UseDurationItemComponent.CODEC));
    public static final ItemComponentType<FoodItemComponent> FOOD = register("food", new ItemComponentType<>(FoodItemComponent.CODEC));
    public static final ItemComponentType<BlockItemComponent> BLOCK = register("block", new ItemComponentType<>(BlockItemComponent.CODEC));
    public static final ItemComponentType<DamageableItemComponent> DAMAGEABLE = register("damageable", new ItemComponentType<>(DamageableItemComponent.CODEC));
    public static final ItemComponentType<ToolItemComponent> TOOL = register("tool", new ItemComponentType<>(ToolItemComponent.CODEC));
    public static final ItemComponentType<EntityItemComponent> ENTITY = register("entity", new ItemComponentType<>(EntityItemComponent.CODEC));

    private ItemComponentTypes() {}

    public static void init() {}

    private static <T extends ItemComponent> ItemComponentType<T> register(String id, ItemComponentType<T> type) {
        return Registry.register(ITEM_COMPONENT_TYPE, id, type);
    }
}
