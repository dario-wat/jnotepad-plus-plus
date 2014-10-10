package hr.fer.zemris.java.hw08.jnotepadpp;

import hr.fer.zemris.java.hw08.jnotepadpp.localization.ILocalizationListener;
import hr.fer.zemris.java.hw08.jnotepadpp.localization.ILocalizationProvider;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Instances of this class are used as listeners of localization provider bridge.
 * These actions are used in <code>JComponents</code> that support actions. Whenever
 * language is changed, this object will change it's component content. Class extends
 * <code>AbstractAction</code>.
 * @author Dario Vidas
 */
public class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 5772755287356407502L;

	/** Key for finding language translation in bundle. */
	private String key;

	/** Reference to provider bridge. */
	private ILocalizationProvider provider;

	/**
	 * Constructor with 2 arguments. Adds itself as listener to bridge.
	 * @param key key for translation bundle
	 * @param provider reference to bridge
	 */
	public LocalizableAction(String key, ILocalizationProvider provider) {
		super();
		this.key = key;
		this.provider = provider;
		provider.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				updateAction();
			}
		});
		updateAction();
	}

	/**
	 * Updates action.
	 */
	private void updateAction() {
		putValue(NAME, provider.getString(key));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// do nothing
	}

}
