package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.DoubleInput;
import com.justinyaodu.rgbcube.ui.FXHelper;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CubeViewUI extends ControlBox<RgbCube>
{
	public CubeViewUI(RgbCube rgbCube)
	{
		super("View", rgbCube);
	}

	protected Pane buildControls(RgbCube rgbCube)
	{
		GridPane gridPane = FXHelper.buildStyledGridPane();
		int rowIndex = 0;

		// create slider and input for each angle
		// add label, slider, and input

		Slider spinSlider = buildAngleSlider(0, 360, rgbCube.spinAngleProperty());
		DoubleInput spinInput = new DoubleInput(rgbCube.spinAngleProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Spin (deg)"), spinSlider, spinInput);

		Slider elevationSlider = buildAngleSlider(-90, 90, rgbCube.elevationAngleProperty());
		DoubleInput elevationInput = new DoubleInput(rgbCube.elevationAngleProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Elevation (deg)"), elevationSlider, elevationInput);

		Slider rollSlider = buildAngleSlider(0, 360, rgbCube.rollAngleProperty());
		DoubleInput rollInput = new DoubleInput(rgbCube.rollAngleProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Roll (deg)"), rollSlider, rollInput);

		Slider widthSlider = FXHelper.buildSlider(1, 100, 20, rgbCube.boxWidthProperty());
		DoubleInput widthInput = new DoubleInput(rgbCube.boxWidthProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Box Width (px)"), widthSlider, widthInput);

		return gridPane;
	}

	private static Slider buildAngleSlider(double min, double max, DoubleProperty property)
	{
		Slider slider = FXHelper.buildSlider(min, max, 0, property);

		// show ticks
		slider.setMajorTickUnit(90);
		slider.setMinorTickCount(5);
		slider.setShowTickMarks(true);

		// increment by one if using arrow keys etc.
		slider.setBlockIncrement(1);

		return slider;
	}
}
