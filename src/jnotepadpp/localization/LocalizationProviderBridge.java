package hr.fer.zemris.java.hw08.jnotepadpp.localization;

/**
 * Instances of this class act as localization provider bridge. Instances are
 * decorators to singleton localization provider.
 * @author Dario Vidas
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/** Is connected to localization provider. */
	private boolean connected = false;

	/** Reference to localization provider. */
	private ILocalizationProvider provider;

	/** Bridge listener to localization provider. */
	private ILocalizationListener listener;

	/**
	 * Constructor for bridge provider. Recieves single argument
	 * <code>ILocalizationProvider</code> which is a reference to localization
	 * provider.
	 * @param p reference to localization provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider p) {
		super();
		this.provider = p;
		listener = new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				fire();
			}
		};
	}

	/**
	 * Connects to localization provider.
	 */
	public void connect() {
		if (!connected) {
			connected = true;
			provider.addLocalizationListener(listener);
		}
	}

	/**
	 * Disconnects from localization provider.
	 */
	public void disconnect() {
		connected = false;
		provider.removeLocalizationListener(listener);
	}

	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

}
