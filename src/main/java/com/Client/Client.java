package com.Client;

import com.ICommon;
import utils.editor.TextEditor;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;

/**
 * A child of the text-editor class that has some specific
 * methods implemented in a different way.
 * It continuously checks if the text has been changed, and if so,
 * it updates the text field.
 */
public class Client
        extends TextEditor
        implements IClient, ICommon {

    /**
     * A socket instance to help
     * communicate to the server
     */
    private Socket _socket;
    /**
     * Input stream to read data
     * from other clients
     */
    private ObjectInputStream _reader;
    /**
     * Output stream to send data
     * to other clients
     */
    private ObjectOutputStream _writer;

    /**
     * Constructor of the client class
     * @param socket A socket instance that contains
     *               the host and the port informations.
     */
    public Client(Socket socket) {
        try
        {
            _writer = new ObjectOutputStream(socket.getOutputStream());
            _writer.flush();
            _reader = new ObjectInputStream(socket.getInputStream());
            _socket = socket;
        }
        catch(IOException e) {
            close();
        }
    }

    /**
     * Loop until there is a socket connection, read
     * data from other clients and update the text
     * field accordingly.
     */
    @Override
    public void run() {
        while (_socket.isConnected()) {
            try {
                var newText= _reader.readUTF();
                this.changeText(newText);
            } catch (IOException e) {
                close();
                break;
            }
        }
    }

    /**
     * When a client is disconnected, dispose the
     * reader, writer and the socket.
     */
    private void close() {
        try {
            if (_writer != null) _writer.close();
            if (_reader != null) _reader.close();
            _socket.close();
        }
        catch(IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * When the other clients release their keys, broadcast
     * and update the text area of all other clients.
     *
     * @param e Args supplied that provides detail about
     *          the key released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        try {
            _writer.writeUTF(this.getText());
            _writer.flush();
        }
        catch(IOException ex) {
            close();
        }
    }

    /**
     * Entry point of the client class
     * @param args args supplied from the terminal
     */
    public static void main(String[] args) {
        try {
            var client = new Thread(new Client(new Socket(HOST, PORT)));
            client.run();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
