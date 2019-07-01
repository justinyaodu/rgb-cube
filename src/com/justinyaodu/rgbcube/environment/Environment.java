package com.justinyaodu.rgbcube.environment;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class Environment
{
	private ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(Color.WHITE);
	private ObjectProperty<Color> lightColor = new SimpleObjectProperty<>(Color.WHITE);

	public ObjectProperty<Color> backgroundColorProperty()
	{
		return backgroundColor;
	}

	public ObjectProperty<Color> lightColorProperty()
	{
		return lightColor;
	}
}
