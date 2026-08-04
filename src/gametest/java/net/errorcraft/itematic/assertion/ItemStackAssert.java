package net.errorcraft.itematic.assertion;

import net.errorcraft.itematic.mixin.enchantment.EnchantmentHelperAccessor;
import net.errorcraft.itematic.util.TestUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemStackAssert {
    private final GameTestHelper helper;
    private final ItemStack stack;
    private final String name;

    ItemStackAssert(GameTestHelper helper, ItemStack stack) {
        this(helper, stack, "item stack");
    }

    ItemStackAssert(GameTestHelper helper, ItemStack stack, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.stack = Assert.isNotNull(this.helper, stack, name);
        this.name = Objects.requireNonNull(name);
    }

    public ItemStackAssert is(ResourceKey<Item> id) {
        if (this.stack.itematic$isOf(id)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_type",
            this.name,
            id.identifier(),
            this.stack.getItemHolder().getRegisteredName()
        );
    }

    public ItemStackAssert isEmpty() {
        if (this.stack.isEmpty()) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.item_stack.expected_empty",
            this.name,
            this.stack.getItemHolder().getRegisteredName()
        );
    }

    public ItemStackAssert isNotEmpty() {
        if (!this.stack.isEmpty()) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.item_stack.expected_not_empty",
            this.name
        );
    }

    public ItemStackAssert hasCount(Consumer<IntsAssert> countAssertion) {
        countAssertion.accept(Assert.ints(this.helper, this.stack.getCount(), "item stack count"));
        return this;
    }

    public ItemStackAssert isDamaged() {
        if (this.stack.isDamaged()) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.item_stack.expected_damaged",
            this.name
        );
    }

    public ItemStackAssert isNotDamaged() {
        if (!this.stack.isDamaged()) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.item_stack.expected_not_damaged",
            this.name
        );
    }

    public <T> ItemStackAssert hasComponent(DataComponentType<T> type) {
        if (this.stack.has(type)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.item_stack.expected_data_component",
            this.name,
            type
        );
    }

    public <T> ItemStackAssert hasComponent(DataComponentType<T> type, Consumer<T> assertion) {
        assertion.accept(TestUtil.getDataComponent(this.helper, this.stack, type));
        return this;
    }

    public <T> ItemStackAssert doesNotHaveComponent(DataComponentType<T> type) {
        if (!this.stack.has(type)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.item_stack.did_not_expect_data_component",
            this.name,
            type
        );
    }

    public ItemStackAssert hasPotion(Holder<Potion> expected) {
        return this.hasComponent(DataComponents.POTION_CONTENTS, potionContents -> {
            Holder<Potion> potion = potionContents.potion()
                .orElseThrow(() -> this.helper.assertionException(
                    "test.error.item_stack.expected_potion",
                    this.name,
                    expected.getRegisteredName()
                ));
            if (expected != potion) {
                throw this.helper.assertionException(
                    "test.error.item_stack.expected_other_potion",
                    this.name,
                    expected.getRegisteredName(),
                    potion.getRegisteredName()
                );
            }
        });
    }

    public ItemStackAssert hasEnchantments() {
        return this.hasComponent(EnchantmentHelperAccessor.getComponentType(this.stack), enchantments -> {
            if (enchantments.isEmpty()) {
                throw this.helper.assertionException(
                    "test.error.item_stack.expected_enchantments",
                    this.name
                );
            }
        });
    }

    public ItemStackAssert hasNoEnchantments() {
        return this.hasComponent(EnchantmentHelperAccessor.getComponentType(this.stack), enchantments -> {
            if (!enchantments.isEmpty()) {
                throw this.helper.assertionException(
                    "test.error.item_stack.expected_no_enchantments",
                    this.name
                );
            }
        });
    }

    @SafeVarargs
    public final ItemStackAssert hasEnchantments(ResourceKey<Enchantment>... expected) {
        return this.hasComponent(EnchantmentHelperAccessor.getComponentType(this.stack), enchantments -> {
            Set<ResourceKey<Enchantment>> remaining = new HashSet<>(List.of(expected));
            for (Holder<Enchantment> enchantment : enchantments.keySet()) {
                enchantment.unwrapKey().ifPresent(remaining::remove);
            }

            if (remaining.isEmpty()) {
                return;
            }

            throw this.helper.assertionException(
                "test.error.item_stack.expected_specified_enchantments",
                this.name,
                remaining.stream()
                    .map(ResourceKey::identifier)
                    .sorted()
                    .map(Identifier::toString)
                    .collect(Collectors.joining(", "))
            );
        });
    }
}
