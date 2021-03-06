package jnotepadpp;

import jnotepadpp.localization.FormLocalizationProvider;
import jnotepadpp.localization.ILocalizationListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Class defines buttons that have icon, action and also short description (which is
 * added from action).
 * @author Dario Vidas
 */
public class ToolbarButton extends JButton {

	private static final long serialVersionUID = -368165279012279207L;

	/** Reference to localization provider. */
	private FormLocalizationProvider flp;

	/** Button action. */
	private Action action;

	/**
	 * Constructor that sets button icon, button action and button tool tip.
	 * @param icon icon
	 * @param action action
	 * @param flp localization provider
	 */
	public ToolbarButton(Icon icon, Action action, FormLocalizationProvider flp) {
		super(icon);
		this.flp = flp;
		this.action = action;
		addActionListener(action);
		this.flp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				updateToolTip();
			}
		});
		updateToolTip();
	}

	/**
	 * Updates tool tip text.
	 */
	private void updateToolTip() {
		setToolTipText((String) action.getValue(Action.SHORT_DESCRIPTION));
	}

}
