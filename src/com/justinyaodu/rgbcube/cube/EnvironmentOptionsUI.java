package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.environment.Environment;
import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.FXHelper;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class EnvironmentOptionsUI extends ControlBox<Environment>
{
	public EnvironmentOptionsUI(Environment environment)
	{
		super("Environment Options", environment);
	}

	@Override
	public Pane buildControls(Environment environment)
	{
		GridPane gridPane = FXHelper.buildStyledGridPane();
		int rowIndex = 0;

		ColorPicker backgroundColorPicker = new ColorPicker();
		backgroundColorPicker.valueProperty().bindBidirectional(environment.backgroundColorProperty());

		ColorPicker lightColorPicker = new ColorPicker();
		lightColorPicker.valueProperty().bindBidirectional(environment.lightColorProperty());

		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Background Color"), backgroundColorPicker);
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Illumination Color"), lightColorPicker);

		return gridPane;
	}
}
