package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.FXHelper;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class FaceCutUI extends ControlBox<CubeMasker>
{
	static final String FACE_LABEL = "Face Cut Depth";

	public FaceCutUI(CubeMasker cubeMasker)
	{
		super(null, cubeMasker);
	}

	@Override
	protected Pane buildControls(CubeMasker cubeMasker)
	{
		GridPane gridPane = FXHelper.gridPaneWithColHeaders("Face", FACE_LABEL);

		for (int i = 0; i < RgbCube.Face.values().length; i++)
		{
			int rowIndex = i + 1;
			RgbCube.Face face = RgbCube.Face.values()[i];

			// create slider and bind to appropriate value
			Slider slider = FXHelper.buildDiscreteSlider(0, CubeMasker.FACE_CUT_MAX_DEPTH, 0, cubeMasker.faceCutDepthProperty(face));
			cubeMasker.faceCutDepthProperty(face).bindBidirectional(slider.valueProperty());

			gridPane.addRow(rowIndex, FXHelper.rightAlignedText(face.toString()), slider);
		}

		return gridPane;
	}
}
