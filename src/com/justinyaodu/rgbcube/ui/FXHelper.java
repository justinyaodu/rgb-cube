package com.justinyaodu.rgbcube.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FXHelper
{
	private static final double SPACING = 5;
	private static final Insets PADDING = new Insets(SPACING);

	public static Text boldText(String string)
	{
		return boldText(new Text(string));
	}

	public static Text boldText(Text text)
	{
		text.setStyle("-fx-font-weight:bold");
		return text;
	}

	public static VBox styleVbox(VBox vBox)
	{
		vBox.setSpacing(SPACING);
		vBox.setPadding(PADDING);
		return vBox;
	}

	public static GridPane buildStyledGridPane()
	{
		return styleGridPane(new GridPane());
	}

	public static GridPane styleGridPane(GridPane gridPane)
	{
		gridPane.setHgap(SPACING * 2);
		gridPane.setVgap(SPACING);
		gridPane.setPadding(PADDING);
		return gridPane;
	}

	// create a GridPane with column headers
	public static GridPane gridPaneWithColHeaders(String... headers)
	{
		GridPane gridPane = buildStyledGridPane();
		for (int i = 0; i < headers.length; i++)
		{
			Text text = boldText(headers[i]);

			// right align first column's header
			if (i == 0)
			{
				GridPane.setHalignment(text, HPos.RIGHT);
			}

			gridPane.add(text, i, 0);
		}
		return gridPane;
	}

	public static GridPane gridPaneWithRowHeaders(String... headers)
	{
		GridPane gridPane = buildStyledGridPane();
		for (int i = 0; i < headers.length; i++)
		{
			Text text = new Text(headers[i]);

			// right align all headers
			GridPane.setHalignment(text, HPos.RIGHT);

			gridPane.add(text, 0, i);
		}
		return gridPane;
	}

	public static BorderPane titledPane(String title, Node content)
	{
		return styleBorderPane(new BorderPane(content, boldText(title), null, null, null));
	}

	public static BorderPane styleBorderPane(BorderPane borderPane)
	{
		borderPane.setPadding(PADDING);
		return borderPane;
	}

	public static Text rightAlignedText(String s)
	{
		Text text = new Text(s);
		GridPane.setHalignment(text, HPos.RIGHT);
		return text;
	}

	public static Slider buildSlider(double min, double max, double initial, DoubleProperty property)
	{
		Slider slider = new Slider(min, max, initial);
		if (property != null)
		{
			slider.valueProperty().bindBidirectional(property);
		}
		return slider;
	}

	public static CheckBox buildCheckBox(BooleanProperty property)
	{
		CheckBox checkBox = new CheckBox();
		checkBox.selectedProperty().bindBidirectional(property);
		return checkBox;
	}

	public static Slider buildDiscreteSlider(double min, double max, double initial, DoubleProperty property)
	{
		// make maxDepth a slightly larger double value
		// ugly hack to get the last slider tick to show up
		Slider slider = buildSlider(0, max + 0.01, initial, property);

		// show ticks
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setShowTickMarks(true);

		// increment by one if using arrow keys etc.
		slider.setBlockIncrement(1);

		// cut depths will sometimes jump up by one on cursor release when using snapToTicks,
		// because snapToTicks will round up sometimes; instead, add a listener and use floor
		slider.valueProperty().addListener((o, d1, d2) ->
		{
			slider.setValue(Math.floor(d2.doubleValue()));
		});

		return slider;
	}

	public static Text buildWrappedText(double width, String s)
	{
		Text text = new Text(s);
		text.setWrappingWidth(width);
		return text;
	}
}
