package com.justinyaodu.rgbcube.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

// definitely a misuse of ControlBox, but makes life easier
class Help extends ControlBox<Void>
{
	Help()
	{
		super("Help", null);
	}

	@Override
	protected Pane buildControls(Void content)
	{
		double width = 350;

		VBox vBox = FXHelper.styleVbox(new VBox(
				FXHelper.buildWrappedText(width, "This program displays an interactive RGB cube."),
				FXHelper.buildWrappedText(width, "The cube display contains 16³ colored boxes,"
						+ " one for each three-digit hex color."
						+ " Hovering the mouse over a box displays a tooltip with the color's hex code;"
						+ " left-click to copy the hex code to the clipboard."
						+ " Right-clicking temporarily hides all of the other colored boxes,"
						+ " which can be useful for comparing a single color to the background."
						+ " When the cube is too large to fit on the screen,"
						+ " the view can be panned by dragging the mouse."),
				FXHelper.buildWrappedText(width, "The Main tab contains controls for rotating the cube,"
						+ " snapping the view to a specific face or corner, and zooming."
						+ " It also provides quick access to various ways of cutting the cube to expose the interior."
						+ " More comprehensive cut options can be found in the More Cuts tab."),
				FXHelper.buildWrappedText(width, "The Display Options tab contains various options"
						+ " controlling how boxes are displayed,"
						+ " as well as the colors of the background and global illumination.")));

		vBox.setAlignment(Pos.CENTER_LEFT);

		return vBox;
	}
}
