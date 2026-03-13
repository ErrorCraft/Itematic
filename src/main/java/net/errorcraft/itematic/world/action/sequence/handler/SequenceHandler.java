package net.errorcraft.itematic.world.action.sequence.handler;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionRequirements;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.registry.entry.RegistryEntry;

public interface SequenceHandler<T extends SequenceHandler<T>> {
    MapCodec<SequenceHandler<?>> CODEC = ItematicRegistries.SEQUENCE_HANDLER_TYPE.getCodec().dispatchMap("handler", SequenceHandler::type, SequenceHandlerType::codec);

    SequenceHandlerType<T> type();
    boolean handle(NewActionContext context);
    Iterable<RegistryEntry<ActionEntry>> iterateEntries();

    interface Builder<T extends SequenceHandler<T>, S extends Builder<T, S>> {
        T build();
        default S add(Builder<?, ?> builder) {
            return this.add(SequenceAction.of(builder));
        }
        default S add(ActionRequirements requirements, Builder<?, ?> builder) {
            return this.add(requirements, SequenceAction.of(builder));
        }
        default S add(SequenceHandler<?> handler) {
            return this.add(SequenceAction.of(handler));
        }
        default S add(ActionRequirements requirements, SequenceHandler<?> handler) {
            return this.add(requirements, SequenceAction.of(handler));
        }
        default S add(Action<?> action) {
            return this.add(ActionEntry.of(action));
        }
        default S add(ActionRequirements requirements, Action<?> action) {
            return this.add(ActionEntry.of(requirements, action));
        }
        default S add(ActionEntry entry) {
            return this.add(RegistryEntry.of(entry));
        }
        S add(RegistryEntry<ActionEntry> entry);
    }
}
