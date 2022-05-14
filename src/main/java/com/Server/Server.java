package com.Server;

import com.ICommon;
import java.io.IOException;
import java.net.*;

/**
 * The server class that handles all clients.
 */
public class Server
        implements IServer, ICommon {

    /**
     * The server socket instance that contains
     * the host and the port informations.
     */
    private final ServerSocket _server;

    /**
     * The server constructor that initializes
     * the server socket information.
     * @param server Server socket information
     *               (host, port)
     */
    public Server(ServerSocket server) {
        _server = server;
    }

    /**
     * The run() method that gets run in a
     * separate thread and handles the clients
     * while the server is connected.
     */
    public void run() {
        int id = 0;
        while (!_server.isClosed()) {
            try {
                var clientSocket = _server.accept();
                System.out.println("A new client has connected. Id = " + id);
                var clientHandler = new ClientHandler(clientSocket, id++);
                var thread = new Thread(clientHandler);
                thread.start();
            }
            catch (IOException e) {
                closeServer();
                break;
            }
        }
    }

    /**
     * Dispose the server socket instance
     * and close the server when called.
     */
    private void closeServer() {
        try {
            if (_server != null) {
                _server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of the server class
     * @param args args provided in console.
     * @throws IOException when the server socket instance could not
     * be created.
     */
    public static void main(String[] args) throws IOException {
        var server = new Server(new ServerSocket(PORT));
        server.run();
    }
}
