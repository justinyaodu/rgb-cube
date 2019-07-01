package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.util.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

// the RGB cube, which contains 3D boxes for each RGB value
public class RgbCube extends Group
{
	// width of each box in pixel units
	private DoubleProperty boxWidth = new SimpleDoubleProperty(20);

	// gap between boxes as a multiple of the distance between centers of adjacent boxes
	private DoubleProperty boxGap = new SimpleDoubleProperty(0);

	// 3D array of BooleanProperties for whether each box is visible
	// elements are added when each RgbBox is constructed
	private BooleanProperty[][][] boxVisible =
			new BooleanProperty[Color12.CHANNEL_DEPTH][Color12.CHANNEL_DEPTH][Color12.CHANNEL_DEPTH];

	// rotation angles
	private DoubleProperty elevationAngle = new SimpleDoubleProperty();
	private DoubleProperty rollAngle = new SimpleDoubleProperty();
	private DoubleProperty spinAngle = new SimpleDoubleProperty();

	// whether axes are shown
	private BooleanProperty showAxes = new SimpleBooleanProperty();

	// if only showing one box, which one is it?
	private ObjectProperty<RgbBox> singleBox = new SimpleObjectProperty<>(null);

	private ObjectProperty<DrawMode> boxDrawMode = new SimpleObjectProperty<>(DrawMode.LINE);

	// compress the entire cube along the Z axis so camera clipping isn't a concern
	private static final double Z_SCALE = 0.01;

	// maximum visible width
	private DoubleProperty maxSize = new SimpleDoubleProperty();

	{
		maxSize.bind(boxWidth.multiply(Color12.CHANNEL_DEPTH * Math.sqrt(3)));
	}

	// maximum extension from the origin along the Z axis
	private DoubleProperty maxExtensionZ = new SimpleDoubleProperty();

	{
		maxExtensionZ.bind(maxSize.multiply(Z_SCALE / 2));
	}

	// angle to rotate a cube so that one of the diagonals is vertical
	public static final double ROT_DIAGONAL_VERTICAL = Math.asin((Math.sqrt(3) - 1.0 / Math.sqrt(3)) / Math.sqrt(2)) * 180 / Math.PI;

	// angle to rotate a cube from flat into isometric view
	public static final double ROT_ISOMETRIC = Math.asin(1 / Math.sqrt(3)) * 180 / Math.PI;

	// specifies a corner of the RGB cube
	enum Corner
	{
		WHITE(true, true, true),
		MAGENTA(true, false, true),
		YELLOW(true, true, false),
		CYAN(false, true, true),
		RED(true, false, false),
		GREEN(false, true, false),
		BLUE(false, false, true),
		BLACK(false, false, false);

		final Color12 COLOR;

		Corner(boolean r, boolean g, boolean b)
		{
			COLOR = new Color12(
					r ? Color12.CHANNEL_DEPTH - 1 : 0,
					g ? Color12.CHANNEL_DEPTH - 1 : 0,
					b ? Color12.CHANNEL_DEPTH - 1 : 0);
		}

		// make toString nicer
		@Override
		public String toString()
		{
			return Util.enumToString(this);
		}
	}

	// specifies a face of the RGB cube
	enum Face
	{
		MAGENTA_YELLOW(Color12.Channel.R, false),
		YELLOW_CYAN(Color12.Channel.G, false),
		CYAN_MAGENTA(Color12.Channel.B, false),
		GREEN_BLUE(Color12.Channel.R, true),
		BLUE_RED(Color12.Channel.G, true),
		RED_GREEN(Color12.Channel.B, true);

		// channel corresponding to the axis this face is perpendicular to
		final Color12.Channel CHANNEL;

		// position of this face along the axis
		final int POSITION;

		Face(Color12.Channel channel, boolean darker)
		{
			CHANNEL = channel;
			POSITION = darker ? 0 : Color12.CHANNEL_DEPTH - 1;
		}

		// make toString nicer
		@Override
		public String toString()
		{
			return Util.enumToString(this);
		}
	}

	public RgbCube()
	{
		// nested groups for more intuitive rotation behaviour
		Group spinGroup = new Group(buildBoxes());
		Group elevationGroup = new Group(spinGroup);
		Group rollGroup = new Group(elevationGroup);

		// rotate about the line going through white and black, which is now the global Y-axis
		Rotate rotateSpin = buildBoundRotate(Rotate.Y_AXIS, spinAngle);
		spinGroup.getTransforms().add(rotateSpin);

		// rotate about the global horizontal axis
		Rotate rotateElevation = buildBoundRotate(Rotate.X_AXIS, elevationAngle);
		elevationGroup.getTransforms().add(rotateElevation);

		Rotate rotateRoll = buildBoundRotate(Rotate.Z_AXIS, rollAngle);
		rollGroup.getTransforms().addAll(rotateRoll);

		getChildren().add(rollGroup);

		getTransforms().add(new Scale(1, 1, Z_SCALE, 0, 0, 0));
	}

	private Group buildBoxes()
	{
		// container group, which will later be rotated
		Group boxes = new Group();

		// add boxes for each color value
		for (int r = 0; r < Color12.CHANNEL_DEPTH; r++)
		{
			for (int g = 0; g < Color12.CHANNEL_DEPTH; g++)
			{
				for (int b = 0; b < Color12.CHANNEL_DEPTH; b++)
				{
					boxes.getChildren().add(new RgbBox(r, g, b));
				}
			}
		}

		boxes.getChildren().add(buildAxes());

		// rotate so that white is on top, black is on bottom, red is in front
		Rotate rotateX = new Rotate(ROT_DIAGONAL_VERTICAL, Rotate.X_AXIS);
		Rotate rotateY = new Rotate(315, Rotate.Y_AXIS);
		Rotate rotateZ = new Rotate(270, Rotate.Z_AXIS);
		boxes.getTransforms().addAll(rotateX, rotateY, rotateZ);

		return boxes;
	}

	private static Rotate buildBoundRotate(Point3D axis, DoubleProperty angle)
	{
		Rotate rotate = new Rotate(angle.get(), axis);
		rotate.angleProperty().bind(angle);
		return rotate;
	}

	private class RgbBox extends Box
	{
		private int r;
		private int g;
		private int b;
		private Color12 color12;

		RgbBox(int r, int g, int b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
			color12 = new Color12(r, g, b);

			// set box color
			setMaterial(new PhongMaterial(color12.getColor()));

			// handle bindings for translation and scale
			initTransformBindings();

			// set up bindings for when this box should be visible
			initVisibilityBindings();

			// set up mouse event handling
			initMouseEvents();

			// add tooltip
			initTooltip();

			drawModeProperty().bind(boxDrawMode);
		}

		private void initTransformBindings()
		{
			// bind width, height, and depth properties
			for (DoubleProperty doubleProperty : new DoubleProperty[]{widthProperty(), heightProperty(), depthProperty()})
			{
				doubleProperty.bind(boxWidth.multiply(boxGap.multiply(-1).add(1)));
			}

			// translate relative to parent so that the cube is in the correct position in the RGB cube,
			// and add an offset so the RGB cube is centered about the origin
			translateXProperty().bind(boxWidth.multiply(r - (Color12.CHANNEL_DEPTH / 2) + 0.5));
			translateYProperty().bind(boxWidth.multiply(g - (Color12.CHANNEL_DEPTH / 2) + 0.5));
			translateZProperty().bind(boxWidth.multiply(b - (Color12.CHANNEL_DEPTH / 2) + 0.5));
		}

		private void initVisibilityBindings()
		{
			// binding to hide this box if only showing one box and it isn't this one
			BooleanBinding singleBoxMode = new BooleanBinding()
			{
				{
					bind(singleBox);
				}

				@Override
				protected boolean computeValue()
				{
					return singleBox.getValue() == null || singleBox.getValue().equals(RgbBox.this);
				}
			};

			// add visibility property to publicly accessible 3D array so it can be interacted with externally
			BooleanProperty hideExternal = new SimpleBooleanProperty(true);
			boxVisible[r][g][b] = hideExternal;

			// only show box if both internal and external conditions met
			visibleProperty().bind(Bindings.and(singleBoxMode, hideExternal));
		}

		private void initMouseEvents()
		{
			// copy hex color code to clipboard on left click
			setOnMouseClicked(mouseEvent ->
			{
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					ClipboardContent content = new ClipboardContent();
					content.putString(color12.toString());
					Clipboard.getSystemClipboard().setContent(content);
				}
			});

			// on right click held, hide everything except for this box
			setOnMousePressed(mouseEvent ->
			{
				if (mouseEvent.getButton().equals(MouseButton.SECONDARY))
				{
					singleBox.set(this);
				}
			});

			// go back to showing everything if right mouse button is released
			setOnMouseReleased(mouseEvent ->
			{
				if (this.equals(singleBox.get()) && mouseEvent.getButton().equals(MouseButton.SECONDARY))
				{
					singleBox.set(null);
				}
			});
		}

		private void initTooltip()
		{
			Tooltip tooltip = new Tooltip(String.format("%s (click to copy to clipboard)", color12.toString()));

			// add a small square of the current color inside the tooltip
			tooltip.setGraphic(new Rectangle(10, 10, color12.getColor()));

			Tooltip.install(this, tooltip);
		}
	}

	private Group buildAxes()
	{
		// extend beyond edges of cube by one box width
		DoubleBinding length = boxWidth.multiply(Color12.CHANNEL_DEPTH + 2);

		// initialize other dimensions
		Box r = new Box(1, 1, 1);
		Box g = new Box(1, 1, 1);
		Box b = new Box(1, 1, 1);

		// bind appropriate property for the dimension that the box should be longest in
		r.widthProperty().bind(length);
		g.heightProperty().bind(length);
		b.depthProperty().bind(length);

		// set colours
		r.setMaterial(new PhongMaterial(Color.RED));
		g.setMaterial(new PhongMaterial(Color.GREEN));
		b.setMaterial(new PhongMaterial(Color.BLUE));

		Group axes = new Group(r, g, b);
		axes.setVisible(false);

		// only show axes if external setting is set and we are not currently in the single box view mode
		axes.visibleProperty().bind(Bindings.and(showAxes, new BooleanBinding()
		{
			{
				bind(singleBox);
			}

			@Override
			protected boolean computeValue()
			{
				return singleBox.get() == null;
			}
		}));

		return axes;
	}

	// getters for all properties

	DoubleProperty elevationAngleProperty()
	{
		return elevationAngle;
	}

	DoubleProperty rollAngleProperty()
	{
		return rollAngle;
	}

	DoubleProperty spinAngleProperty()
	{
		return spinAngle;
	}

	public DoubleProperty maxSizeProperty()
	{
		return maxSize;
	}

	public DoubleProperty maxExtensionZProperty()
	{
		return maxExtensionZ;
	}

	DoubleProperty boxGapProperty()
	{
		return boxGap;
	}

	DoubleProperty boxWidthProperty()
	{
		return boxWidth;
	}

	BooleanProperty boxVisibleProperty(int r, int g, int b)
	{
		return boxVisible[r][g][b];
	}

	BooleanProperty showAxesProperty()
	{
		return showAxes;
	}

	ObjectProperty<DrawMode> boxDrawModeProperty()
	{
		return boxDrawMode;
	}
}
