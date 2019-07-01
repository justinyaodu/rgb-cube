package com.justinyaodu.rgbcube.cube;

import com.justinyaodu.rgbcube.ui.ControlBox;
import com.justinyaodu.rgbcube.ui.FXHelper;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class CubeSnapperUI extends ControlBox<RgbCube>
{
	public CubeSnapperUI(RgbCube rgbCube)
	{
		super("Snap To", rgbCube);
	}

	protected Pane buildControls(RgbCube rgbCube)
	{
		GridPane gridPane = FXHelper.buildStyledGridPane();
		int rowIndex = 0;

		// combo box for selecting snap corner
		ComboBox<RgbCube.Corner> cornerComboBox = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(RgbCube.Corner.values())));
		cornerComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldCorner, newCorner) ->
		{
			if (newCorner != null)
			{
				rgbCube.spinAngleProperty().set(snapSpin(newCorner));
				rgbCube.elevationAngleProperty().set(snapElevation(newCorner));
			}
		});
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Corner"), cornerComboBox);

		// combo box for selecting snap face
		ComboBox<RgbCube.Face> faceComboBox = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(RgbCube.Face.values())));
		faceComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldFace, newFace) ->
		{
			if (newFace != null)
			{
				rgbCube.spinAngleProperty().set(snapSpin(newFace));
				rgbCube.elevationAngleProperty().set(snapElevation(newFace));
			}
		});
		gridPane.addRow(rowIndex++, FXHelper.rightAlignedText("Face"), faceComboBox);

		// add listeners to clear combo box selections if rotation values are changed afterwards
		addListeners(cornerComboBox, faceComboBox, rgbCube);

		return gridPane;
	}

	private void addListeners(ComboBox<RgbCube.Corner> cornerComboBox, ComboBox<RgbCube.Face> faceComboBox, RgbCube rgbCube)
	{
		double threshold = 0.0001;

		rgbCube.spinAngleProperty().addListener((o, oldNum, newNum) ->
		{
			if (cornerComboBox.getValue() != null
					&& Math.abs(snapSpin(cornerComboBox.getValue()) - newNum.doubleValue()) > threshold)
			{
				cornerComboBox.getSelectionModel().clearSelection();
			}
		});

		rgbCube.elevationAngleProperty().addListener((o, oldNum, newNum) ->
		{
			if (cornerComboBox.getValue() != null
					&& Math.abs(snapElevation(cornerComboBox.getValue()) - newNum.doubleValue()) > threshold)
			{
				cornerComboBox.getSelectionModel().clearSelection();
			}
		});

		rgbCube.spinAngleProperty().addListener((o, oldNum, newNum) ->
		{
			if (faceComboBox.getValue() != null
					&& Math.abs(snapSpin(faceComboBox.getValue()) - newNum.doubleValue()) > threshold)
			{
				faceComboBox.getSelectionModel().clearSelection();
			}
		});

		rgbCube.elevationAngleProperty().addListener((o, oldNum, newNum) ->
		{
			if (faceComboBox.getValue() != null
					&& Math.abs(snapElevation(faceComboBox.getValue()) - newNum.doubleValue()) > threshold)
			{
				faceComboBox.getSelectionModel().clearSelection();
			}
		});
	}

	private double snapElevation(RgbCube.Corner corner)
	{
		switch (corner)
		{
			case WHITE:
				return 90;
			case MAGENTA:
			case YELLOW:
			case CYAN:
				return RgbCube.ROT_DIAGONAL_VERTICAL - RgbCube.ROT_ISOMETRIC;
			case RED:
			case GREEN:
			case BLUE:
				return -1 * (RgbCube.ROT_DIAGONAL_VERTICAL - RgbCube.ROT_ISOMETRIC);
			default:
				return -90;
		}
	}

	private double snapSpin(RgbCube.Corner corner)
	{
		return corner.COLOR.getColor().getHue();
	}

	private double snapElevation(RgbCube.Face face)
	{
		switch (face)
		{
			case MAGENTA_YELLOW:
			case YELLOW_CYAN:
			case CYAN_MAGENTA:
				return 90 - RgbCube.ROT_DIAGONAL_VERTICAL;
			default:
				return -1 * (90 - RgbCube.ROT_DIAGONAL_VERTICAL);
		}
	}

	private double snapSpin(RgbCube.Face face)
	{
		switch (face)
		{
			case MAGENTA_YELLOW:
				return 0;
			case YELLOW_CYAN:
				return 120;
			case CYAN_MAGENTA:
				return 240;
			case RED_GREEN:
				return 60;
			case GREEN_BLUE:
				return 180;
			default:
				return 300;
		}
	}
}
