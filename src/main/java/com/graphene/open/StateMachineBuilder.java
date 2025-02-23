package com.graphene.open;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class StateMachineBuilder<State extends Enum<State>, Event extends Enum<Event>, DataModel> {

    private final StateMachine<State, Event, DataModel> stateMachine;

    public StateMachineBuilder(State initialState, DataModel dataModel) {
        this.stateMachine = new StateMachine<>(initialState, dataModel);
    }

    public TransitionBuilder externalTransition() {
        return new TransitionBuilder(this);
    }

    public StateMachine<State, Event, DataModel> build() {
        return stateMachine;
    }

    public class TransitionBuilder {
        private final StateMachineBuilder<State, Event, DataModel> builder;
        private State fromState;
        private State toState;
        private Event event;
        private Predicate<DataModel> guard;
        private Consumer<DataModel> action;

        public TransitionBuilder(StateMachineBuilder<State, Event, DataModel> builder) {
            this.builder = builder;
        }

        public TransitionBuilder from(State fromState) {
            this.fromState = fromState;
            return this;
        }

        public TransitionBuilder to(State toState) {
            this.toState = toState;
            return this;
        }

        public TransitionBuilder on(Event event) {
            this.event = event;
            return this;
        }

        public TransitionBuilder when(Predicate<DataModel> guard) {
            this.guard = guard;
            return this;
        }

        public TransitionBuilder perform(Consumer<DataModel> action) {
            this.action = action;
            return this;
        }

        public StateMachineBuilder<State, Event, DataModel> buildTransition() {
            builder.stateMachine.addTransition(new Transition<>(fromState, toState, event, guard, action));
            return builder;
        }
    }
}
