package utils.editor;

import utils.types.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;
import java.io.FileWriter;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;

public class TextEditor
        extends JFrame
        implements ITextEditor {

    private JPanel _panel;
    private JTextArea _textArea;
    private JScrollPane _scrollPane;
    private JFileChooser _fileChooser;


    private boolean _contentChanged;
    private String _filePath="";
    private String _fileName="";

    public TextEditor() {
        super(UNNAMED);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(_panel);
        var _scrollPane = new JScrollPane(_textArea);
        _scrollPane.addMouseWheelListener(this);
        super.setContentPane(_scrollPane);
        this.initMenuBar();
        initFileProperties();
        var caret = (DefaultCaret)_textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.pack();
        setSize(800,600);
        addComponentListeners();
    }


    private void initFileProperties() {

        _fileChooser = new JFileChooser(USER_DIR);
        _filePath = USER_DIR;
    }

    private void initMenuBar() {
        var _menuBar = new JMenuBar();
        var menu = new JMenu(FileMenuBarTypes.File.toString());
        var newM = new JMenuItem(FileMenuBarTypes.New.toString());
        var saveM = new JMenuItem(FileMenuBarTypes.Save.toString());
        var openM = new JMenuItem(FileMenuBarTypes.Open.toString());
        newM.addActionListener((e) -> initEditor());
        saveM.addActionListener((e) -> handleFileSave());
        openM.addActionListener((e) -> handleFileOpen());
        menu.add(newM);
        menu.addSeparator();
        menu.add(saveM);
        menu.addSeparator();
        menu.add(openM);
        _menuBar.add(menu);
        super.setJMenuBar(_menuBar);
    }

    private int showMessageBox(String message, int option, int type) {
        var res= JOptionPane.showOptionDialog(
                this,
                message,
                null,
                option,
                type,
                null,
                null,
                null);
        return res;
    }


    public void initEditor() {
        if (_contentChanged) {
            switch (showMessageBox(SAVE_WARN, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                case JOptionPane.CLOSED_OPTION -> { return; }
                case JOptionPane.YES_OPTION -> saveProgress();
            }
        }
        this.setTitle(UNNAMED);
        _textArea.setText("");
        _contentChanged = false;
        _filePath = "";
        _fileName = "";
    }

    public void saveProgress() {
        _contentChanged = false;
    }

    public void contentChanged() {
        if (!_textArea.getText().isBlank()) {
            _contentChanged = true;
        }
        _textArea.setCaretPosition(_textArea.getDocument().getLength());
    }

    public void handleFileOpen() {
        if (handleFileChooser(FileMenuBarTypes.Open)) {
            var file = _fileChooser.getSelectedFile();
            _filePath = file.getAbsolutePath();
            try {
                var content = new String(Files.readAllBytes(Path.of(_filePath)));
                _textArea.setText(content);
                _fileName = file.getName();
                this.setTitle(_fileName);
            }
            catch (IOException e) {
                showMessageBox(NO_FILE_WARN, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean fileIsAlreadyPresent(String path) {
        return new File(path).exists();
    }

    public void handleFileSave() {
        if (handleFileChooser(FileMenuBarTypes.Save)) {
            var file = _fileChooser.getSelectedFile();
            _filePath = file.getAbsolutePath();
            try(var writer = new FileWriter(_filePath)) {
                if (fileIsAlreadyPresent(_filePath)) {
                    var n= showMessageBox(FILE_ALREADY_PRESENT, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (n == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                writer.write(_textArea.getText());
            }
            catch (IOException e) {
                showMessageBox(WRITE_ERR, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public boolean handleFileChooser(FileMenuBarTypes type) {
        _fileChooser.setVisible(true);
        _fileChooser.setAcceptAllFileFilterUsed(false);

        var res = type == FileMenuBarTypes.Open
                ? _fileChooser.showOpenDialog(this)
                : _fileChooser.showSaveDialog(this);
        System.out.println(res);
        return (res == JFileChooser.APPROVE_OPTION);
    }

    private void addComponentListeners() {
        _textArea.addMouseWheelListener(this);
        _textArea.addKeyListener(this);
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        _scrollPane.getVerticalScrollBar()
                .setValue(_scrollPane.getVerticalScrollBar().getValue() + 2 * e.getWheelRotation());
    }

    public static void main(String[] args) {
        var frame = new TextEditor();
        frame.setVisible(true);
    }
}
