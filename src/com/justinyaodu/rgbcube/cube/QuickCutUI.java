package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.FXHelper;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class QuickCutUI extends ControlBox<CubeMasker>
{
	public QuickCutUI(CubeMasker cubeMasker)
	{
		super("Quick Cut", cubeMasker);
	}

	@Override
	protected Pane buildControls(CubeMasker cubeMasker)
	{
		GridPane gridPane = FXHelper.buildStyledGridPane();
		int rowIndex = 0;

		// choose the face to cut from
		ComboBox<RgbCube.Face> faceComboBox = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(RgbCube.Face.values())));

		// set the cut depth
		Slider faceSlider = FXHelper.buildDiscreteSlider(0, CubeMasker.FACE_CUT_MAX_DEPTH, 0, null);

		// make the slider cut the face selected by the combo box
		faceComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) ->
		{
			// oldValue will be null the first time this is called
			if (oldValue != null)
			{
				// for simple view, make behaviour more intuitive by only cutting one face at a time
				// this disables the previous cut
				faceSlider.setValue(0);

				// unbind from old cut depth property
				cubeMasker.faceCutDepthProperty(oldValue).unbindBidirectional(faceSlider.valueProperty());
			}

			// bind to new cut depth property
			faceSlider.valueProperty().bindBidirectional(cubeMasker.faceCutDepthProperty(newValue));
		});

		// initialize value
		faceComboBox.getSelectionModel().select(0);

		gridPane.addRow(rowIndex++, FXHelper.boldText(FXHelper.rightAlignedText("Face Cut")), faceComboBox);
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Face Cut Depth"), faceSlider);

		// choose the corner to cut from
		ComboBox<RgbCube.Corner> cornerComboBox = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(RgbCube.Corner.values())));

		// set the cut depth
		Slider diagonalSlider = FXHelper.buildDiscreteSlider(0, CubeMasker.DIAGONAL_CUT_MAX_DEPTH, 0, null);
		Slider cubeSlider = FXHelper.buildDiscreteSlider(0, CubeMasker.CUBE_CUT_MAX_DEPTH, 0, null);

		// make the slider cut the face selected by the combo box
		cornerComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) ->
		{
			// oldValue will be null the first time this is called
			if (oldValue != null)
			{
				// for simple view, make behaviour more intuitive by only cutting one corner at a time
				// this disables the previous cuts
				diagonalSlider.setValue(0);
				cubeSlider.setValue(0);

				// unbind from old cut depth properties
				cubeMasker.diagonalCutDepthProperty(oldValue).unbindBidirectional(diagonalSlider.valueProperty());
				cubeMasker.cubeCutDepthProperty(oldValue).unbindBidirectional(cubeSlider.valueProperty());
			}

			// bind to new cut depth property
			diagonalSlider.valueProperty().bindBidirectional(cubeMasker.diagonalCutDepthProperty(newValue));
			cubeSlider.valueProperty().bindBidirectional(cubeMasker.cubeCutDepthProperty(newValue));
		});

		// initialize value
		cornerComboBox.getSelectionModel().select(RgbCube.Corner.RED);

		gridPane.addRow(rowIndex++, FXHelper.boldText(FXHelper.rightAlignedText("Corner Cut")), cornerComboBox);
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Angled Cut Depth"), diagonalSlider);
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Cube Cut Depth"), cubeSlider);

		return gridPane;
	}
}
