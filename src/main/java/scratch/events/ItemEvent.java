package scratch.events;

public class ItemEvent {
    public enum Event {CREATED, UPDATED, DELETED}

    private final Item item;
    private final Event event;

    public ItemEvent(Item item, Event event) {
        this.item = item;
        this.event = event;
    }

    public Item getItem() {
        return item;
    }

    public Event getEvent() {
        return event;
    }
}
