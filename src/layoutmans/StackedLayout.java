package layoutmans;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import javax.swing.SizeRequirements;

/**
 * Class that defines Stacked layout that is similar to <code>BoxedLayout</code>.
 * This layout can layout containers in 3 different direction: from top, from bottom
 * and fill. Instances of this layout will not be shared among multiple containers,
 * @author Dario Vidas
 */
public class StackedLayout implements LayoutManager2 {

	private StackedLayoutDirection direction;
	private transient SizeRequirements yTotal;
	private transient SizeRequirements xTotal;
	private transient SizeRequirements[] yChildren;
	private transient SizeRequirements[] xChildren;

	/**
	 * Constructor with one argument.
	 * @param direction direction for stacking components.
	 */
	public StackedLayout(StackedLayoutDirection direction) {
		super();
		this.direction = direction;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		Dimension size;
		synchronized (this) {
			checkRequests(target);
			size = new Dimension(xTotal.preferred, yTotal.preferred);
		}

		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right,
				Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom,
				Integer.MAX_VALUE);
		return size;
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		Dimension size;
		synchronized (this) {
			checkRequests(target);
			size = new Dimension(xTotal.minimum, yTotal.minimum);
		}

		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right,
				Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom,
				Integer.MAX_VALUE);
		return size;
	}

	/**
	 * Lays out container depending on direction. If direction is
	 * <code>FROM_TOP</code>, lays out container from top with preferred fixed sizes,
	 * if direction is <code>FROM_BOTTOM</code>, lays out container from bottom with
	 * preferred fixed sizes, and if direction is <code>FILL</code> spreads container
	 * across allocated space.
	 * @param target container to lay out
	 */
	@Override
	public void layoutContainer(Container target) {
		int nChildren = target.getComponentCount();
		int[] xOffsets = new int[nChildren];
		int[] xSpans = new int[nChildren];
		int[] yOffsets = new int[nChildren];
		int[] ySpans = new int[nChildren];

		Dimension alloc = target.getSize();
		Insets insets = target.getInsets();
		alloc.width -= insets.left + insets.right;
		alloc.height -= insets.top + insets.bottom;

		if (direction == StackedLayoutDirection.FROM_TOP) {
			synchronized (this) {
				checkRequests(target);
				calculateFixedSizes(yOffsets, ySpans, 0);
				SizeRequirements.calculateAlignedPositions(alloc.width, xTotal, xChildren, xOffsets, xSpans);
			}
		} else if (direction == StackedLayoutDirection.FROM_BOTTOM) {
			final int totalOffset = Math.max(alloc.height - preferredLayoutSize(target).height
					+ insets.bottom + insets.top, 0);		//full height - container height
			synchronized (this) {
				checkRequests(target);
				calculateFixedSizes(yOffsets, ySpans, totalOffset);
				SizeRequirements.calculateAlignedPositions(alloc.width, xTotal, xChildren, xOffsets, xSpans);
			}
		} else {
			synchronized (this) {
				checkRequests(target);
				SizeRequirements.calculateAlignedPositions(alloc.width, xTotal, xChildren, xOffsets, xSpans);
				SizeRequirements.calculateTiledPositions(alloc.height, yTotal, yChildren, yOffsets, ySpans);
			}
		}

		for (int i = 0; i < nChildren; i++) {
			Component comp = target.getComponent(i);
			comp.setBounds((int) Math.min((long) insets.left + (long) xOffsets[i], Integer.MAX_VALUE),
					(int) Math.min((long) insets.top + (long) yOffsets[i], Integer.MAX_VALUE), xSpans[i],
					ySpans[i]);
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		Dimension size;
		synchronized (this) {
			checkRequests(target);
			size = new Dimension(xTotal.maximum, yTotal.maximum);
		}

		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right,
				Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom,
				Integer.MAX_VALUE);
		return size;
	}

	@Override
	public synchronized float getLayoutAlignmentX(Container target) {
		checkRequests(target);
		return xTotal.alignment;
	}

	@Override
	public synchronized float getLayoutAlignmentY(Container target) {
		checkRequests(target);
		return yTotal.alignment;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	/**
	 * Checks size requirements and fills respective arrays.
	 * @param target container
	 */
	private void checkRequests(Container target) {
		if (yChildren == null || xChildren == null) {

			int compCount = target.getComponentCount();
			yChildren = new SizeRequirements[compCount];
			xChildren = new SizeRequirements[compCount];

			for (int i = 0; i < compCount; i++) {
				Component comp = target.getComponent(i);
				if (!comp.isVisible()) {
					yChildren[i] = new SizeRequirements(0, 0, 0, comp.getAlignmentY());
					xChildren[i] = new SizeRequirements(0, 0, 0, 0.0f);
					continue;
				}

				Dimension min = comp.getMinimumSize();
				Dimension typ = comp.getPreferredSize();
				Dimension max = comp.getMaximumSize();
				yChildren[i] = new SizeRequirements(min.height, typ.height, max.height, comp.getAlignmentY());
				xChildren[i] = new SizeRequirements(min.width, typ.width, max.width, 0.0f);
			}

			xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
			yTotal = SizeRequirements.getTiledSizeRequirements(yChildren);
		}
	}

	/**
	 * Calculates offsets and spans for fixed preferred size.
	 * @param offsets offsets
	 * @param spans spans
	 * @param totalOffset initial total offset
	 */
	private void calculateFixedSizes(int[] offsets, int[] spans, int totalOffset) {
		for (int i = 0; i < spans.length; i++) {
			offsets[i] = totalOffset;
			SizeRequirements req = yChildren[i];
			spans[i] = (int) req.preferred;
			totalOffset = (int) Math.min((long) totalOffset + (long) spans[i], Integer.MAX_VALUE);
		}
	}

	/**
	 * Defines 3 contants that are used for stacking directions.
	 * @author Dario Vidas
	 */
	public enum StackedLayoutDirection {
		FROM_TOP,
		FROM_BOTTOM,
		FILL
	}

}
