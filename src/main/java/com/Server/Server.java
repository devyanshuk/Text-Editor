package com.Server;

import com.ICommon;
import java.io.IOException;
import java.net.*;


public class Server
        implements IServer, ICommon {

    private final ServerSocket _server;

    public Server(ServerSocket server) {
        _server = server;
    }

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

    private void closeServer() {
        try {
            if (_server != null) {
                _server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        var server = new Server(new ServerSocket(PORT));
        server.run();
    }
}
