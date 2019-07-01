package com.justinyaodu.rgbcube.cube;

import javafx.scene.paint.Color;

public class Color12
{
	public enum Channel
	{
		R(0),
		G(1),
		B(2);

		Channel(int index)
		{
			INDEX = index;
		}

		public final int INDEX;
	}

	static final int CHANNEL_DEPTH = 16;

	final int R;
	final int G;
	final int B;

	Color12(int r, int g, int b)
	{
		if (r < 0 || g < 0 || b < 0 || r >= CHANNEL_DEPTH || g >= CHANNEL_DEPTH || b >= CHANNEL_DEPTH)
		{
			throw new IllegalArgumentException();
		}

		this.R = r;
		this.G = g;
		this.B = b;
	}

	public Color getColor()
	{
		return Color.web(toString());
	}

	@Override
	public String toString()
	{
		// repeat each digit twice
		return String.format("#%x%x%x%x%x%x", R, R, G, G, B, B);
	}
}
