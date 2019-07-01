package com.justinyaodu.rgbcube.util;

// Miscellaneous utility methods
public class Util
{
	// makes the default enum string representation more palatable
	public static String enumToString(Enum e)
	{
		// replace underscores with strings, make lowercase
		StringBuilder s = new StringBuilder(e.name().replace('_', ' ').toLowerCase());

		// capitalize first letter of each word
		for (int i = 0; i < s.length(); i++)
		{
			if (i == 0 || s.charAt(i - 1) == ' ')
			{
				s.setCharAt(i, (char) (s.charAt(i) + 'A' - 'a'));
			}
		}
		return s.toString();
	}
}