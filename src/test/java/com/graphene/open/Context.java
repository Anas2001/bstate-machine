package com.graphene.open;

public class Context {
    private String requiredField1;
    private String requiredField2;
    private boolean isApproved;
    private StateSample currentState;

    // Getters and setters
    public String getRequiredField1() { return requiredField1; }
    public void setRequiredField1(String requiredField1) { this.requiredField1 = requiredField1; }

    public String getRequiredField2() { return requiredField2; }
    public void setRequiredField2(String requiredField2) { this.requiredField2 = requiredField2; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }

    public StateSample getCurrentState() { return currentState; }
    public void setCurrentState(StateSample currentState) { this.currentState = currentState; }
}
