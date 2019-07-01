package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.FXHelper;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class QuickFaceCutUI extends ControlBox<CubeMasker>
{
	public QuickFaceCutUI(CubeMasker cubeMasker)
	{
		super(null, cubeMasker);
	}

	@Override
	protected Pane buildControls(CubeMasker cubeMasker)
	{
		GridPane gridPane = FXHelper.gridPaneWithRowHeaders("Face", FaceCutUI.FACE_LABEL);

		// choose the face to cut from
		ComboBox<RgbCube.Face> faceComboBox = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(RgbCube.Face.values())));

		// set the cut depth
		Slider slider = FXHelper.buildDiscreteSlider(0, CubeMasker.FACE_CUT_MAX_DEPTH, 0, null);

		// make the slider cut the face selected by the combo box
		faceComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) ->
		{
			// oldValue will be null the first time this is called
			if (oldValue != null)
			{
				// for simple view, make behaviour more intuitive by only cutting one face at a time
				// this disables the previous cut
				slider.setValue(0);

				// unbind from old cut depth property
				cubeMasker.faceCutDepthProperty(oldValue).unbindBidirectional(slider.valueProperty());
			}

			// bind to new cut depth property
			cubeMasker.faceCutDepthProperty(newValue).bindBidirectional(slider.valueProperty());
		});

		// initialize value
		faceComboBox.getSelectionModel().select(0);

		gridPane.addColumn(1, faceComboBox, slider);

		return gridPane;
	}
}
