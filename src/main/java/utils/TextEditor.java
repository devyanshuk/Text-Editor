package utils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.Color;
import java.awt.event.*;

import static utils.FileMenuBarTypes.*;

public class TextEditor
        extends JFrame
        implements ITextEditor {

    private JPanel _panel;
    private JMenuBar _menuBar;
    private JTextArea _textArea;
    private JScrollBar _scrollBar;

    private boolean contentChanged;

    public TextEditor(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(_panel);
        this.initMenuBar();
        var caret = (DefaultCaret)_textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.pack();
        setSize(800,600);
        addComponentListeners();
    }

    private void initMenuBar() {
        _menuBar = new JMenuBar();
        var menu = new JMenu(File.toString());
        var newM = new JMenuItem(New.toString());
        var saveM = new JMenuItem(Save.toString());
        var openM = new JMenuItem(Open.toString());
        newM.addActionListener((e) -> initEditor());
        saveM.addActionListener((e) -> saveProgress());
        openM.addActionListener((e) -> openNew());
        menu.add(newM);
        menu.addSeparator();
        menu.add(saveM);
        menu.addSeparator();
        menu.add(openM);
        _menuBar.add(menu);
        super.setJMenuBar(_menuBar);
    }


    public void initEditor() {
        if (contentChanged) {
            Object[] options = {"Yes","No"};
            int n = JOptionPane.showOptionDialog(
                    this,
                    "File not saved. Would you like to save it?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (n == 0) {
                saveProgress();
            }
        }
        _textArea.setText("");
    }

    public void saveProgress() {
        System.out.println("SAVE PROGRESS");
        contentChanged = false;
    }

    public void contentChanged() {
        if (_textArea.getText() != "") {
            contentChanged = true;
        }
        _textArea.setCaretPosition(_textArea.getDocument().getLength());
    }

    public void openNew() {
        System.out.println("OPEN NEW");
    }

    private void addComponentListeners() {
        _textArea.addMouseWheelListener(this);
        _textArea.addKeyListener(this);
        _scrollBar.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                _scrollBar.setBackground(Color.getColor("#929298"));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("MOUSE MOVED");
                _scrollBar.setBackground(Color.getColor("#929298"));
            }
        });

        _scrollBar.addAdjustmentListener(e ->
                e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("MOUSE WHEEL MOVED");
    }

    public static void main(String[] args) {
        var frame = new TextEditor("Title");
        frame.setVisible(true);
    }
}
