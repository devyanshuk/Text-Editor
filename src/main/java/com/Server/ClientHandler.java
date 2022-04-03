package com.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket _clientSocket;
    private ObjectInputStream _reader;
    private ObjectOutputStream _writer;
    private int _id;

    private static ArrayList<ClientHandler> _clients;


    static {
            _clients = new ArrayList<>();
    }

    public ClientHandler(Socket clientSocket, int id) {
        try
        {
            _writer = new ObjectOutputStream(clientSocket.getOutputStream());
             _writer.flush();
            _reader = new ObjectInputStream(clientSocket.getInputStream());
            _clientSocket = clientSocket;
            _id = id;
            _clients.add(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        removeClient();
        try {
            if (_reader != null) _reader.close();
            if (_writer != null) _writer.close();
            if (_clientSocket != null) _clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (var client : _clients) {
            try {
                if (this._id != client._id) {
                    client._writer.writeUTF(message);
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
        System.out.println("Client with id " + _id + " has left");
    }

    @Override
    public void run() {
        while(_clientSocket.isConnected()) {
            try {
                var newText = _reader.readUTF();
                broadcast(newText);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
