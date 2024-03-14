package botanical.harmony.papaya;

public interface EventHandler<T extends Event> {
  void handle(Object sender, T event);
}
