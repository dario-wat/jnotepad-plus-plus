package jnotepadpp;

import jnotepadpp.localization.ILocalizationListener;
import jnotepadpp.localization.ILocalizationProvider;

import javax.swing.JMenu;

/**
 * Class defines <code>JMenu</code> with localizations.
 * @author Dario Vidas
 */
public class LJMenu extends JMenu {

	private static final long serialVersionUID = -4439220105941794296L;

	/** Key for resource bundle. */
	protected String key;

	/** Reference to localization provider. */
	protected ILocalizationProvider provider;

	/**
	 * Constructs <code>JMenu</code> with key to resource bundle and localization
	 * provider. Adds itself as a listener to localization provider.
	 * @param key key to resource bundle
	 * @param provider localization provider
	 */
	public LJMenu(String key, ILocalizationProvider provider) {
		super(provider.getString(key));
		this.key = key;
		this.provider = provider;
		provider.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				updateMenu();
			}
		});
	}

	/**
	 * Updates menu text.
	 */
	private void updateMenu() {
		setText(provider.getString(key));
	}
}
