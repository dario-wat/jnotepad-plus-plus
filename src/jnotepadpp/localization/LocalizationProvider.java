package jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class defines localization provider. Class is singleton and is never used directly
 * but only through decorators or bridges. Listeners of this object are localization
 * provider bridges so it actually notifies other providers of change and then they
 * notify their listeners.
 * @author Dario Vidas
 */
public final class LocalizationProvider extends AbstractLocalizationProvider {

	/** Static instance. */
	private static LocalizationProvider instance = new LocalizationProvider();

	/** Language tag. */
	private String language;

	/** Resource bundle. */
	private ResourceBundle bundle;

	/**
	 * Private constructor used for singleton class. Constructor sets inital language
	 * to english.
	 */
	private LocalizationProvider() {
		super();
		language = "en";
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(getClass().getPackage().getName() + ".translations", locale);
	}

	/**
	 * Returns instance of this class.
	 * @return instance
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Sets language and fires notifications to listeners.
	 * @param language language tag
	 */
	public void setLanguage(String language) {
		if (!this.language.equals(language)) {
			this.language = language;
			Locale locale = Locale.forLanguageTag(language);
			bundle = ResourceBundle.getBundle(getClass().getPackage().getName() + ".translations", locale);
			fire();
		}
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

}
