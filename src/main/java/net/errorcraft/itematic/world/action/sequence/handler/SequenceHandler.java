package net.errorcraft.itematic.world.action.sequence.handler;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public interface SequenceHandler<T extends SequenceHandler<T>> {
    MapCodec<SequenceHandler<?>> CODEC = ItematicBuiltInRegistries.SEQUENCE_HANDLER_TYPE.byNameCodec().dispatchMap("handler", SequenceHandler::type, SequenceHandlerType::codec);

    SequenceHandlerType<T> type();
    boolean handle(ActionContext context);
    Iterable<Holder<ActionEntry>> iterateEntries();

    interface Builder<T extends SequenceHandler<T>, S extends Builder<T, S>> {
        T build();
        default S add(Builder<?, ?> builder) {
            return this.add(SequenceAction.of(builder));
        }
        default S add(LootItemCondition.Builder requirements, Builder<?, ?> builder) {
            return this.add(requirements, SequenceAction.of(builder));
        }
        default S add(SequenceHandler<?> handler) {
            return this.add(SequenceAction.of(handler));
        }
        default S add(LootItemCondition.Builder requirements, SequenceHandler<?> handler) {
            return this.add(requirements, SequenceAction.of(handler));
        }
        default S add(Action<?> action) {
            return this.add(ActionEntry.of(action));
        }
        default S add(LootItemCondition.Builder requirements, Action<?> action) {
            return this.add(ActionEntry.of(requirements, action));
        }
        default S add(ActionEntry entry) {
            return this.add(Holder.direct(entry));
        }
        S add(Holder<ActionEntry> entry);
    }
}
