package jnotepadpp.localization;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Instances of this class are used as localization providers in frames. Class
 * extends <code>LocalizationProviderBridge</code>.
 * @author Dario Vidas
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor, adds window listener to frame. When window is opened, frame
	 * connects to localization provider, when window is closed it disconnects.
	 * @param provider localization provider
	 * @param frame frame
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}
}
