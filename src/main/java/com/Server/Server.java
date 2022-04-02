package com.Server;

import com.Client.IClient;
import com.ICommon;
import utils.editor.TextEditor;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;


public class Server
        extends Thread
        implements IServer, ICommon {

    private ServerSocket _server;
    private ArrayList<IClient> _clients;

    public Server(ServerSocket server) {
        _server = server;
    }

    public void run() {
        while (true) {

        }
    }

    public static void main(String[] args) throws IOException {
        var server = new Server(new ServerSocket(PORT));
        server.start();
    }
}
