# State Machine Implementation in Java

This project provides a custom state machine implementation in Java, 
designed to manage complex workflows with states, events, guards, and actions. 
The state machine is built from scratch without relying on external libraries and features a fluent API for defining transitions.

## Table of Contents

1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
4. [Usage](#usage)
   - [Defining States and Events](#defining-states-and-events)
   - [Building the State Machine](#building-the-state-machine)
   - [Firing Events](#firing-events)
5. [Runbook](#runbook)
   - [Step-by-Step Guide](#step-by-step-guide)
   - [Example Workflow](#example-workflow)
6. [Contributing](#contributing)
7. [License](#license)

## Overview

The state machine allows you to:

- Define states and events.
- Create transitions between states triggered by events.
- Add guards to validate conditions before allowing transitions.
- Define actions to perform operations during transitions.
- Use a fluent API for easy configuration.

## Key Features

- **Fluent API**: Chain methods like `.from()`, `.to()`, `.on()`, `.when()`, and `.perform()`.
- **Guards**: Validate conditions before allowing a transition.
- **Actions**: Perform operations during transitions.
- **Custom Context**: Pass dynamic data with events using a context object.
- **Lightweight**: No external dependencies.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later.
- Apache Maven installed.

### Installation

Clone the repository:

```sh
git clone https://github.com/Anas2001/bstate-machine.git
```

Navigate to the project directory:

```sh
cd state-machine
```

Build the project using Maven:

```sh
mvn clean install
```

## Usage

### Defining States and Events

Define your states and events for an order processing system using enums:

```java
public enum OrderState {
    ORDER_PLACED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED
}

public enum OrderEvent {
    PROCESS_ORDER,
    SHIP_ORDER,
    DELIVER_ORDER,
    CANCEL_ORDER
}
```

### Building the State Machine

Use the `StateMachineBuilder` to define transitions:

```java
StateMachineBuilder<OrderState, OrderEvent, OrderContext> builder = new StateMachineBuilder<>(OrderState.ORDER_PLACED, new OrderContext());

builder.externalTransition()
    .from(OrderState.ORDER_PLACED)
    .to(OrderState.PROCESSING)
    .on(OrderEvent.PROCESS_ORDER)
    .perform(context -> System.out.println("Order is now being processed"))
    .buildTransition();

builder.externalTransition()
    .from(OrderState.PROCESSING)
    .to(OrderState.SHIPPED)
    .on(OrderEvent.SHIP_ORDER)
    .perform(context -> System.out.println("Order has been shipped"))
    .buildTransition();

builder.externalTransition()
    .from(OrderState.SHIPPED)
    .to(OrderState.DELIVERED)
    .on(OrderEvent.DELIVER_ORDER)
    .perform(context -> System.out.println("Order has been delivered"))
    .buildTransition();

builder.externalTransition()
    .from(OrderState.ORDER_PLACED)
    .to(OrderState.CANCELED)
    .on(OrderEvent.CANCEL_ORDER)
    .perform(context -> System.out.println("Order has been canceled"))
    .buildTransition();

StateMachine<OrderState, OrderEvent, OrderContext> stateMachine = builder.build();
```

### Firing Events

Use the `StateMachine` to fire events and trigger transitions:

```java
if (stateMachine.fire(OrderEvent.PROCESS_ORDER)) {
    System.out.println("Order moved to PROCESSING state.");
}

if (stateMachine.fire(OrderEvent.SHIP_ORDER)) {
    System.out.println("Order moved to SHIPPED state.");
}

if (stateMachine.fire(OrderEvent.DELIVER_ORDER)) {
    System.out.println("Order moved to DELIVERED state.");
}
```

## Runbook

### Step-by-Step Guide

1. **Define States and Events**:
   - Create enums for states (`OrderState`) and events (`OrderEvent`).
2. **Define the Context**:
   - Create a class (`OrderContext`) to manage order-specific data.
3. **Build the State Machine**:
   - Use the `StateMachineBuilder` to define transitions with guards and actions.
4. **Fire Events**:
   - Use the `StateMachine` to trigger transitions by firing events.
5. **Test and Debug**:
   - Verify transitions through logs and test cases.

### Example Workflow

1. **Initial State**: `ORDER_PLACED`
2. **Event**: `PROCESS_ORDER`
   - **Action**: Transition to `PROCESSING` and log the status.
3. **Event**: `SHIP_ORDER`
   - **Action**: Transition to `SHIPPED` and notify the customer.
4. **Event**: `DELIVER_ORDER`
   - **Action**: Transition to `DELIVERED` and mark order as complete.
5. **Event**: `CANCEL_ORDER` (only valid from `ORDER_PLACED`)
   - **Action**: Transition to `CANCELED` and issue a refund if necessary.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Submit a pull request with a detailed description of your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

