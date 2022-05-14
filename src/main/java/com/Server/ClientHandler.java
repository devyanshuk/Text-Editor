package com.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * A class that contains all information of the
 * clients connected to a server, and contains
 * client entry/exit handlers.
 */
public class ClientHandler implements Runnable {

    /**
     * A socket instance that contains the port,
     * the host information.
     */
    private Socket _clientSocket;
    /**
     * An input stream that reads data broadcasted
     * by some client.
     */
    private ObjectInputStream _reader;
    /**
     * An output stream that broadcasts information
     * provided by some client
     */
    private ObjectOutputStream _writer;
    /**
     * An integer 0..n that uniquely identifies
     * client in the server.
     */
    private int _id;

    /**
     * A static array that contains the socket, id, input
     * and output information of all clients.
     */
    private static ArrayList<ClientHandler> _clients;


    /**
     * Since the array is static, we use a static
     * constructor.
     */
    static {
            _clients = new ArrayList<>();
    }

    /**
     * The ClientHandler constructor. It initializes the
     * writer, reader, id and updates the clients array.
     * @param clientSocket A socket instance that contains the host, port, etc
     * @param id Unique id of a client on the server.
     */
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

    /**
     * When this method is called, remove this client from
     * the server, close the reader and writer, and the
     * client socket.
     */
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

    /**
     * When a client sends a 'message', i.e. when the
     * client types in new texts in their editor, broadcast
     * this information to all other clients.
     * @param message Message to be broadcasted
     */
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

    /**
     * Remove this instance from the _clients list and display
     * that information in the server.
     */
    private void removeClient() {
        _clients.remove(this);
        System.out.println("Client with id " + _id + " has left");
    }

    /**
     * While there is a socket connection, read messages from
     * a client and broadcast the message to other clients.
     */
    @Override
    public void run() {
        while(_clientSocket.isConnected()) {
            try {
                var newText = _reader.readUTF();
                broadcast(newText);
            } catch(Exception e) {
                close();
                return;
            }
        }
    }
}
