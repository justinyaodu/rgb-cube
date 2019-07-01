package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.DoubleInput;
import com.justinyaodu.rgbcube.ui.FXHelper;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.DrawMode;

import java.util.Arrays;

public class CubeOptionsUI extends ControlBox<RgbCube>
{

	public CubeOptionsUI(RgbCube rgbCube)
	{
		super("Cube Options", rgbCube);
	}

	@Override
	public Pane buildControls(RgbCube rgbCube)
	{
		GridPane gridPane = FXHelper.buildStyledGridPane();
		int rowIndex = 0;

		Slider gapSlider = FXHelper.buildSlider(0, 1, 0, rgbCube.boxGapProperty());
		DoubleInput gapInput = new DoubleInput(rgbCube.boxGapProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Box Gap (ratio)"), gapSlider, gapInput);

		CheckBox showAxesCheckBox = FXHelper.buildCheckBox(rgbCube.showAxesProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Show Axes"), showAxesCheckBox);

		ComboBox<DrawMode> boxDrawMode = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(DrawMode.values())));
		boxDrawMode.getSelectionModel().select(DrawMode.FILL);
		rgbCube.boxDrawModeProperty().bind(boxDrawMode.getSelectionModel().selectedItemProperty());
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Box Draw Mode"), boxDrawMode);

		return gridPane;
	}
}
