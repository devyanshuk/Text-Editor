package com.Client;

import com.ICommon;
import utils.editor.ITextEditor;
import utils.editor.TextEditor;

import java.io.*;
import java.net.*;

public class Client implements IClient, ICommon {

    private Socket _socket;
    private BufferedReader _reader;
    private BufferedWriter _writer;
    private ITextEditor _textEditor;

    public Client(Socket socket) {
        try(var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))
        {
            _socket = socket;
            _reader = reader;
            _writer = writer;
            _textEditor = new TextEditor();
        }
        catch(IOException e) {
            close();
        }
    }

    private void run() {
        new Thread(() -> {
            while(_socket.isConnected()) {
                try {
                    _textEditor.changeText(_reader.lines().toString());
                    if (_textEditor.isChanged()) {
                        _writer.write(_textEditor.getText());
                        _writer.flush();
                    }
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
