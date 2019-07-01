package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.FXHelper;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CornerCutUI extends ControlBox<CubeMasker>
{
	static final String DIAGONAL_LABEL = "Angled Cut Depth";
	static final String CUBE_LABEL = "Cube Cut Depth";

	public CornerCutUI(CubeMasker cubeMasker)
	{
		super(null, cubeMasker);
	}

	@Override
	protected Pane buildControls(CubeMasker cubeMasker)
	{
		GridPane gridPane = FXHelper.gridPaneWithColHeaders("Corner", DIAGONAL_LABEL, CUBE_LABEL);

		for (int i = 0; i < RgbCube.Corner.values().length; i++)
		{
			int rowIndex = i + 1;
			RgbCube.Corner corner = RgbCube.Corner.values()[i];

			// add slider for each type of cut and bind to appropriate value
			Slider diagonalSlider = FXHelper.buildDiscreteSlider(0, CubeMasker.DIAGONAL_CUT_MAX_DEPTH, 0, cubeMasker.diagonalCutDepthProperty(corner));
			Slider cubeSlider = FXHelper.buildDiscreteSlider(0, CubeMasker.CUBE_CUT_MAX_DEPTH, 0, cubeMasker.cubeCutDepthProperty(corner));

			gridPane.addRow(rowIndex, FXHelper.rightAlignedText(corner.toString()), diagonalSlider, cubeSlider);
		}

		return gridPane;
	}
}
