package hr.fer.zemris.java.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implements <code>ILocalizationProvider</code> and acts as abstract class for
 * localization providers. As abstract class it defines methods for adding and
 * removing listeners and also method for firing listeners when change occurs.
 * @author Dario Vidas
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/** List of listeners. */
	private List<ILocalizationListener> listeners = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public AbstractLocalizationProvider() {
		super();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}

	/**
	 * Method is called when change in provider happens. Method notifies all
	 * listeners of change.
	 */
	public void fire() {
		for (ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}

}
