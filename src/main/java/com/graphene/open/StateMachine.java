package com.graphene.open;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StateMachine<State extends Enum<State>, Event extends Enum<Event>, Context> {

    private final List<Transition<State, Event, Context>> transitions = new ArrayList<>();
    private State currentState;
    private final Context context;

    public StateMachine(State initialState, Context context) {
        this.currentState = initialState;
        this.context = context;
    }

    public void addTransition(Transition<State, Event, Context> transition) {
        transitions.add(transition);
    }

    public boolean fire(Event event) {
        return transitions.stream()
                .filter(transition -> transition.getFromState() == currentState)
                .filter(transition -> transition.getEvent() == event)
                .filter(transition ->
                        Optional.ofNullable(transition.getGuard())
                                .map(guard -> guard.test(context)).orElse(true)
                )
                .findFirst()
                .map(transition -> {
                    Optional.ofNullable(transition.getAction()).ifPresent(action -> action.accept(context));
                    currentState = transition.getToState();
                    return true;
                })
                .orElse(false);
    }

    public State getCurrentState() {
        return currentState;
    }
}
