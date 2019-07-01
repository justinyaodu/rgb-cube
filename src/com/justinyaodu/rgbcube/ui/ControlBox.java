package com.justinyaodu.rgbcube.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class ControlBox<T> extends VBox
{
	public ControlBox(String name, T controlled)
	{
		FXHelper.styleVbox(this);

		Pane controls = buildControls(controlled);

		getChildren().add(name != null ? FXHelper.titledPane(name, controls) : controls);
	}

	protected abstract Pane buildControls(T content);
}
