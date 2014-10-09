package hr.fer.zemris.java.hw07.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class for creating JNotepad++.
 * @author Dario Vidas
 */
public class JnotepadPP extends JFrame {

	private static final long serialVersionUID = -2377156355243496559L;

	/** Map that holds all actions. */
	private Map<String, Action> actionMap = new HashMap<>();

	/** Extended from <code>JTabbedPane</code>. Holds all tabs. */
	private ModifiedTabbedPane tabs;

	//GUI constants
	private static final int INIT_SIZE_X = 520;
	private static final int INIT_SIZE_Y = 600;
	private static final int INIT_POS_X = 30;
	private static final int INIT_POS_Y = 30;

	/**
	 * Constructor that creates main frame and initializes GUI. Sets frame title,
	 * opening location, initial size and layout. Calls method <code>initGUI</code>.
	 */
	public JnotepadPP() {
		super();
		setTitle("JNotepad++");
		//setIconImage(new ImageIcon(this.getClass().getResource("/Notepad.png")).getImage());
		setIconImage(new ImageIcon("res/Notepad.png").getImage());
		setLocation(INIT_POS_X, INIT_POS_Y);
		setSize(INIT_SIZE_X, INIT_SIZE_Y);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		initGUI();
	}

	/**
	 * Method initializes GUI. Runs other methods for initialization. Initializes
	 * lookAndFeel, actions, menu bar, toolbar, tabbed pane and listeners.
	 * @see #initLookAndFeel()
	 * @see #actionInit()
	 * @see #initMenuBar()
	 * @see #initToolBar()
	 * @see #initTabbedPane()
	 * @see #initWindowEvent()
	 * @see #initTabChangeListener()
	 */
	private void initGUI() {
		initLookAndFeel();
		actionInit();
		initMenuBar();
		initToolBar();
		initTabbedPane();
		initWindowEvent();
		initTabChangeListener();
	}

	/**
	 * Initializes system lookAndFeel. Uses GUI specific to operating system. If
	 * error occurs while retrieving GUI, it will use standard java GUI (which looks
	 * pretty ugly).
	 */
	private void initLookAndFeel() {
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ignorable) {
			// if exception arises just use normal java look and feel
		}
	}

	/**
	 * Method initializes menu bar. Menu bar has 2 menus: File and Edit. File
	 * contains: new file, open file, save file, save file as and exit. Edit
	 * contains: cut, copy, paste and statistics. All actions are pre-initialized by
	 * other methods.
	 * @see #actionInit()
	 */
	private void initMenuBar() {
		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);

		JMenu fileOperations = new JMenu("File");
		menu.add(fileOperations);
		fileOperations.add(new JMenuItem(actionMap.get("blank")));
		fileOperations.add(new JMenuItem(actionMap.get("open")));
		fileOperations.add(new JMenuItem(actionMap.get("save")));
		fileOperations.add(new JMenuItem(actionMap.get("saveas")));
		fileOperations.add(new JSeparator());
		fileOperations.add(new JMenuItem(actionMap.get("exit")));

		JMenu textOperations = new JMenu("Edit");
		menu.add(textOperations);
		textOperations.add(new JMenuItem(actionMap.get("cut")));
		textOperations.add(new JMenuItem(actionMap.get("copy")));
		textOperations.add(new JMenuItem(actionMap.get("paste")));
		textOperations.add(new JSeparator());
		textOperations.add(new JMenuItem(actionMap.get("stats")));
	}

	/**
	 * Initializes toolbar. Toolbar is not floatable. It is split in 4 parts. File
	 * action: new file, save file, save file as, open file. Editing actions: cut,
	 * copy, paste. Statistics and Exit.
	 * @see #actionInit()
	 */
	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);

		//Class<? extends JnotepadPP> jnCl = this.getClass();		//JnotepadPP Class

		toolBar.add(new ToolbarButton(new ImageIcon("res/Blank.png"), actionMap.get("blank")));
		toolBar.add(new ToolbarButton(new ImageIcon("res/Open.png"), actionMap.get("open")));
		toolBar.add(new ToolbarButton(new ImageIcon("res/Save.png"), actionMap.get("save")));
		toolBar.add(new ToolbarButton(new ImageIcon("res/SaveAs.png"), actionMap.get("saveas")));
		toolBar.addSeparator();

		toolBar.add(new ToolbarButton(new ImageIcon("res/Cut.png"), actionMap.get("cut")));
		toolBar.add(new ToolbarButton(new ImageIcon("res/Copy.png"), actionMap.get("copy")));
		toolBar.add(new ToolbarButton(new ImageIcon("res/Paste.png"), actionMap.get("paste")));
		toolBar.addSeparator();

		toolBar.add(new ToolbarButton(new ImageIcon("res/Stats.png"), actionMap.get("stats")));
		toolBar.addSeparator();

		toolBar.add(new ToolbarButton(new ImageIcon("res/Exit.png"), actionMap.get("exit")));
	}

	/**
	 * Initializes tabbed pane. Makes background the same color as everything else.
	 */
	private void initTabbedPane() {
		tabs = new ModifiedTabbedPane();
		tabs.setOpaque(true);
		add(tabs, BorderLayout.CENTER);
	}

	/**
	 * Initializes window events. This method initializes window event when window is
	 * closing. When closing window, it will call Exit action.
	 * @see #exitAction()
	 */
	private void initWindowEvent() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});
	}

	/**
	 * Initializes <code>ChangeListener</code> for tabs. Overrides
	 * <code>stateChanged</code> method so whenever the tab is changed, method
	 * <code>changeTitle</code> will be called.
	 * @see #changeTitle()
	 */
	private void initTabChangeListener() {
		tabs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changeTitle();
			}
		});
	}

	/**
	 * Initializes all required actions. Action are: New Blank Document, Open File,
	 * Save File, Save File As, Cut, Copy, Paste, Statistics, Exit. Method also
	 * defines short descriptions and key bindings for each action.
	 */
	private void actionInit() {

		// Action for creating new blank document
		Action action = new AbstractAction("New Blank Document") {
			private static final long serialVersionUID = -226053105959290280L;

			@Override
			public void actionPerformed(ActionEvent e) {
				tabs.add(new TabComponent(null));
				tabs.setSelectedIndex(tabs.getTabCount() - 1);
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Create new blank document.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, CTRL_DOWN_MASK, true));
		actionMap.put("blank", action);

		// Action for opening existing files
		action = new AbstractAction("Open File...") {
			private static final long serialVersionUID = 2062164732722174194L;

			@Override
			public void actionPerformed(ActionEvent e) {
				File file = fileOpenAction(true);	//true is flag for opening
				if (file != null) {
					tabs.add(new TabComponent(file));
					tabs.setSelectedIndex(tabs.getTabCount() - 1);
					changeTitle();
				}
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Open existing file.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, CTRL_DOWN_MASK, true));
		actionMap.put("open", action);

		// Action for saving existing file
		action = new AbstractAction("Save File") {
			private static final long serialVersionUID = -1007710883041538399L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Save file");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_DOWN_MASK, true));
		actionMap.put("save", action);

		// Action for save as, for existing file
		action = new AbstractAction("Save File As...") {
			private static final long serialVersionUID = -5549917973146258863L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsAction();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Save file as...");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift S"));
		actionMap.put("saveas", action);

		// Action for cutting text
		action = new AbstractAction("Cut") {
			private static final long serialVersionUID = -3255431990098608569L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabComponent tab = (TabComponent) tabs.getSelectedComponent();
				if (tab != null) {
					tab.cut();
				}
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Cut selected text.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, CTRL_DOWN_MASK, true));
		actionMap.put("cut", action);

		// Action for copying text
		action = new AbstractAction("Copy") {
			private static final long serialVersionUID = -3255431990098608569L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabComponent tab = (TabComponent) tabs.getSelectedComponent();
				if (tab != null) {
					tab.copy();
				}
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Copy selected text.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, CTRL_DOWN_MASK, true));
		actionMap.put("copy", action);

		// Action for pasting text
		action = new AbstractAction("Paste") {
			private static final long serialVersionUID = -3255431990098608569L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabComponent tab = (TabComponent) tabs.getSelectedComponent();
				if (tab != null) {
					tab.paste();
				}
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Paste text from clipboard.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, CTRL_DOWN_MASK));
		actionMap.put("paste", action);

		// Action for getting statistics
		action = new AbstractAction("Statistics") {
			private static final long serialVersionUID = 1934192466083401633L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabComponent tab = (TabComponent) tabs.getSelectedComponent();
				if (tab == null) {
					JOptionPane.showMessageDialog(JnotepadPP.this, "There are no tabs.", "Statistics",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String statistics = tab.getStatistics();
				JOptionPane.showMessageDialog(JnotepadPP.this, statistics, "Statistics",
						JOptionPane.INFORMATION_MESSAGE);
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Calculate statistics.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, CTRL_DOWN_MASK, true));
		actionMap.put("stats", action);

		// Action for exiting application
		action = new AbstractAction("Exit") {
			private static final long serialVersionUID = 7203980126285743479L;

			@Override
			public void actionPerformed(ActionEvent e) {
				exitAction();
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, "Exit application.");
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL_DOWN_MASK, true));
		actionMap.put("exit", action);
	}

	/**
	 * Helper method doing "save" action. It is called by "save"
	 * <code>actionPerformed</code> method.
	 * @see #actionInit()
	 */
	void saveAction() {
		TabComponent tab = (TabComponent) tabs.getSelectedComponent();
		if (tab == null) {
			return;
		}

		if (tab.isNew()) {		//run normal "save as" action if file is new
			saveAsAction();
		} else {
			tab.saveFile();		//automatically updates tab label (removes star)
			changeTitle();
		}
	}

	/**
	 * Helper method doing "save as" action. It is called by "save as"
	 * <code>actionPerformed</code> method.
	 * @see #actionInit()
	 */
	private void saveAsAction() {
		TabComponent tab = (TabComponent) tabs.getSelectedComponent();
		if (tab == null) {
			return;
		}

		File file = fileOpenAction(false);		//false for saving
		if (file == null) {			//NO was clicked
			return;
		}

		boolean write = true;
		if (file.exists()) {
			write = overwrite(file.getName());		//should overwrite?
		}

		if (write) {
			try {
				file.createNewFile();	//if exists won't do anything
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error while creating new file.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			tab.saveFile(file);		//automatically updates tab label
			changeTitle();
		}
	}

	/**
	 * Helper method doing "exit" action. It is called by "exit"
	 * <code>actionPerformed</code> method. Goes through all tabs and saves them if
	 * necessary. It is not necessary to save file if it is new and not a single
	 * character was typed inside.
	 * @see #actionInit()
	 */
	private void exitAction() {
		for (Component comp : tabs.getComponents()) {		//run all tabs
			if (!(comp instanceof TabComponent)) {
				continue;
			}

			final boolean dirty = ((TabComponent) comp).isChanged();
			final String name = ((TabComponent) comp).getTabLabel().getText().substring(1);
			tabs.setSelectedComponent(comp);

			if (dirty) {	//should i even ask?
				int option = JOptionPane.showOptionDialog(JnotepadPP.this, "Would you like to save document "
						+ name + "?", "File not saved", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, 2);

				if (option == 0) {		//YES
					saveAction();
					continue;
				} else if (option == 1) {		//NO
					continue;
				}
				return;			//Cancel or close
			}
		}

		setVisible(false);
		dispose();		//closing if not interrupted by cancel or close on dialogs
	}

	/**
	 * Pops up file choosing dialog. It is used in "save as" and "open" actions.
	 * @param open <code>true</code> if it's used in "open" action,
	 *            <code>false</code> otherwise
	 * @return opened/saved file, <code>null</code> if cancel was pressed
	 */
	private File fileOpenAction(boolean open) {
		JFileChooser chooser = new JFileChooser();
		if (!open) {
			chooser.setApproveButtonText("Save");
			chooser.setDialogTitle("Save");
		} else {
			chooser.setApproveButtonText("Open");
			chooser.setDialogTitle("Open");
		}

		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
		chooser.addChoosableFileFilter(filter);

		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {		//cancel, closed or error
			return null;
		}
	}

	/**
	 * Pops up question dialog with YES-NO options.
	 * @param name name of the file for overwriting
	 * @return <code>true</code> if YES is clicked, <code>false</code> if NO is
	 *         clicked or dialog was closed
	 */
	private boolean overwrite(String name) {
		int option = JOptionPane.showOptionDialog(this,
				"File already exists! Would you like to overwrite file \"" + name + "\" ?", "File overwrite",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (option == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Method changes title of the main frame. Format of the new name is:
	 * "[absolute path of current file] - JNotepad++".
	 */
	void changeTitle() {
		TabComponent tab = (TabComponent) tabs.getSelectedComponent();
		if (tab != null) {		//in case last tab was closed
			setTitle(tab.getMainTitle() + " - JNotepad++");
		} else {
			setTitle("JNotepad++");
		}
	}

	/**
	 * Main method. Runs program (frame).
	 * @param args no arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new JnotepadPP().setVisible(true);
			}
		});
	}

}
