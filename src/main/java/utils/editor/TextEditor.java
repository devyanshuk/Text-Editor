package utils.editor;

import utils.types.FileMenuBarTypes;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.text.DefaultCaret;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The Text-Editor class that contains all necessary handlers,
 * elements. Allows user to save the progress, open a new file,
 * and work on it.
 */
public class TextEditor
        extends JFrame
        implements ITextEditor {

    /**
     * The main panel of the text editor
     * which consists of the text area,
     * the scroll pane, the menu bar.
     */
    private JPanel _panel;
    /**
     * The text area component of the
     * text editor.
     */
    private JTextArea _textArea;
    /**
     * The scroll pane component of the
     * text editor.
     */
    private JScrollPane _scrollPane;
    /**
     * A JFileChooser instance that is used
     * for displaying the files to the user
     * to choose from.
     */
    private JFileChooser _fileChooser;

    /**
     * A bool flag that indicates if the
     * file was changed since the last save.
     * Will be set to false if the user clicks
     * on the 'new' option from the menu bar.
     */
    private boolean _contentChanged;
    /**
     * The path to load data from, or to save
     * data to.
     */
    private String _filePath="";
    /**
     * the file to load data from, or to save
     * data to, inside the _filePath directory.
     */
    private String _fileName="";

    /**
     * The constructor for the text-editor class.
     * Initialize the title, the menu bar, all component listeners,
     * (mouse, keyboard, etc), the scroll pane, the size of the editor,
     * and many more.
     */
    public TextEditor() {
        super(UNNAMED);
        _textArea = new JTextArea();
        _textArea.setMinimumSize(new Dimension(150,150));
        _textArea.setLineWrap(false);
        _textArea.setBackground(new Color(-1901569));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _panel = new JPanel();
        this.setContentPane(_panel);
        _scrollPane = new JScrollPane(_textArea);
        _scrollPane.addMouseWheelListener(this);
        super.setContentPane(_scrollPane);
        this.initMenuBar();
        initFileProperties();
        var caret = (DefaultCaret)_textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.pack();
        setSize(800,600);
        addComponentListeners();
        this.setVisible(true);
    }


    /**
     * set the file path to the path the user is working
     * from, and set the filechooser location to start from
     * the directory the user is working from, since the user
     * is most likely going to save their work in the same directory
     * they open the application from.
     */
    private void initFileProperties() {
        _fileChooser = new JFileChooser(USER_DIR);
        _filePath = USER_DIR;
    }

    /**
     * Initialize the menu bar component of the text editor,
     * adding in the 'new', 'save', 'open' options along with
     * their corresponding handlers to the menu bar.
     */
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

    /**
     * Since we're only interested in the 'message', 'option' and the
     * 'type' parameters of the JOptionPane's showOptionDialog, we use a common
     * method to pass in these parameters to, to display the option dialog.
     *
     * @param message The message to display in the dialog.
     * @param option an integer designating the options available on the dialog: DEFAULT_OPTION, YES_NO_OPTION, YES_NO_CANCEL_OPTION, or OK_CANCEL_OPTION
     * @param type an integer designating the type of dialog it is (warning, error, info, etc)
     * @return n integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
     */
    private int showMessageBox(String message, int option, int type) {
        return JOptionPane.showOptionDialog(this, message,null, option, type,null,null, null);
    }

    /**
     * Initialize the text area contents of the text editor.
     * Used then the user clicks on 'new' option from the menu.
     * If some contents were changed, it prompts the user that the
     * some contents of the file hasn't been saved and asks if they
     * want to save those.
     *
     * After that, it resets the text area property, the _filepath and the
     * _fileName properties.
     */
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

    /**
     * Since we saved the progress, we set the contentChanged
     * field to false, to prevent displaying of the 'not saved'
     * warning message if the user tries to re-save it.
     */
    public void saveProgress() {
        _contentChanged = false;
    }

    /**
     * A method the checks if the text area is empty,
     * if not, we set the _contentChanged property to true.
     *
     */
    public void contentChanged() {
        if (!_textArea.getText().isBlank()) {
            _contentChanged = true;
        }
    }

    /**
     * Given a new set of sentences for the text
     * area, change the textArea field to the new
     * text
     * @param newText text to change the textarea to.
     */
    public void changeText(String newText) {
        _textArea.setText(newText);
    }

    /**
     * Returns all contents that was typed in the
     * text area.
     * @return the contents of the text area.
     */
    public String getText() {
        return _textArea.getText();
    }

    /**
     * An event handler for the 'open' button in the text editor.
     * If the user clicks the 'open' option, it prompts the user to
     * choose an existing file.
     * If the file existed, it reads all bytes from the file and sets title
     * to the file the user chose, but displays a warning message if an
     * IOException occurred.
     */
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

    /**
     * Check if the file already exists. Used by the
     * 'save' event handler to warn user that a file already
     * existed to prevent overwriting the contents by default.
     *
     * @param path path to check for file existence.
     * @return existence of the file in that path
     */
    private boolean fileIsAlreadyPresent(String path) {
        return new File(path).exists();
    }

    /**
     * If the user clicks on 'save' option in the menu bar,
     * we check if the file already exists, and if so, we
     * warn the user about this, and if the user still proceeds
     * with it, we overwrite the contents of that file specified
     * by the _filePath variable.
     *
     * If IOException occurs, then we display an error message box
     * to notify the user about this.
     */
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


    /**
     * A common function that handles the 'save', 'open'
     * options, displaying the user of the relevant dialog
     * and get the option they clicked to further handle those
     * option.
     * @param type Type of action (open, save)
     * @return a bool indicating whether the user clicked the approve
     * button in the dialog displayed by the program.
     */
    public boolean handleFileChooser(FileMenuBarTypes type) {
        _fileChooser.setVisible(true);
        _fileChooser.setAcceptAllFileFilterUsed(false);

        var res = type == FileMenuBarTypes.Open
                ? _fileChooser.showOpenDialog(this)
                : _fileChooser.showSaveDialog(this);
        return (res == JFileChooser.APPROVE_OPTION);
    }

    /**
     * Adds in the mouse and the key listeners to the
     * text area field to constantly listen for any
     * event and update the area accordingly.
     */
    private void addComponentListeners() {
        _textArea.addMouseWheelListener(this);
        _textArea.addKeyListener(this);
    }


    /**
     * An overriden method to handle the mouse wheel movement.
     * The program adjusts the scroll pane according to the speed
     * of the rotation. The default speed is 2.
     * @param e A class containing information about the mouse wheel
     *          movement. We use the wheel rotation to set the scroll pane
     *          position.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        _scrollPane.getVerticalScrollBar()
                .setValue(_scrollPane.getVerticalScrollBar().getValue() + 2 * e.getWheelRotation());
    }

    /**
     * The entry-point of the text editor program.
     * We make a new text-editor instance here, and initialize
     * all the text editor properties in the constructor.
     * @param args arguments supplied to run the text editor program.
     * @author Devyanshu
     */
    public static void main(String[] args) {
        var frame = new TextEditor();
    }
}
