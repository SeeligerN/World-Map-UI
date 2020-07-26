package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ui.Window.ViewSettings;

/**
 * This class reads, manages and draws mapdata. Loading happens in a static
 * block and is therefore the first thing that happens.
 * 
 * @author Niklas S.
 *
 */
public class Map {

	private static double mapWidth;
	private static double mapHeight;
	private static List<double[]> lines;
	private static List<Country> countries;

	private static int[][] adjacencyMatrix;

	/*
	 * loading in map data
	 */
	static {
		try {
			InputStream is = Map.class.getResourceAsStream("/ui/resources/riskmapdata.txt");
			Scanner sc = new Scanner(is);
			countries = new ArrayList<>();
			lines = new ArrayList<>();

			for (int i = 0; i < 2; i++) {
				String[] split = sc.nextLine().split("=");

				if (split[0].equalsIgnoreCase("width"))
					mapWidth = Double.parseDouble(split[1]);
				if (split[0].equalsIgnoreCase("height"))
					mapHeight = Double.parseDouble(split[1]);
			}

			// line data
			String linedataline = sc.nextLine().replace("lines", "");
			String[] lines = linedataline.split(" M ");
			for (String line : lines) {
				if (line.isEmpty())
					continue;
				String[] segments = line.split(" L ");

				double[] da = new double[] { Double.parseDouble(segments[0].split(",")[0]),
						Double.parseDouble(segments[0].split(",")[1]), Double.parseDouble(segments[1].split(",")[0]),
						Double.parseDouble(segments[1].split(",")[1]) };

				Map.lines.add(da);
			}

			int idCounter = 0;
			while (sc.hasNextLine()) {
				String[] split = sc.nextLine().split("=");

				if (split[0].startsWith("#"))
					continue;

				countries.add(new Country(idCounter++, split[0], split[1]));
			}

			sc.close();

			// adjacency
			double tolerance = 1;
			adjacencyMatrix = new int[countries.size()][countries.size()];
			for (int x = 0; x < adjacencyMatrix.length; x++)
				for (int y = 0; y < adjacencyMatrix[0].length; y++)
					adjacencyMatrix[x][y] = 0;

			for (int from = 0; from < adjacencyMatrix.length; from++)
				COUNTRY: for (int to = 0; to < from; to++) {

					List<double[]> fromVertices = countries.get(from).dumpVertices();
					List<double[]> toVertices = countries.get(to).dumpVertices();

					for (double[] v1 : fromVertices)
						for (double[] v2 : toVertices)
							if (Math.abs(v1[0] - v2[0]) < tolerance && Math.abs(v1[1] - v2[1]) < tolerance) {
								countries.get(from).addNeighbour(countries.get(to));
								continue COUNTRY;
							}

				}

		} catch (Exception e) {
			countries = null;
			mapWidth = -1;
			mapHeight = -1;
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the width of the map.
	 * 
	 * @return the width of the map.
	 */
	static double getWidth() {
		return mapWidth;
	}

	/**
	 * Getter for the height of the map.
	 * 
	 * @return the height of the map.
	 */
	static double getHeight() {
		return mapHeight;
	}

	/**
	 * Getter for the list of countries.
	 * 
	 * @return a list of all Countries listed in the mapdata.
	 */
	public static List<Country> getCountries() {
		return countries;
	}

	/**
	 * Getter for specific country contained in mapdata based on its String id.
	 * 
	 * @param id is the String id that will be searched for.
	 * @return the country that has the same String id as the argumend id. Should no
	 *         such country exist in the map data null will be returned.
	 */
	public static Country getCountry(String id) {
		for (Country c : countries)
			if (c.id.equalsIgnoreCase(id))
				return c;

		return null;
	}

	/**
	 * This is a method for drawing the countries in the mapdata with Graphics g and
	 * transformed to comply with the ViewSettings.
	 * 
	 * @param g    is the Grahpics2D object with which the countries will be drawn.
	 * @param view is the ViewSettings that will be used to transform the country
	 *             data to fit with the settings.
	 */
	public static void drawCountries(Graphics2D g, ViewSettings view) {

		g.setStroke(new BasicStroke(1.5f));

		List<String[]> text = new ArrayList<>();
		for (Country c : countries) {
			Polygon joinedShape = new Polygon();
			for (Shape s : c.generateShapes(view)) {
				g.setColor(c.getColor());
				g.fill(s);
				g.setColor(Color.WHITE);
				g.draw(s);

				Rectangle boundingBox = s.getBounds();
				joinedShape.addPoint(boundingBox.x, boundingBox.y);
				joinedShape.addPoint(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height);
			}

			// entering country's label into toDraw list
			if (c.getLabel() != null && !c.getLabel().isEmpty()) {
				String[] lines = c.getLabel().split("\n");

				float labelX = (float) joinedShape.getBounds().getCenterX();
				float labelYBaseLine = (float) joinedShape.getBounds().getCenterY()
						+ 1.8f * g.getFont().getSize() * lines.length / 2;

				for (int i = 0; i < lines.length; i++) {

					int stringWidth = g.getFontMetrics().stringWidth(lines[i]);

					text.add(new String[] { lines[i], "" + (labelX - stringWidth / 2),
							"" + (labelYBaseLine - (1.8f * g.getFont().getSize() * (lines.length - i)) / 2),
							"" + drawBlack(c.getColor()) });

				}
			}
		}

		// drawing country's label
		for (String[] lineToDraw : text) {
			if (Boolean.parseBoolean(lineToDraw[3]))
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.LIGHT_GRAY);

			g.drawString(lineToDraw[0], Float.parseFloat(lineToDraw[1]), Float.parseFloat(lineToDraw[2]));
		}

		g.setColor(Color.BLACK);
		g.setStroke(
				new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10f }, 0.0f));

		for (double[] line : lines) {
			int[] from = view.convertToScreenSpace(line[0], line[1]);
			int[] to = view.convertToScreenSpace(line[2], line[3]);

			g.drawLine(from[0], from[1], to[0], to[1]);
		}

		g.setStroke(new BasicStroke(1.5f));
	}

	/**
	 * Method for deciding whether or not to draw a black label on top of the
	 * specified color. The average RGB value has to be above 80 for this method to
	 * return true.
	 * 
	 * @param c is the base color.
	 * @return whether to draw in black or not. (true = black, false = white)
	 */
	static boolean drawBlack(Color c) {
		return drawBlack(c, 80);
	}

	/**
	 * Method for deciding whether or not to draw a black label on top of the
	 * specified color based on a custom average RGB value threshold.
	 * 
	 * @param c          is the base color.
	 * @param threshhold is the value above which the average RGB has to be for true
	 *                   to be returned.
	 * @return whether to draw in black or not. (true = black, false = white)
	 */
	static boolean drawBlack(Color c, int threshhold) {
		if (c.getRed() + c.getGreen() + c.getBlue() > threshhold * 3)
			return true;
		return false;
	}

	/**
	 * This method traces which country a specified screenspace coordinates falls
	 * onto based on the specified viewSettings.
	 * 
	 * @param x    the x coordinate
	 * @param y    the y coordinate
	 * @param view the viewSettings used to calculate mapspace coordinates from the
	 *             screenspace coordinates.
	 * @return the country in which the coordinates are contained. Should no country
	 *         contain the specified coordinates null will be returned. Should
	 *         multiple countries contain the coordinates only the first country
	 *         listed in the mapdata will be returned.
	 */
	public static Country traceCountry(int x, int y, ViewSettings view) {
		view = view.copy();

		for (Country c : countries)
			for (Shape s : c.generateShapes(view))
				if (s.contains(new Point(x, y)))
					return c;

		return null;
	}

	/**
	 * This class manages loaded data of countries.
	 * 
	 * @author Niklas S.
	 *
	 */
	public static class Country {

		private final int numId;
		private String id;
		private List<List<double[]>> shapes;
		private Color c;
		private Color hoverColor;
		private String tooltipText;
		private String label;

		/**
		 * Constructor for creating a Country object.
		 * 
		 * @param numId            is a unique identification number for each number.
		 * @param id               is the string id that is specified in the mapdata
		 *                         file and is the main way countries are identified.
		 * @param drawInstructions are the draw instructions in the mapdata. These are
		 *                         immediately parsed into a list of coordinates as draw
		 *                         instructions of different shapes.
		 */
		public Country(int numId, String id, String drawInstructions) {
			this.numId = numId;
			this.id = id;
			this.tooltipText = id;
			this.c = new Color(205, 205, 205);
			label = "";

			shapes = new ArrayList<>();

			boolean absoluteInstructions = drawInstructions.contains("L");

			String[] inst = drawInstructions.split(" ");

			List<double[]> currentShape = new ArrayList<>();
			double penx = 0;
			double peny = 0;
			for (int i = 0; i < inst.length; i++) {

				if (inst[i].equalsIgnoreCase("m")) {
					if (currentShape.size() != 0) {
						shapes.add(currentShape);
						currentShape = new ArrayList<>();
					}
					i++;

					String[] move = inst[i].split(",");

					if (absoluteInstructions) {
						penx = Double.parseDouble(move[0]);
						peny = Double.parseDouble(move[1]);
					} else {
						penx += Double.parseDouble(move[0]);
						peny += Double.parseDouble(move[1]);
					}

					continue;
				}

				if (inst[i].equalsIgnoreCase("z")) {
					currentShape.add(new double[] { penx, peny });
				}

				if (inst[i].equalsIgnoreCase("l")) {
					continue;
				}

				if (inst[i].matches("[-\\d.]+,[-\\d.]+")) {

					currentShape.add(new double[] { penx, peny });

					String[] move = inst[i].split(",");
					if (absoluteInstructions) {
						penx = Double.parseDouble(move[0]);
						peny = Double.parseDouble(move[1]);
					} else {
						penx += Double.parseDouble(move[0]);
						peny += Double.parseDouble(move[1]);
					}

				}
			}
			shapes.add(currentShape);
		}

		/**
		 * Getter for the String id.
		 * 
		 * @return the string id.
		 */
		public String getId() {
			return id;
		}

		/**
		 * Compiles a list of all vertices of all shapes.
		 * 
		 * @return a list of all vertices regardless of which shape they are apart of.
		 */
		public List<double[]> dumpVertices() {
			List<double[]> vertices = new ArrayList<>();

			for (List<double[]> shapes : this.shapes)
				vertices.addAll(shapes);

			return vertices;
		}

		/**
		 * Compiles all vertex groups into shapes in mapspace. Useful for calculating
		 * point shape intersection.
		 * 
		 * @return a list of shapes of all "islands" of the country that would be drawn.
		 */
		public List<Shape> generateShapes() {
			List<Shape> shapes = new ArrayList<>();

			for (List<double[]> shape : this.shapes) {
				Polygon p = new Polygon();

				for (double[] d : shape) {
					int[] screenSpaceCoords = new int[] { (int) d[0], (int) d[1] };
					p.addPoint(screenSpaceCoords[0], screenSpaceCoords[1]);
				}
				shapes.add(p);
			}
			return shapes;
		}

		/**
		 * Compiles all vertex groups into shapes in screenspace based on the given
		 * viewsettings. Useful for calculating point shape intersection.
		 * 
		 * @param view is the ViewSettings objects used to calculate the screenspace
		 *             coordinates.
		 * @return a list of shapes of all "islands" of the country that would be drawn.
		 */
		public List<Shape> generateShapes(ViewSettings view) {
			List<Shape> shapes = new ArrayList<>();

			for (List<double[]> shape : this.shapes) {
				Polygon p = new Polygon();

				for (double[] d : shape) {
					int[] screenSpaceCoords = view.convertToScreenSpace(d[0], d[1]);
					p.addPoint(screenSpaceCoords[0], screenSpaceCoords[1]);
				}
				shapes.add(p);
			}
			return shapes;
		}

		/**
		 * This method adds the specified country as a neighbour to this country as well
		 * as the other way around.
		 * 
		 * @param neighbourToAdd the neighbour country that is to be added to be added.
		 */
		public void addNeighbour(Country neighbourToAdd) {
			adjacencyMatrix[numId][neighbourToAdd.numId] = 1;
			adjacencyMatrix[neighbourToAdd.numId][numId] = 1;
		}

		/**
		 * Compiles a list of all countries that are neighbours to this country.
		 * 
		 * @return list of all neighbouring countries.
		 */
		public List<Country> getNeighbours() {
			List<Country> neighbours = new ArrayList<>();

			for (int i = 0; i < countries.size(); i++)
				if (adjacencyMatrix[numId][i] != 0)
					neighbours.add(countries.get(i));

			return neighbours;
		}

		/**
		 * Getter for the countries color.
		 * 
		 * @return the color of the country.
		 */
		public Color getColor() {
			return c;
		}

		/**
		 * Setter for the countries color.
		 * 
		 * @param c is the new color. Should c be null nothing will happen.
		 */
		public void setColor(Color c) {
			if (c != null)
				this.c = c;
		}

		/**
		 * Getter for the hover color of the country.
		 * 
		 * @return the hover color of the country.
		 */
		public Color getHoverColor() {
			return hoverColor;
		}

		/**
		 * Setter for the hover color.
		 * 
		 * @param hoverColor is the new hover color. Should null be specified null will
		 *                   be the new hover color.
		 */
		public void setHoverColor(Color hoverColor) {
			this.hoverColor = hoverColor;
		}

		/**
		 * Setter for the tooltip text, the text to be displayed in a small label
		 * besides the mouse if the mouse is resting on this country.
		 * 
		 * @param tooltipText is the new tooltiptext.
		 */
		public void setTooltipText(String tooltipText) {
			this.tooltipText = tooltipText;
		}

		/**
		 * Getter for the tooltip text, the text to be displayed in a small label
		 * besides the mouse if the mouse is resting on this country.
		 * 
		 * @return the current tooltip text.
		 */
		public String getTooltipText() {
			return tooltipText;
		}

		/**
		 * Getter for the label of the country, the text to be displayed permanently on
		 * the map.
		 * 
		 * @param label is the new String to be set as the label. Should label be null
		 *              or an empty String no label will be displayed.
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/**
		 * Getter for the label of the country, the text to be displayed permanently on
		 * the map.
		 * 
		 * @return the label of the country. Should null have been specified as the
		 *         label an empty String will be returned.
		 */
		public String getLabel() {
			if (label == null)
				return "";
			return label;
		}

		@Override
		public String toString() {
			return id;
		}
	}

}
