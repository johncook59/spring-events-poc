package scratch.events;

public class ItemCreatedEvent {
    private final Item item;

    public ItemCreatedEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
