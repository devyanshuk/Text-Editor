package utils;
import java.awt.event.*;

public interface ITextEditor
        extends MouseWheelListener, KeyListener {

    String SAVE_WARN = "File not saved. Do you want to save it?";
    String NO_FILE_WARN = "No such file found.";
    String WRITE_ERR = "Error while saving the file";
    String USER_DIR = System.getProperty("user.dir");

    void initEditor();;

    void contentChanged();

    void handleFileOpen();

    void handleFileSave();

    @Override
    default void keyTyped(KeyEvent e) {
        contentChanged();
    }

    @Override
    default void keyPressed(KeyEvent e) {
        if(e.isControlDown()  || e.isMetaDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_N -> initEditor();
                case KeyEvent.VK_O -> handleFileOpen();
                case KeyEvent.VK_S -> handleFileSave();
            }
        }
    }

    @Override
    default void keyReleased(KeyEvent e) {}
}
