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

public class Map {

	private static double mapWidth;
	private static double mapHeight;
	private static List<double[]> lines;
	private static List<Country> countries;

	private static int[][] adjacencyMatrix;

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

	static double getWidth() {
		return mapWidth;
	}

	static double getHeight() {
		return mapHeight;
	}

	public static List<Country> getCountries() {
		return countries;
	}

	public static Country getCountry(String id) {
		for (Country c : countries)
			if (c.id.equalsIgnoreCase(id))
				return c;

		return null;
	}

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

	static boolean drawBlack(Color c) {
		return drawBlack(c, 80);
	}

	static boolean drawBlack(Color c, int threshhold) {
		if (c.getRed() + c.getGreen() + c.getBlue() > threshhold * 3)
			return true;
		return false;
	}

	public static Country traceCountry(int x, int y, ViewSettings view) {
		view = view.copy();

		for (Country c : countries)
			for (Shape s : c.generateShapes(view))
				if (s.contains(new Point(x, y)))
					return c;

		return null;
	}

	public static class Country {

		private final int numId;
		private String id;
		private List<List<double[]>> shapes;
		private Color c;
		private Color hoverColor;
		private String tooltipText;
		private String label;
		
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

		public String getId() {
			return id;
		}

		public List<double[]> dumpVertices() {
			List<double[]> vertices = new ArrayList<>();

			for (List<double[]> shapes : this.shapes)
				vertices.addAll(shapes);

			return vertices;
		}

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

		public void addNeighbour(Country neighbourToAdd) {
			adjacencyMatrix[numId][neighbourToAdd.numId] = 1;
			adjacencyMatrix[neighbourToAdd.numId][numId] = 1;
		}

		public List<Country> getNeighbours() {
			List<Country> neighbours = new ArrayList<>();

			for (int i = 0; i < countries.size(); i++)
				if (adjacencyMatrix[numId][i] != 0)
					neighbours.add(countries.get(i));

			return neighbours;
		}

		public Color getColor() {
			return c;
		}

		public void setColor(Color c) {
			if (c != null)
				this.c = c;
		}

		public Color getHoverColor() {
			return hoverColor;
		}

		public void setHoverColor(Color hoverColor) {
			this.hoverColor = hoverColor;
		}

		public void setTooltipText(String tooltipText) {
			this.tooltipText = tooltipText;
		}

		public String getTooltipText() {
			return tooltipText;
		}

		public void setLabel(String label) {
			this.label = label;
		}

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
