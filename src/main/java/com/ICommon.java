package com;

/**
 * An ICommon interface used by both the
 * server and the client to get the host
 * and the port information
 */
public interface ICommon {
    /**
     * The port to connect to, to establish
     * client-server connection.
     */
    int PORT = 1234;
    /**
     * The host to connect to, to establish
     * client-server connection.
     */
    String HOST = "localhost";
}
