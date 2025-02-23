package com.graphene.open;

import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class StateMachineTest {

    private Context context;
    private StateMachine<StateSample, EventSample, Context> stateMachine;

    @Before
    public void setUp() {
        // Initialize the data model and state machine
        context = new Context();
        context.setCurrentState(StateSample.INCOMPLETE);

        StateMachineBuilder<StateSample, EventSample, Context> stateMachineBuilder =
                new StateMachineBuilder<>(StateSample.INCOMPLETE, context);

        // Define transitions
        stateMachineBuilder.externalTransition()
                .from(StateSample.INCOMPLETE)
                .to(StateSample.PENDING)
                .on(EventSample.SUBMIT)
                .when(model -> model.getRequiredField1() != null && model.getRequiredField2() != null)
                .perform(model -> {
                    model.setCurrentState(StateSample.PENDING);
                    System.out.println("Transitioned from INCOMPLETE to PENDING");
                })
                .buildTransition();

        stateMachineBuilder.externalTransition()
                .from(StateSample.PENDING)
                .to(StateSample.COMPLETE)
                .on(EventSample.APPROVE)
                .when(Context::isApproved)
                .perform(model -> {
                    model.setCurrentState(StateSample.COMPLETE);
                    System.out.println("Transitioned from PENDING to COMPLETE");
                })
                .buildTransition();

        stateMachineBuilder.externalTransition()
                .from(StateSample.PENDING)
                .to(StateSample.INCOMPLETE)
                .on(EventSample.REJECT)
                .perform(model -> {
                    model.setApproved(false);
                    model.setCurrentState(StateSample.INCOMPLETE);
                    System.out.println("Transitioned from PENDING to INCOMPLETE");
                })
                .buildTransition();

        stateMachineBuilder.externalTransition()
                .from(StateSample.COMPLETE)
                .to(StateSample.ARCHIVED)
                .on(EventSample.ARCHIVE)
                .perform(model -> {
                    model.setCurrentState(StateSample.ARCHIVED);
                    System.out.println("Transitioned from COMPLETE to ARCHIVED");
                })
                .buildTransition();

        stateMachine = stateMachineBuilder.build();
    }

    @Test
    public void testSubmitEventSuccess() {
        // Set up the data model
        context.setRequiredField1("Field1");
        context.setRequiredField2("Field2");

        // Fire the SUBMIT event
        assertThat(stateMachine.fire(EventSample.SUBMIT)).isTrue();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.PENDING);
    }

    @Test
    public void testSubmitEventFailure() {
        // Do not set required fields
        context.setRequiredField1(null);
        context.setRequiredField2(null);

        // Fire the SUBMIT event
        assertThat(stateMachine.fire(EventSample.SUBMIT)).isFalse();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.INCOMPLETE);
    }

    @Test
    public void testSubmitEventFailure2() {
        // Do not set required fields
        context.setRequiredField1("Field1");
        context.setRequiredField2(null);

        // Fire the SUBMIT event
        assertThat(stateMachine.fire(EventSample.SUBMIT)).isFalse();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.INCOMPLETE);
    }

    @Test
    public void testApproveEventSuccess() {
        // Transition to PENDING first
        context.setRequiredField1("Field1");
        context.setRequiredField2("Field2");
        stateMachine.fire(EventSample.SUBMIT);

        // Set isApproved to true
        context.setApproved(true);

        // Fire the APPROVE event
        assertThat(stateMachine.fire(EventSample.APPROVE)).isTrue();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.COMPLETE);
    }

    @Test
    public void testApproveEventFailure() {
        // Transition to PENDING first
        context.setRequiredField1("Field1");
        context.setRequiredField2("Field2");
        stateMachine.fire(EventSample.SUBMIT);

        // Do not set isApproved
        context.setApproved(false);

        // Fire the APPROVE event
        assertThat(stateMachine.fire(EventSample.APPROVE)).isFalse();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.PENDING);
    }

    @Test
    public void testRejectEvent() {
        // Transition to PENDING first
        context.setRequiredField1("Field1");
        context.setRequiredField2("Field2");
        stateMachine.fire(EventSample.SUBMIT);

        // Fire the REJECT event
        assertThat(stateMachine.fire(EventSample.REJECT)).isTrue();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.INCOMPLETE);
        assertThat(context.isApproved()).isFalse();
    }

    @Test
    public void testArchiveEvent() {
        // Transition to COMPLETE first
        context.setRequiredField1("Field1");
        context.setRequiredField2("Field2");
        stateMachine.fire(EventSample.SUBMIT);
        context.setApproved(true);
        stateMachine.fire(EventSample.APPROVE);

        // Fire the ARCHIVE event
        assertThat(stateMachine.fire(EventSample.ARCHIVE)).isTrue();
        assertThat(context.getCurrentState()).isEqualTo(StateSample.ARCHIVED);
    }
}