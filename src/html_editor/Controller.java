package html_editor;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }

    public void exit() {
        System.exit(0);
    }

    public void init() {
        createNewDocument();
    }

    public void resetDocument() {
        if (document != null)
            document.removeUndoableEditListener(view.getUndoListener());
        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void setPlainText(String text) {
        try {
            resetDocument();
            StringReader stringReader = new StringReader(text);
            new HTMLEditorKit().read(stringReader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() {
        try {
            StringWriter stringWriter = new StringWriter();
            new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());
            return stringWriter.toString();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return null;
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public Controller(View view) {
        this.view = view;
    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument() {
        try {
            view.selectHtmlTab();
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileFilter(new HTMLFileFilter());
            int i = jFileChooser.showOpenDialog(view);
            if (i == JFileChooser.APPROVE_OPTION) {
                currentFile = jFileChooser.getSelectedFile();
                resetDocument();
                view.setTitle(currentFile.getName());
                FileReader reader = new FileReader(currentFile);
                new HTMLEditorKit().read(reader, document, 0);
                view.resetUndo();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void saveDocument() {
        try {
            view.selectHtmlTab();
            if (currentFile == null)
                saveDocumentAs();
            view.setTitle(currentFile.getName());
            FileWriter writer = new FileWriter(currentFile);
            new HTMLEditorKit().write(writer, document, 0, document.getLength());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void saveDocumentAs() {
        try {
            view.selectHtmlTab();
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileFilter(new HTMLFileFilter());
            int i = jFileChooser.showSaveDialog(view);
            if (i == JFileChooser.APPROVE_OPTION) {
                currentFile = jFileChooser.getSelectedFile();
                view.setTitle(currentFile.getName());
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
}
