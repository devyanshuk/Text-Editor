
package utils.editor;
import java.awt.event.*;

/**
 * An interface that has all basic functionalities
 * defined for a basic text editor
 */
public interface ITextEditor
        extends MouseWheelListener, KeyListener {

    /**
     * When a new file is created,
     * this name gets stored as the file name.
     */
    String UNNAMED = "(unnamed)";
    /**
     * Display this message to warn the user
     * that a filewas already present while trying
     * to save it.
     */
    String FILE_ALREADY_PRESENT = "File already present. Do you want to overwrite?";
    /**
     * Display this message to warn the user
     * that the file has not been saved and that
     * the contents would be lost.
     */
    String SAVE_WARN = "File not saved. Do you want to save it?";
    /**
     * Display this message to warn the user
     * that the file a user requested wasn't present.
     */
    String NO_FILE_WARN = "No such file found.";
    /**
     * Display this message to warn the user
     * that there were problems saving contents to a file.
     */
    String WRITE_ERR = "Error while saving the file";
    /**
     * The working directory of the user.
     */
    String USER_DIR = System.getProperty("user.dir");


    /**
     * Initlalize properties of the text editor class.
     */
    void initEditor();

    /**
     * Given a new text, initialize the text area to be this text
     * @param newText text to replace the text area with
     */
    void changeText(String newText);

    /**
     * Check if the contents of the text editor were changed, and
     * perform specific actions if so.
     */
    void contentChanged();

    /**
     * If the user clicks on the 'open' menu bar,
     * perform certain action.
     */
    void handleFileOpen();

    /**
     * If the user clicks on the 'save' menu bar,
     * perform certain action.
     */
    void handleFileSave();

    /**
     * When the user types in the key, call the
     * contentChanged() function to update the progress
     * @param e Args supplied that provides details of the
     *          key typed.
     */
    @Override
    default void keyTyped(KeyEvent e) {
        contentChanged();
    }

    /**
     * Handle key bind actions.
     * Eg:
     * Ctrl + N (Cmd + N on Mac) initializes the editor
     * Ctrl + O (Cmd + O on Mac) opens the selected file
     * Ctrl + S (Cmd + S on Mac) saves the progress
     * @param e Args supplied that provides details of the
     *          key bindings.
     */
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

    /**
     * Handle key released event
     * @param e Args supplied that provides detail about
     *          key released.
     */
    @Override
    default void keyReleased(KeyEvent e) {
    }
}
