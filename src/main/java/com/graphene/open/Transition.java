package com.graphene.open;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Transition<State extends Enum<State>, Event extends Enum<Event>, DataModel> {
    private final State fromState;
    private final State toState;
    private final Event event;
    private final Predicate<DataModel> guard;
    private final Consumer<DataModel> action;

    public Transition(
            State fromState,
            State toState,
            Event event,
            Predicate<DataModel> guard,
            Consumer<DataModel> action
    ) {
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
        this.guard = guard;
        this.action = action;
    }

    public State getFromState() {
        return fromState;
    }

    public State getToState() {
        return toState;
    }

    public Event getEvent() {
        return event;
    }

    public Predicate<DataModel> getGuard() {
        return guard;
    }

    public Consumer<DataModel> getAction() {
        return action;
    }
}

