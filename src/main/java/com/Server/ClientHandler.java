package com.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket _clientSocket;
    private BufferedReader _reader;
    private BufferedWriter _writer;
    private int _id;

    private static ArrayList<ClientHandler> _clients;


    static {
            _clients = new ArrayList<>();
    }

    public ClientHandler(Socket clientSocket, int id) {
        try(var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            var writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())))
        {
            _clientSocket = clientSocket;
            _id = id;
            _writer = writer;
            _reader = reader;
            _clients.add(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            _reader.close();
            _writer.close();
            _clientSocket.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (var client : _clients) {
            try {
                if (this._id != client._id) {
                    client._writer.write(message);
                    client._writer.newLine();
                    client._writer.flush();
                }
            }
            catch(IOException e) {
                close();
            }
        }
    }

    private void removeClient() {
        _clients.remove(this);
    }

    @Override
    public void run() {
        while(_clientSocket.isConnected()) {
            var newText = _reader.lines().toString();
            broadcast(newText);
        }
    }
}
