package hr.fer.zemris.java.hw08.jnotepadpp.localization;

/**
 * Interface for localization providers. Acts as subject in observer desing pattern.
 * @author Dario Vidas
 */
public interface ILocalizationProvider {

	/**
	 * Method adds listener to subject.
	 * @param l listener to add
	 */
	void addLocalizationListener(ILocalizationListener l);

	/**
	 * Method removes listener from subject.
	 * @param l listener to remove
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * Method gets string for given key.
	 * @param key key
	 * @return localization specific string
	 */
	String getString(String key);
}
