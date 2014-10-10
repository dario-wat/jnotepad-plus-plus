package hr.fer.zemris.java.hw08.jnotepadpp;

import hr.fer.zemris.java.hw08.jnotepadpp.localization.ILocalizationListener;
import hr.fer.zemris.java.hw08.jnotepadpp.localization.ILocalizationProvider;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
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
	
	/** Localization provider. */
	private ILocalizationProvider provider;

	/**
	 * Constructor.
	 * @param provider localization provider
	 */
	public ModifiedTabbedPane(ILocalizationProvider provider) {
		super();
		this.provider = provider;
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

			final ImageButton closeButton = new ImageButton(getClass().getResource("res/Close.png"));
			closeButton.setAction(new LocalizableAction("close", provider) {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (component.isChanged()) {
						final String name = component.getTabLabel().getText().substring(1);
						int option = JOptionPane.showOptionDialog(
								getTopLevelAncestor(),
								provider.getString("saveQuestion") + " " + name + "?",
								provider.getString("notSaved"),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								null,
								2);

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

			provider.addLocalizationListener(new ILocalizationListener() {
				
				@Override
				public void localizationChanged() {
					closeButton.setToolTipText(provider.getString("closeTab"));
				}
			});
			closeButton.setToolTipText(provider.getString("closeTab"));
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
		public ImageButton(URL imagePath) {
			super();
			try {
				img = ImageIO.read(imagePath);
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
