package jnotepadpp;

import jnotepadpp.localization.ILocalizationProvider;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class defines tab components which saves file that is opened inside of it.
 * @author Dario Vidas
 */
public class TabComponent extends JPanel implements DocumentListener {

	private static final long serialVersionUID = -5727015594528397658L;
	private static final int FONTSIZE = 12;

	/** New document counter. */
	private static int docCount = 1;

	/** File to read from, or write to. Current active file in text area. */
	private File file;

	/** Dirty bit. Saying if text has changed since last save. */
	private boolean dirty = false;

	/** Tab's text area. */
	private JTextArea textArea = null;

	/** Title of the main frame. Contains path to file. */
	private String mainTitle;

	/** Label for tab title. Contains file name. */
	private JLabel title = new JLabel();

	/** Localization provider. */
	private ILocalizationProvider provider;

	/**
	 * Constructor with one argument. If tab has no file (it is blank/new) then file
	 * is <code>null</code>.
	 * @param file file that is opened inside of tab
	 * @param provider localization provider
	 */
	public TabComponent(File file, ILocalizationProvider provider) {
		super(new BorderLayout());
		this.file = file;
		this.provider = provider;

		//initial text area, main title and tab title contents
		if (file == null) {		//blank/new
			textArea = new JTextArea();
			title.setText(provider.getString("newDocumentTag") + docCount);
			incDocCount();
			mainTitle = title.getText();
		} else {				//existing
			textArea = new JTextArea(readFile());
			title.setText(file.getName());
			mainTitle = file.getAbsolutePath();
		}

		Font font = new Font("Lucida Console", Font.PLAIN, FONTSIZE);
		textArea.setFont(font);
		textArea.getDocument().addDocumentListener(this);

		//overriding system hotkeys
		InputMap inMap = textArea.getInputMap();
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "clearV");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "clearC");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), "clearX");

		JScrollPane pane = new JScrollPane(textArea);
		add(pane);
	}

	/**
	 * Increases doc count. To avoid findbugs bullshit.
	 */
	private static void incDocCount() {
		docCount++;
	}

	/**
	 * Helper method that reads file content into string.
	 * @return string
	 */
	private String readFile() {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file),
				StandardCharsets.UTF_8))) {

			final int buffSize = 4096;
			char[] buffer = new char[buffSize];
			int len;
			while ((len = reader.read(buffer)) != -1) {
				builder.append(buffer, 0, len);
			}
		} catch (IOException e) {
			showMessageDialog(
					getTopLevelAncestor(),
					provider.getString("readingFileContentError"),
					provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
		}
		return builder.toString();
	}

	/**
	 * Method saves current text area content into file from which it read. This
	 * method also uses dirty bit for saving. This method is used in "save" action.
	 */
	void saveFile() {
		if (dirty) {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file),
					StandardCharsets.UTF_8))) {

				writer.write(textArea.getText());
			} catch (IOException ignorable) {
				showMessageDialog(
						getTopLevelAncestor(),
						provider.getString("writingFileContentError"),
						provider.getString("error"),
						JOptionPane.ERROR_MESSAGE);
			}

			dirty = false;
			this.title.setText(file.getName());
			this.mainTitle = file.getAbsolutePath();
		}
	}

	/**
	 * Method saves current text area content into file given in argument. This
	 * method does not use dirty bit. Method is used in "save as" action.
	 * @param file file
	 */
	void saveFile(File file) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file),
				StandardCharsets.UTF_8))) {

			writer.write(textArea.getText());
		} catch (IOException ignorable) {
			showMessageDialog(
					getTopLevelAncestor(),
					provider.getString("writingFileContentError"),
					provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
		}

		this.file = file;
		dirty = false;
		this.title.setText(file.getName());
		this.mainTitle = file.getAbsolutePath();
	}

	/**
	 * Method calculates statistics and returns them as string. Method calculates
	 * number of lines, character count and non-blank character count.
	 * @return string representing statistics
	 */
	String getStatistics() {
		String text = textArea.getText();
		int rowCount = 1;
		final int allChars = text.length();
		int noWhitespace = 0;

		for (int i = 0; i < allChars; i++) {
			char c = text.charAt(i);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ' && c != '\0') {
				noWhitespace++;
			}
			if (c == '\n' || c == '\r') {
				rowCount++;
			}
		}

		return String.format(provider.getString("statsStringFormat"), allChars, noWhitespace, rowCount);
	}

	/**
	 * Checks if file has been changed.
	 * @return <code>true</code> if it has, <code>false</code> otherwise
	 */
	boolean isChanged() {
		return dirty;
	}

	/**
	 * Checks if this tab has not yet been saved for the first time.
	 * @return <code>true</code> if it's new, <code>false</code> otherwise
	 */
	boolean isNew() {
		return file == null;
	}

	/**
	 * Method gets tab title.
	 * @return tab title
	 */
	String getMainTitle() {
		return mainTitle;
	}

	/**
	 * Method gets <code>JLabel</code> contained in tab.
	 * @return tab label
	 */
	JLabel getTabLabel() {
		return title;
	}

	/**
	 * Cuts selected text from text area into system clipboard.
	 */
	void cut() {
		textArea.cut();
	}

	/**
	 * Copies selected text from text area into system clipboard.
	 */
	void copy() {
		textArea.copy();
	}

	/**
	 * Pastes text from system clipboard to text area.
	 */
	void paste() {
		textArea.paste();
	}

	/*
	 * next few methods are used to set dirty bit which is later used as saving
	 * condition, just like real notepad :D
	 */

	@Override
	public void insertUpdate(DocumentEvent e) {
		changed();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changed();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		changed();
	}

	/**
	 * If text has been changed, sets <code>dirty</code> to <code>true</code>.
	 */
	private void changed() {
		if (!dirty) {
			title.setText("*" + title.getText());
			mainTitle = "*" + mainTitle;
			((JnotepadPP) getTopLevelAncestor()).changeTitle();
			dirty = true;
		}
	}

}
