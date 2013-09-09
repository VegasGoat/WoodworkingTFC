package VegasGoatTFC;

import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public class CarvingRecipe
{
	private static ArrayList<CarvingRecipe> recipeList = new ArrayList<CarvingRecipe>();

	public static void add(int outputID, int outputQuantity, boolean useWoodType, String... pattern)
	{
		int width = 0;
		int height = pattern.length;
		for(String row : pattern)
		{
			if(row.length() > width) width = row.length();
		}
		byte[] patternData = new byte[width * height];
		for(int yy = 0; yy < height; ++yy)
		{
			String row = pattern[yy];
			for(int xx = 0; xx < width; ++xx)
			{
				if((xx < row.length()) && (row.charAt(xx) != ' '))
				{
					patternData[yy * width + xx] = (byte) 1;
				}
				else
				{
					patternData[yy * width + xx] = (byte) 0;
				}
			}
		}

		recipeList.add(new CarvingRecipe(width, height, patternData, outputID, outputQuantity, useWoodType));
	}

	public static CarvingRecipe findMatch(byte[] data, int dataWidth)
	{
		for(CarvingRecipe recipe : recipeList)
		{
			if(recipe.matches(data, dataWidth))
			{
				return recipe;
			}
		}
		return null;
	}

	private int width;
	private int height;
	private byte[] pattern;
	private int outputID;
	private int outputQuantity;
	private boolean useWoodType;

	private CarvingRecipe(int width, int height, byte[] pattern, int outputID, int outputQuantity, boolean useWoodType)
	{
		this.width = width;
		this.height = height;
		this.pattern = pattern;
		this.outputID = outputID;
		this.outputQuantity = outputQuantity;
		this.useWoodType = useWoodType;
	}

	public boolean matches(byte[] data, int dataWidth)
	{
		int dataHeight = data.length / dataWidth;

		if(this.width > dataWidth) return false;

		if(this.height > dataHeight) return false;

		if((this.width == dataWidth) && (this.height == dataHeight))
		{
			return Arrays.equals(this.pattern, data);
		}

		for(int yy = 0; yy <= dataHeight - this.height; ++yy)
		{
			for(int xx = 0; xx <= dataWidth - this.width; ++xx)
			{
				if(smallerMatches(data, dataWidth, dataHeight, xx, yy))
				{
					return true;
				}
			}
		}
		return false;
	}

	public ItemStack generateResult(int woodType)
	{
		if(this.useWoodType)
		{
			return new ItemStack(this.outputID, this.outputQuantity, woodType);
		}
		else
		{
			return new ItemStack(this.outputID, this.outputQuantity, 0);
		}
	}

	private boolean smallerMatches(byte[] data, int dataWidth, int dataHeight, int startX, int startY)
	{
		int endX = startX + this.width;
		int endY = startY + this.height;

		for(int yy = 0; yy < dataHeight; ++yy)
		{
			for(int xx = 0; xx < dataWidth; ++xx)
			{
				byte dataValue = data[yy * dataWidth + xx];
				if((xx < startX) || (xx >= endX) || (yy < startY) || (yy >= endY))
				{
					// outside the recipe area, has to be 0
					if(dataValue != (byte) 0)
					{
						return false;
					}
				}
				else
				{
					// inside recipe area, must be equal
					int recipeX = xx - startX;
					int recipeY = yy - startY;
					byte recipeValue = this.pattern[recipeY * this.width + recipeX];
					if(dataValue != recipeValue)
					{
						return false;
					}
				}
			}
		}

		// didn't find any mismatches, all good
		return true;
	}
}
