package com.Client;

/**
 * An IClient interface that has the run()
 * method defined that continuously runs and
 * performs some specific action.
 */
public interface IClient extends Runnable {
    /**
     * Used to loop continuously (in a separate thread)
     * and perform some action(s)
     */
    void run();
}
