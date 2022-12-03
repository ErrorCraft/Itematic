package errorcraft.itematic.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.Consumer;

public class EventUtil {
    private EventUtil() {}

    public static <T> Event<Consumer<T>> consumer() {
        return EventFactory.createArrayBacked(Consumer.class, listeners -> value -> {
            for (Consumer<T> listener : listeners) {
                listener.accept(value);
            }
        });
    }
}
