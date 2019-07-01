package com.justinyaodu.rgbcube.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// definitely a misuse of ControlBox, but makes life easier
class About extends ControlBox<Void>
{
	About()
	{
		super("About Interactive RGB Cube", null);
	}

	@Override
	protected Pane buildControls(Void content)
	{
		return FXHelper.styleVbox(new VBox(
				new Text("Created by Justin Yao Du."),
				new Text("See github.com/justinyaodu/rgb-cube/")));
	}
}
