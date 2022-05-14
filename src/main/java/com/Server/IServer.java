package com.Server;

/**
 * The IServer interface which has run()
 * merthod that handles all clients.
 */
public interface IServer {
    /**
     * The run() method that gets run in a
     * separate thread and handles the clients
     * while the server is connected.
     */
    void run();
}
