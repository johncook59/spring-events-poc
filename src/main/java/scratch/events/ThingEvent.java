package scratch.events;

public class ThingEvent {
    private final Thing thing;

    public ThingEvent(Thing thing) {
        this.thing = thing;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ThingEvent{");
        sb.append("thing=").append(thing);
        sb.append('}');
        return sb.toString();
    }
}
