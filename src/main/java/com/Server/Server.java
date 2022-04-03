package com.Server;

import com.Client.IClient;
import com.ICommon;
import utils.editor.TextEditor;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class Server
        implements IServer, ICommon {

    private ServerSocket _server;
    private ArrayList<IClient> _clients;

    public Server(ServerSocket server) {
        _server = server;
    }

    public void run() {
        int id = 0;
        while (!_server.isClosed()) {
            try {
                assert _server != null;
                var clientSocket = _server.accept();
                System.out.println("A new client has connected");
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
