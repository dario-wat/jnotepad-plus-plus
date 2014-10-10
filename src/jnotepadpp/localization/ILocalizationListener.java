package jnotepadpp.localization;

/**
 * Interface for localization listeners. These act as observer in observer design
 * pattern.
 * @author Dario Vidas
 */
public interface ILocalizationListener {

	/**
	 * Method is called when change happens. Defines what will class do when change
	 * occurs.
	 */
	void localizationChanged();
}
