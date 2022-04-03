package com.Client;

import com.ICommon;
import utils.editor.TextEditor;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;

public class Client
        extends TextEditor
        implements IClient, ICommon {

    private Socket _socket;
    private ObjectInputStream _reader;
    private ObjectOutputStream _writer;

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

    private void run() {
        new Thread(() -> {
            while (_socket.isConnected()) {
                try {
                    var newText= _reader.readUTF();
                    this.changeText(newText);
                } catch (IOException e) {
                    close();
                    break;
                }
            }
        }).start();
    }

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


    public static void main(String[] args) {
        try {
            var client = new Client(new Socket(HOST, PORT));
            client.run();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
