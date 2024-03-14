package botanical.harmony.papaya;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublishTests {
  @Test
  void publish_event_invokes_registered_handler_with_correct_arguments() {
    AtomicBoolean invoked = new AtomicBoolean(false);
    TestEvent expectedEvent = new TestEvent(42);
    EventAggregator eventAggregator = EventAggregator.create();
    eventAggregator.register(TestEvent.class, (o, e) -> {
      boolean correctEvent = e == expectedEvent;
      invoked.set(correctEvent);
    });

    eventAggregator.publish(expectedEvent);

    assertTrue(invoked.get());
  }

  @Test
  void multiple_registered_handlers_get_invoked() {
    List<AtomicBoolean> invoked = createCheckInvokedList(5);
    TestEvent expectedEvent = new TestEvent(42);
    EventAggregator eventAggregator = EventAggregator.create();
    invoked.forEach(i -> {
      eventAggregator.register(TestEvent.class, (o, e) -> {
        boolean correctEvent = e == expectedEvent;
        i.set(correctEvent);
      });
    });

    eventAggregator.publish(expectedEvent);

    invoked.forEach(i -> {
      assertTrue(i.get());
    });
  }

  @Test
  void registering_the_same_handler_throws() {
    EventAggregator eventAggregator = EventAggregator.create();
    EventHandler<TestEvent> handler = (o,e) -> {};
    eventAggregator.register(TestEvent.class, handler);

    assertThrows(DuplicateHandlerException.class, () -> eventAggregator.register(TestEvent.class, handler));
  }

  private static List<AtomicBoolean> createCheckInvokedList(int count) {
    return Stream.iterate(1, i -> i++)
            .limit(count)
            .map(i -> new AtomicBoolean(false)).toList();
  }
}
