package com.justinyaodu.rgbcube.cube;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;

// logic to handle various ways of slicing the RGB cube to expose the inside
public class CubeMasker
{
	// depth of the cut for each corner/face, indexed by the ordinal of the corner/face enum
	private DoubleProperty[] cubeCutDepths = new DoubleProperty[RgbCube.Corner.values().length];
	private DoubleProperty[] diagonalCutDepths = new DoubleProperty[RgbCube.Corner.values().length];
	private DoubleProperty[] faceCutDepths = new DoubleProperty[RgbCube.Face.values().length];

	// manually calculated based on cube geometry
	// these values prevent the cube from disappearing entirely
	static final int CUBE_CUT_MAX_DEPTH = 15;
	static final int DIAGONAL_CUT_MAX_DEPTH = 45;
	static final int FACE_CUT_MAX_DEPTH = 15;

	public CubeMasker(RgbCube cube)
	{
		// construct all properties
		for (DoubleProperty[] depthPropertyArray :
				new DoubleProperty[][]{diagonalCutDepths, cubeCutDepths, faceCutDepths})
		{
			for (int i = 0; i < depthPropertyArray.length; i++)
			{
				depthPropertyArray[i] = new SimpleDoubleProperty();
			}
		}

		// create and attach bindings for visibility of each cube
		for (int r = 0; r < Color12.CHANNEL_DEPTH; r++)
		{
			for (int g = 0; g < Color12.CHANNEL_DEPTH; g++)
			{
				for (int b = 0; b < Color12.CHANNEL_DEPTH; b++)
				{
					cube.boxVisibleProperty(r, g, b).bind(getBoxVisibleBinding(r, g, b));
				}
			}
		}
	}

	// helper method to get a binding for whether a box should be visible
	private BooleanBinding getBoxVisibleBinding(int r, int g, int b)
	{
		List<BooleanBinding> bindings = new ArrayList<>();

		// add bindings for corner and face cuts
		for (RgbCube.Corner corner : RgbCube.Corner.values())
		{
			bindings.add(new CubeCutVisibleBinding(corner, r, g, b));
			bindings.add(new DiagonalCutVisibleBinding(corner, r, g, b));
		}
		for (RgbCube.Face face : RgbCube.Face.values())
		{
			bindings.add(new FaceCutVisibleBinding(face, r, g, b));
		}

		// evaluate logical and over the value of all bindings
		BooleanBinding binding = Bindings.and(bindings.get(0), bindings.get(1));
		for (int i = 2; i < bindings.size(); i++)
		{
			binding = Bindings.and(binding, bindings.get(i));
		}

		return binding;
	}

	// abstract class for whether a box should be visible with a cut of a certain depth
	private abstract class CutVisibleBinding extends BooleanBinding
	{
		final DoubleProperty CUT_DEPTH_PROPERTY;
		int MAX_VISIBLE_DEPTH;

		CutVisibleBinding(Enum relativeTo, int r, int g, int b)
		{
			CUT_DEPTH_PROPERTY = getCutDepthPropertyArray()[relativeTo.ordinal()];
			bind(CUT_DEPTH_PROPERTY);

			// store max visible depth
			MAX_VISIBLE_DEPTH = getMaxVisibleDepth(r, g, b, relativeTo);
		}

		// gets the cut depth property for the cut type and specific corner/face
		abstract DoubleProperty[] getCutDepthPropertyArray();

		// returns the maximum cut depth at which a cube at (r,g,b) is still visible
		abstract int getMaxVisibleDepth(int r, int g, int b, Enum relativeTo);

		@Override
		protected boolean computeValue()
		{
			// check if cut is shallower than this cube's depth
			return CUT_DEPTH_PROPERTY.intValue() <= MAX_VISIBLE_DEPTH;
		}
	}

	// abstract class for cuts originating from a corner
	private abstract class CornerCutVisibleBinding extends CutVisibleBinding
	{
		CornerCutVisibleBinding(RgbCube.Corner corner, int r, int g, int b)
		{
			super(corner, r, g, b);
		}
	}

	// whether a box should be visible with a cube-shaped cut
	private class CubeCutVisibleBinding extends CornerCutVisibleBinding
	{
		CubeCutVisibleBinding(RgbCube.Corner corner, int r, int g, int b)
		{
			super(corner, r, g, b);
		}

		@Override
		int getMaxVisibleDepth(int r, int g, int b, Enum relativeTo)
		{
			RgbCube.Corner corner = (RgbCube.Corner) relativeTo;

			// maximum of distances along each axis
			return Math.max(Math.max(Math.abs(r - corner.COLOR.R), Math.abs(g - corner.COLOR.G)), Math.abs(b - corner.COLOR.B));
		}

		@Override
		DoubleProperty[] getCutDepthPropertyArray()
		{
			return cubeCutDepths;
		}
	}

	// whether a box should be visible with a diagonal cut
	private class DiagonalCutVisibleBinding extends CornerCutVisibleBinding
	{
		DiagonalCutVisibleBinding(RgbCube.Corner corner, int r, int g, int b)
		{
			super(corner, r, g, b);
		}

		@Override
		int getMaxVisibleDepth(int r, int g, int b, Enum relativeTo)
		{
			RgbCube.Corner corner = (RgbCube.Corner) relativeTo;

			// taxicab distance
			return Math.abs(r - corner.COLOR.R) + Math.abs(g - corner.COLOR.G) + Math.abs(b - corner.COLOR.B);
		}

		@Override
		DoubleProperty[] getCutDepthPropertyArray()
		{
			return diagonalCutDepths;
		}
	}

	// whether a box should be visible with a face cut
	private class FaceCutVisibleBinding extends CutVisibleBinding
	{
		FaceCutVisibleBinding(RgbCube.Face face, int r, int g, int b)
		{
			super(face, r, g, b);
		}

		@Override
		DoubleProperty[] getCutDepthPropertyArray()
		{
			return faceCutDepths;
		}

		@Override
		int getMaxVisibleDepth(int r, int g, int b, Enum relativeTo)
		{
			RgbCube.Face face = (RgbCube.Face) relativeTo;

			// get the position along the cut axis
			int posAlongAxis;
			switch (face.CHANNEL)
			{
				case R:
					posAlongAxis = r;
					break;
				case G:
					posAlongAxis = g;
					break;
				default:
					posAlongAxis = b;
					break;
			}

			// linear distance
			return Math.abs(posAlongAxis - face.POSITION);
		}
	}

	// getters for properties

	DoubleProperty cubeCutDepthProperty(RgbCube.Corner corner)
	{
		return cubeCutDepths[corner.ordinal()];
	}

	DoubleProperty diagonalCutDepthProperty(RgbCube.Corner corner)
	{
		return diagonalCutDepths[corner.ordinal()];
	}

	DoubleProperty faceCutDepthProperty(RgbCube.Face face)
	{
		return faceCutDepths[face.ordinal()];
	}
}
