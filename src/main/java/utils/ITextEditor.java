package utils;
import java.awt.event.*;

public interface ITextEditor
        extends MouseWheelListener, KeyListener {

    void initEditor();

    void saveProgress();

    void openNew();

    void contentChanged();

    @Override
    default void keyTyped(KeyEvent e) {
        contentChanged();
    }

    @Override
    default void keyPressed(KeyEvent e) {
        if(e.isControlDown()  || e.isMetaDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_N: {
                    initEditor();
                }
                case KeyEvent.VK_O: {
                    openNew();
                }
                case KeyEvent.VK_S: {
                    saveProgress();
                }
            }
        }
    }

    @Override
    default void keyReleased(KeyEvent e) {}
}
