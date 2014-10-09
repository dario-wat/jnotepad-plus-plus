package hr.fer.zemris.java.hw07.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Class extends <code>JTabbedPane</code> and defines new method <code>add</code>
 * with single argument, <code>TabComponent component</code>.
 * @author Dario Vidas
 */
public class ModifiedTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 6450737211342077841L;

	/**
	 * Constructor. Calls super constructor.
	 */
	public ModifiedTabbedPane() {
		super();
	}

	/**
	 * Adds component to tabbed pane.
	 * @param component component
	 */
	public void add(TabComponent component) {
		add((Component) component);
		setTabComponentAt(indexOfComponent(component), new TabLook(component));
	}

	/**
	 * Inner class that defines the look of tab. Adds close action.
	 * @author Dario Vidas
	 */
	private class TabLook extends JPanel {

		private static final long serialVersionUID = 6683769797477630047L;

		/**
		 * Constructors tab, adds name and close action.
		 * @param component tab component
		 */
		public TabLook(final TabComponent component) {
			super();
			setLayout(new BorderLayout());
			setOpaque(false);

			add(component.getTabLabel(), BorderLayout.CENTER);

			ImageButton closeButton = new ImageButton("res/Close.png");
			closeButton.setAction(new AbstractAction("Close") {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (component.isChanged()) {
						final String name = component.getTabLabel().getText().substring(1);
						int option = JOptionPane
								.showOptionDialog(getTopLevelAncestor(), "Would you like to save document "
										+ name + "?", "File Not Saved", JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE, null, null, 2);

						if (option == 0) {		//YES
							((JnotepadPP) getTopLevelAncestor()).saveAction();
							ModifiedTabbedPane.this.remove(component);
						} else if (option == 1) {		//NO
							ModifiedTabbedPane.this.remove(component);
						}
						return;		//Cancel or close does nothing
					}
					ModifiedTabbedPane.this.remove(component);
				}
			});

			closeButton.setToolTipText("Close tab");
			closeButton.setOpaque(false);
			add(closeButton, BorderLayout.EAST);
		}
	}

	/**
	 * Private class for defining image buttons.
	 * @author Dario Vidas
	 */
	private static class ImageButton extends JButton {

		private static final long serialVersionUID = 7986375418022984286L;

		/**
		 * Image for button.
		 */
		private transient Image img;

		/**
		 * Constructs button from image.
		 * @param imagePath path to image
		 */
		public ImageButton(String imagePath) {
			super();
			try {
				//img = ImageIO.read(this.getClass().getResource("/Close.png"));
				img = ImageIO.read(new File("res/Close.png"));
			} catch (IOException e) {
				//do nothing
			}

			if (img != null) {
				setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			//super.paintComponent(g);
			if (img != null) {
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		}
	}
}
