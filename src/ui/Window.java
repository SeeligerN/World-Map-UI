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

	public void addClickListener(ClickListener cl) {
		if (cl != null)
			clickListeners.add(cl);
	}

	public void removeClickListener(ClickListener cl) {
		if (cl != null)
			clickListeners.remove(cl);
	}

	public void setTitle(String title) {
		if (title != null)
			frame.setTitle(title);
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

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
	
	public void setControlLabel(int labelLoc, JLabel label) {
		cl.setLabel(labelLoc, label);
	}

	public void showControlBar(boolean show) {
		if (show)
			cl.setHeight(160);
		else
			cl.setHeight(0);
		
		SwingUtilities.updateComponentTreeUI(frame);
	}

	public void doHover(boolean dohover) {
		this.doHover = dohover;
	}

	public boolean doHover() {
		return doHover;
	}

	public void setFallbackHoverColor(Color fallbackHoverColor) {
		if (fallbackHoverColor != null)
			this.fallbackHoverColor = fallbackHoverColor;
	}

	public Color getFallbackHoverColor() {
		return fallbackHoverColor;
	}

	public void doTooltipText(boolean doTooltipText) {
		this.doTooltipText = doTooltipText;
		if (!doTooltipText)
			dl.setToolTipText("");
	}

	public boolean doTooltipText() {
		return doTooltipText;
	}

	public void setBackgroundColor(Color bgColor) {
		if (bgColor != null)
			this.bgColor = bgColor;
	}

	public Color getBackgroundColor() {
		return bgColor;
	}

	public class ViewSettings {

		public double cx;
		public double cy;
		public double zoom; // 1 px equates to "zoom" on the map

		public ViewSettings copy() {
			ViewSettings copy = new ViewSettings();

			copy.cx = this.cx;
			copy.cy = this.cy;
			copy.zoom = this.zoom;

			return copy;
		}

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

			if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
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

			if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
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
