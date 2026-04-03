package net.errorcraft.itematic.assertion;

import net.errorcraft.itematic.mixin.enchantment.EnchantmentHelperAccessor;
import net.errorcraft.itematic.util.TestUtil;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemStackAssert {
    private final TestContext helper;
    private final ItemStack stack;
    private final String name;

    ItemStackAssert(TestContext helper, ItemStack stack) {
        this(helper, stack, "item stack");
    }

    ItemStackAssert(TestContext helper, ItemStack stack, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.stack = Assert.isNotNull(this.helper, stack, name);
        this.name = Objects.requireNonNull(name);
    }

    public ItemStackAssert is(RegistryKey<Item> id) {
        if (this.stack.itematic$isOf(id)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_type",
            this.name,
            id.getValue(),
            this.stack.getRegistryEntry().getIdAsString()
        );
    }

    public ItemStackAssert isEmpty() {
        if (this.stack.isEmpty()) {
            return this;
        }

        throw this.helper.createError(
            "test.error.item_stack.expected_empty",
            this.name,
            this.stack.getRegistryEntry().getIdAsString()
        );
    }

    public ItemStackAssert isNotEmpty() {
        if (!this.stack.isEmpty()) {
            return this;
        }

        throw this.helper.createError(
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

        throw this.helper.createError(
            "test.error.item_stack.damaged",
            this.name
        );
    }

    public ItemStackAssert isNotDamaged() {
        if (!this.stack.isDamaged()) {
            return this;
        }

        throw this.helper.createError(
            "test.error.item_stack.expected_not_damaged",
            this.name
        );
    }

    public <T> ItemStackAssert hasComponent(ComponentType<T> type) {
        if (this.stack.contains(type)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.item_stack.expected_data_component",
            this.name,
            type
        );
    }

    public <T> ItemStackAssert hasComponent(ComponentType<T> type, Consumer<T> assertion) {
        assertion.accept(TestUtil.getDataComponent(this.helper, this.stack, type));
        return this;
    }

    public <T> ItemStackAssert doesNotHaveComponent(ComponentType<T> type) {
        if (!this.stack.contains(type)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.item_stack.did_not_expect_data_component",
            this.name,
            type
        );
    }

    public ItemStackAssert hasPotion(RegistryEntry<Potion> expected) {
        return this.hasComponent(DataComponentTypes.POTION_CONTENTS, potionContents -> {
            RegistryEntry<Potion> potion = potionContents.potion()
                .orElseThrow(() -> this.helper.createError(
                    "test.error.item_stack.expected_potion",
                    this.name,
                    expected.getIdAsString()
                ));
            if (expected != potion) {
                throw this.helper.createError(
                    "test.error.item_stack.expected_other_potion",
                    this.name,
                    expected.getIdAsString(),
                    potion.getIdAsString()
                );
            }
        });
    }

    public ItemStackAssert hasEnchantments() {
        return this.hasComponent(EnchantmentHelperAccessor.getComponentType(this.stack), enchantments -> {
            if (enchantments.isEmpty()) {
                throw this.helper.createError(
                    "test.error.item_stack.expected_enchantments",
                    this.name
                );
            }
        });
    }

    public ItemStackAssert hasNoEnchantments() {
        return this.hasComponent(EnchantmentHelperAccessor.getComponentType(this.stack), enchantments -> {
            if (!enchantments.isEmpty()) {
                throw this.helper.createError(
                    "test.error.item_stack.expected_no_enchantments",
                    this.name
                );
            }
        });
    }

    @SafeVarargs
    public final ItemStackAssert hasEnchantments(RegistryKey<Enchantment>... expected) {
        return this.hasComponent(EnchantmentHelperAccessor.getComponentType(this.stack), enchantments -> {
            Set<RegistryKey<Enchantment>> remaining = new HashSet<>(List.of(expected));
            for (RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
                enchantment.getKey().ifPresent(remaining::remove);
            }

            if (remaining.isEmpty()) {
                return;
            }

            throw this.helper.createError(
                "test.error.item_stack.expected_specified_enchantments",
                this.name,
                remaining.stream()
                    .map(RegistryKey::getValue)
                    .sorted()
                    .map(Identifier::toString)
                    .collect(Collectors.joining(", "))
            );
        });
    }
}
