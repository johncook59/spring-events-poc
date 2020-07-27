package scratch.events;

public class ItemUpdatedEvent {
    private final Item item;

    public ItemUpdatedEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
