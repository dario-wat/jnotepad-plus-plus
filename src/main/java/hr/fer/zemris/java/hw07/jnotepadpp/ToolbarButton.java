package hr.fer.zemris.java.hw07.jnotepadpp;

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

	/**
	 * Constructor that sets button icon, button action and button tool tip.
	 * @param icon icon
	 * @param action action
	 */
	public ToolbarButton(Icon icon, Action action) {
		super(icon);
		addActionListener(action);
		setToolTipText((String) action.getValue(Action.SHORT_DESCRIPTION));
	}

}
