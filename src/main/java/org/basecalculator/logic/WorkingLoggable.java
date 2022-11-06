package org.basecalculator.logic;

// This interface is used for any objects that can log working.
// It allows to get and set the working, without knowing the specific type of the object
public interface WorkingLoggable {
    WorkingLoggable setWorking(WorkingLogger working);
    String getWorking();
}
