package botanical.harmony.papaya;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAggregator {
  private final Map<Class<?>, List<EventHandler>> handlers = new HashMap<>();

  public static EventAggregator create() {
   return new EventAggregator();
  }

  public <T extends Event> EventAggregator register(Class<T> eventClass, EventHandler<T> handler) {
    List<EventHandler> eventHandlers = getHandlersForEvent(eventClass);
    if (eventHandlers.contains(handler)) throw new DuplicateHandlerException();
    eventHandlers.add(handler);
    return this;
  }

  private <T extends Event> List<EventHandler> getHandlersForEvent(Class<T> eventClass) {
    if (handlers.containsKey(eventClass)) return handlers.get(eventClass);
    List<EventHandler> eventHandlers = new ArrayList<>();
    handlers.put(eventClass, eventHandlers);
    return eventHandlers;
  }

  public <T extends Event> void publish(T event) {
    Class<?> clazz = event.getClass();
    handlers.get(clazz).forEach(h -> h.handle(this, event));
  }
}

