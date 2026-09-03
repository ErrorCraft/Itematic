package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Optional;

public record ActionEntry(Action<?> action, Optional<LootItemCondition> requirements) {
    public static final Codec<ActionEntry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.fieldOf("action").forGetter(ActionEntry::action),
        LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirements").forGetter(ActionEntry::requirements)
    ).apply(instance, ActionEntry::new));
    public static final Codec<Holder<ActionEntry>> CODEC = RegistryFileCodec.create(ItematicRegistries.ACTION, DIRECT_CODEC);
    public static final Codec<HolderSet<ActionEntry>> LIST_CODEC = RegistryCodecs.homogeneousList(ItematicRegistries.ACTION, DIRECT_CODEC, true);

    public static ActionEntry of(Action<?> action) {
        return new ActionEntry(action, Optional.empty());
    }

    public static ActionEntry of(SequenceHandler.Builder<?, ?> builder) {
        return new ActionEntry(SequenceAction.of(builder), Optional.empty());
    }

    public static ActionEntry of(LootItemCondition.Builder requirements, Action<?> action) {
        return new ActionEntry(action, Optional.of(requirements.build()));
    }

    public static ActionEntry of(LootItemCondition.Builder requirements, SequenceHandler.Builder<?, ?> builder) {
        return new ActionEntry(SequenceAction.of(builder), Optional.of(requirements.build()));
    }

    public Optional<Boolean> execute(ActionContext context) {
        if (!this.test(context)) {
            return Optional.empty();
        }

        return Optional.of(this.action.execute(context));
    }

    private boolean test(ActionContext context) {
        if (this.requirements.isEmpty()) {
            return true;
        }

        LootItemCondition requirements = this.requirements.get();
        LootContext lootContext = context.lootContext();
        if (lootContext == null) {
            return false;
        }

        lootContext.pushVisitedElement(LootContext.createVisitedEntry(requirements));
        return requirements.test(lootContext);
    }
}
