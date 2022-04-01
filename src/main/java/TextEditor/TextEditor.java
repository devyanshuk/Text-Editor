package TextEditor;

import javax.swing.*;

public class TextEditor extends JFrame {

    private JPanel _panel;

    public TextEditor(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(_panel);
        this.pack();
    }

    public static void main(String[] args) {
        var frame = new TextEditor("Title");
        frame.setVisible(true);
    }
}
