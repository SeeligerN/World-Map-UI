package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ui.Map.Country;
import ui.controlBar.ControlLabel;

/**
 * This class handles the Window displayed on the screen hosting everything in
 * the ui. It also handles Clicks onto the map with ClickListeners.
 * 
 * @author Niklas S.
 *
 */
public class Window {

	public static final int LEFT_LABEL = 0;
	public static final int CENTER_LABEL = 1;
	public static final int RIGHT_LABEL = 2;

	private JFrame frame;

	private ViewSettings view;

	private List<ClickListener> clickListeners;

	private DrawLabel dl;
	private ControlLabel cl;

	private Color bgColor = Color.WHITE;
	private boolean doHover = true;
	private Country hoveredCountry = null;
	private Color fallbackHoverColor = Color.GRAY;
	private boolean doTooltipText = true;

	private int hoverOffset = 0;

	/**
	 * This constructor creates a new Window. By default it is not visible and needs
	 * to be set visible with setVisible(true). The title of the Window is empty by
	 * default. The controlBar is not visible by default.
	 * <p>
	 * The map cannot be changed. The starting view will be in the center of the
	 * map.
	 */
	public Window() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);

		view = new ViewSettings();

		view.cx = Map.getWidth() / 2;
		view.cy = Map.getHeight() / 2;
		view.zoom = 1.25;

		clickListeners = new ArrayList<>();

		dl = new DrawLabel();
		dl.setMaximumSize(new Dimension(99999999, 99999999));
		cl = new ControlLabel();

		GroupLayout gl = new GroupLayout(frame.getContentPane());

		gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(dl).addComponent(cl,
				GroupLayout.Alignment.CENTER));

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(dl).addComponent(cl));

		frame.setLayout(gl);

		PanMouse pm = new PanMouse();
		dl.addMouseListener(pm);
		dl.addMouseMotionListener(pm);
		dl.addMouseWheelListener(pm);
		dl.addComponentListener(pm);

		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Risk");
	}

	/**
	 * This method adds a ClickListener that will be called whenever a ClickEvent on
	 * the Map occurs.
	 * 
	 * @param cl is the ClickListener to be added to the list. Should cl be null
	 *           nothing will happen.
	 */
	public void addClickListener(ClickListener cl) {
		if (cl != null)
			clickListeners.add(cl);
	}

	/**
	 * This method removes a ClickListener from the list of Listeners. The
	 * listeners' method will no longer be invoked on ClickEvents on the map.
	 * 
	 * @param cl is the ClickListener that will be removed from the list. Should cl
	 *           be null or not be in the list nothing will happen.
	 */
	public void removeClickListener(ClickListener cl) {
		if (cl != null)
			clickListeners.remove(cl);
	}

	/**
	 * This method changes the title of the Window to title.
	 * 
	 * @param title is the new title of the Window. Should title be null nothing
	 *              will happen. To have no title displayed, which is strongly
	 *              discouraged, an empty String should be passed.
	 */
	public void setTitle(String title) {
		if (title != null)
			frame.setTitle(title);
	}

	/**
	 * This method sets the Window visible or invisible.
	 * 
	 * @param visible specifies whether or not the Window should now be visible or
	 *                not. (true for visible, false for invisible)
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * This method shows a new {@link FeedbackWindow} on top of this Window. The
	 * {@link FeedbackWindow} summarizes one turn and is used to provide Feedback as
	 * to what happened to the players.
	 * 
	 * @param title               is the Window title and title label of the
	 *                            FeedbackDialog. Should title be null nothing will
	 *                            happen.
	 * @param attacker            is the attacker of the play. Should attacker be
	 *                            null nothing will happen.
	 * @param defender            is the defender of the play. Should defender be
	 *                            null nothing will happen.
	 * @param attackedCountry     is the country that was attacked. Should
	 *                            attackedCountry be null nothing will happen.
	 * @param originCountry       is the country from which the attackedCountry was
	 *                            attacked. Should originCountry be null nothing
	 *                            will happen.
	 * @param diceResultsAttacker are the dice results of the attacker stored in an
	 *                            int[]. Should diceResultsAttacker be null or not
	 *                            contain any Integers nothing will happen.
	 * @param diceResultsDefender are the dice results of the defender stored in an
	 *                            int[]. Should diceResultsDefender be null or not
	 *                            contain any Integers nothing will happen.
	 * @param lossesAttacker      are the troops losses of the attacker.
	 * @param lossesDefender      are the troops losses of the defender.
	 * @param resultText          is a little summary of the play that is displayed
	 *                            at the bottom of the Dialog. Should resultText be
	 *                            null nothing will happen.
	 */
	public void showFeedbackDialog(String title, String attacker, String defender, String attackedCountry,
			String originCountry, int[] diceResultsAttacker, int[] diceResultsDefender, int lossesAttacker,
			int lossesDefender, String resultText) {

		if (title == null || attacker == null || defender == null || attackedCountry == null || originCountry == null
				|| resultText == null || diceResultsAttacker == null || diceResultsDefender == null)
			return;
		if (diceResultsAttacker.length == 0 || diceResultsDefender.length == 0) // both dice result arrays need to
																				// contain at least one int
			return;

		new FeedbackWindow(frame, title, attacker, defender, attackedCountry, originCountry, diceResultsAttacker,
				diceResultsDefender, lossesAttacker, lossesDefender, resultText);
	}

	/**
	 * Sets the controlLabel at the specified in location in the controlBar to the
	 * specified label.
	 * 
	 * @param labelLoc is the label location in the controlBar. Possible Values are
	 *                 Window.LEFT_LABEL, Window.CETNER_LABEL, Window.RIGHT_LABEL.
	 *                 Should none of these values be specified nothing will happen.
	 * @param label    is the label that is to be set. Any {@link JLabel} is
	 *                 possible to be set in the location. But the Labels in
	 *                 controlBar are designed to be placed there. Should label be
	 *                 null an empty JLabel will be placed in that location.
	 */
	public void setControlLabel(int labelLoc, JLabel label) {
		cl.setLabel(labelLoc, label);
	}

	/**
	 * This methods sets the controlBar to be visible or invisible. The contents of
	 * the controlBar are kept and will be visible once the controlBar is set to be
	 * visible again.
	 * 
	 * @param show is whether the controlBar should be visible or not. (true for
	 *             visible, false for invisible)
	 */
	public void showControlBar(boolean show) {
		if (show)
			cl.setHeight(160);
		else
			cl.setHeight(0);

		SwingUtilities.updateComponentTreeUI(frame);
	}

	/**
	 * This method sets the preference on whether or not to highlight the country
	 * that the mouse is currently above by drawing the country with it's hover
	 * color or the default hover color and with a black outline.
	 * 
	 * @param dohover specifies whether or not to highlight 'hovered' country. (true
	 *                highlight country, false don't highlight country)
	 */
	public void doHover(boolean dohover) {
		this.doHover = dohover;
	}

	/**
	 * This method is a getter for the preference on whether or not to highlight the
	 * hovered country.
	 * 
	 * @return whether the 'hovered' country is to be highlighted.
	 */
	public boolean doHover() {
		return doHover;
	}

	/**
	 * Setter for the fallback hover color. This is the color hovered countries are
	 * highlighted with if they themselves do not specify a hover color.
	 * 
	 * @param fallbackHoverColor is the new fallback hover color. Should
	 *                           fallbackHoverColor be null nothing will happen.
	 */
	public void setFallbackHoverColor(Color fallbackHoverColor) {
		if (fallbackHoverColor != null)
			this.fallbackHoverColor = fallbackHoverColor;
	}

	/**
	 * Getter for the default hover color. This is the color that is used to
	 * highlight countries that don't specify a hover color themselves.
	 * 
	 * @return the fallbackHoverColor.
	 */
	public Color getFallbackHoverColor() {
		return fallbackHoverColor;
	}

	/**
	 * Setter for whether or not to draw TooltipText for the countries.
	 * 
	 * @param doTooltipText specifies if the TooltipTexts are to be drawn.
	 */
	public void doTooltipText(boolean doTooltipText) {
		this.doTooltipText = doTooltipText;
		if (!doTooltipText)
			dl.setToolTipText("");
	}

	/**
	 * Getter for whether or not to draw TooltipTexts.
	 * 
	 * @return true if the TooltipTexts are to be drawn.
	 */
	public boolean doTooltipText() {
		return doTooltipText;
	}

	/**
	 * Setter for the background color of the map (i.e. the color of the ocean).
	 * 
	 * @param bgColor the new background color of the map. Should bgColor be null
	 *                nothing will happen.
	 */
	public void setBackgroundColor(Color bgColor) {
		if (bgColor != null)
			this.bgColor = bgColor;
	}

	/**
	 * Getter for the background color of the map.
	 * 
	 * @return the background color of the map.
	 */
	public Color getBackgroundColor() {
		return bgColor;
	}

	/**
	 * This class stores ViewSettings of the map viewport. It is meant to ease
	 * conversion of screenspace coordinates and mapspace coordinates.
	 * 
	 * @author Niklas S.
	 *
	 */
	public class ViewSettings {

		/**
		 * The x coordinate of the center of the viewport on the map in mapspace
		 * coordinates.
		 */
		public double cx;
		/**
		 * The y coordinate of the center of the viewport on the map in mapspace
		 * coordinates.
		 */
		public double cy;
		/**
		 * The zoom level of the viewport. One pixel of screenspace equates to zoom
		 * pixels of mapspace.
		 */
		public double zoom; // 1 px equates to "zoom" on the map

		/**
		 * This method creates a copy of this object with fresh values to be edited
		 * without affecting the values of this object.
		 * 
		 * @return a copy of this ViewSettings object with the values of this one.
		 */
		public ViewSettings copy() {
			ViewSettings copy = new ViewSettings();

			copy.cx = this.cx;
			copy.cy = this.cy;
			copy.zoom = this.zoom;

			return copy;
		}

		/**
		 * This method converts the given mapspace coordinates to screenspace
		 * coordinates based on the current viewport settings.
		 * 
		 * @param x is the x coordinate of the mapspace coordinates that are to be
		 *          converted to screenspace coordinates.
		 * @param y is the y coordinate of the mapspace coordinates that are to be
		 *          converted to screenspace coordinates.
		 * @return an int[] with the coordinates in screenspace that correspond to the
		 *         specified coordinates in mapspace. The x coordinate will be at the
		 *         index 0 the y coordinate will be at the index 1.
		 */
		public int[] convertToScreenSpace(double x, double y) {
			int[] scS = new int[2];

			x -= cx;
			y -= cy;

			int xOffset = (int) (x / zoom);
			int yOffset = (int) (y / zoom);

			scS[0] = xOffset + dl.getWidth() / 2;
			scS[1] = yOffset + dl.getHeight() / 2;

			return scS;
		}

		/**
		 * This method converts the given screenspace coordinates to mapspace
		 * coordinates based on teh current viewport settings.
		 * 
		 * @param x is the x coordinate of the screenspace coordinates taht are to be
		 *          converted to mapspace coordinates.
		 * @param y is the y coordinate of the screenspace coordinates that are to be
		 *          converted to mapspace coordinates.
		 * @return a double[] with the coordinates in mapspace that correspond to the
		 *         specified coordinates in screenspace. The x coordinate will be at the
		 *         index 1 the y coordinate will be at the index 1.
		 */
		public double[] convertToMapSpace(int x, int y) {
			double[] mS = new double[2];

			x -= dl.getWidth() / 2;
			y -= dl.getHeight() / 2;

			mS[0] = (double) x * zoom;
			mS[1] = (double) y * zoom;

			mS[0] += cx;
			mS[1] += cy;

			return mS;
		}
	}

	/**
	 * This class handles the transformation of the ViewSettings based on the user
	 * panning and zooming. It also listens for clicks on the Map and traces them
	 * back to their country.
	 * <p>
	 * When the Map is resized the zoom will also scale to have consistent scaling
	 * on the x axis.
	 * 
	 * @author Niklas S.
	 *
	 */
	private class PanMouse extends ComponentAdapter implements MouseListener, MouseMotionListener, MouseWheelListener {

		int rotation = 0;
		int dragX = 0;
		int dragY = 0;
		double baseZoom = view.zoom;

		Dimension oldDimension = new Dimension(0, 0);

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hoveredCountry = null;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			dragX = e.getX();
			dragY = e.getY();

			if (e.getButton() == MouseEvent.BUTTON1) {
				int x = e.getX();
				int y = e.getY();

				if (view.convertToMapSpace(x, y)[0] < 0)
					x += Map.getWidth() / view.zoom;
				if (view.convertToMapSpace(x, y)[0] > Map.getWidth())
					x -= Map.getWidth() / view.zoom;

				// trigger listener event
				Country c = Map.traceCountry(x, y, view);

				// debugging printing mapCoordinates for finding coords in mapdata
				// double[] mapSpace = view.convertToMapSpace(x, y);
				// System.out.printf("%f, %f\n", mapSpace[0], mapSpace[1]);

				for (ClickListener cl : clickListeners)
					cl.handleClick(new ClickEvent(c));
			}

			frame.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int dx = e.getX() - dragX;
			int dy = e.getY() - dragY;

			dragX = e.getX();
			dragY = e.getY();

			if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
				view.cx -= view.zoom * (float) dx;
				view.cy -= view.zoom * (float) dy;

				// horizontal world wrapping
				if (view.cx < 0) {
					view.cx = Map.getWidth();
					hoverOffset--;
				}
				if (view.cx > Map.getWidth()) {
					view.cx = 0;
					hoverOffset++;
				}

				// vertical capping
				if (view.cy < 0)
					view.cy = 0;
				if (view.cy > Map.getHeight())
					view.cy = Map.getHeight();
			}

			frame.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			hoverOffset = 0;
			if (view.convertToMapSpace(x, y)[0] < 0) {
				x += Map.getWidth() / view.zoom;
				hoverOffset = 1;
			}
			if (view.convertToMapSpace(x, y)[0] > Map.getWidth()) {
				x -= Map.getWidth() / view.zoom;
				hoverOffset = -1;
			}

			if (doHover) {
				Country pastCountry = hoveredCountry;
				hoveredCountry = Map.traceCountry(x, y, view);
				if (pastCountry != hoveredCountry)
					frame.repaint();
			}

			if (doTooltipText) {
				Country hoveredCountry = Map.traceCountry(x, y, view);
				if (hoveredCountry != null && !hoveredCountry.getTooltipText().equals(""))
					dl.setToolTipText(hoveredCountry.getTooltipText());
				else
					dl.setToolTipText(null);
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			rotation += e.getWheelRotation();

			double newZoom = baseZoom * (double) Math.pow(1.06, rotation);

			double maxWidth = 2; // map widths
			double minWidth = .05;

			if (Map.getWidth() * maxWidth / newZoom < dl.getWidth()
					|| Map.getWidth() * minWidth / newZoom > dl.getWidth()) {
				rotation -= e.getWheelRotation();
			} else {
				view.zoom = newZoom;
				frame.repaint();
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);

			// scale zoom
			if (!oldDimension.equals(new Dimension(0, 0))) {
				view.zoom /= (double) dl.getWidth() / (double) oldDimension.getWidth();
				rotation = 0;
				baseZoom = view.zoom;
			}
			oldDimension = new Dimension(dl.getWidth(), dl.getHeight());
		}
	}

	/**
	 * This class is responsible for drawing the Map in its label. It extends JLabel
	 * and only overrides it's paint(Graphics g) method.
	 * 
	 * @author Niklas S.
	 *
	 */
	private class DrawLabel extends JLabel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g1) {
			super.paint(g1);

			Graphics2D g = (Graphics2D) g1;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g.setColor(bgColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.setFont(new Font("Arial", Font.BOLD, (int) (.03 * dl.getWidth())));

			Map.drawCountries(g, view);
			ViewSettings right = view.copy();
			right.cx -= Map.getWidth();
			Map.drawCountries(g, right);
			ViewSettings left = view.copy();
			left.cx += Map.getWidth();
			Map.drawCountries(g, left);

			// hovered country
			if (doHover && hoveredCountry != null) {
				ViewSettings hoverView = view.copy();
				hoverView.cx += hoverOffset * Map.getWidth();

				Polygon joinedShape = new Polygon();
				for (Shape s : hoveredCountry.generateShapes(hoverView)) {
					if (hoveredCountry.getHoverColor() != null)
						g.setColor(hoveredCountry.getHoverColor());
					else
						g.setColor(fallbackHoverColor);
					g.fill(s);
					g.setColor(Color.BLACK);
					g.draw(s);

					Rectangle boundingBox = s.getBounds();
					joinedShape.addPoint(boundingBox.x, boundingBox.y);
					joinedShape.addPoint(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height);
				}

				if (hoveredCountry.getLabel() != null && !hoveredCountry.getLabel().isEmpty()) {
					String[] lines = hoveredCountry.getLabel().split("\n");

					float labelX = (float) joinedShape.getBounds().getCenterX();
					float labelYBaseLine = (float) joinedShape.getBounds().getCenterY()
							+ 1.8f * g.getFont().getSize() * lines.length / 2;

					for (int i = 0; i < lines.length; i++) {

						int stringWidth = g.getFontMetrics().stringWidth(lines[i]);

						if (Map.drawBlack(hoveredCountry.getColor()))
							g.setColor(Color.BLACK);
						else
							g.setColor(Color.LIGHT_GRAY);

						g.drawString(lines[i], labelX - stringWidth / 2,
								labelYBaseLine - (1.8f * g.getFont().getSize() * (lines.length - i)) / 2);
					}
				}
			}
		}
	}
}
