package VegasGoatTFC;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import TFC.Items.ItemDyeCustom;

public class RenderWoodenCarving implements IItemRenderer
{
	// index where wood colors start
	public static final int WOOD_BASE = 1;

	// index where dye colors start
	public static final int DYE_BASE = 17;

	// color palette for wooden carvings
	public static final Color[] COLORS = new Color[] {
		new Color(0, 0, 0, 0),     // none (transparent)
		new Color(156, 127,  78),  // oak
		new Color( 55,  53,  40),  // aspen
		new Color(106,  92,  69),  // birch
		new Color( 77,  28,  16),  // chestnut
		new Color(225, 176, 130),  // douglas fir
		new Color( 50,  25,   8),  // hickory
		new Color(152,  97,  39),  // maple
		new Color(132,  66,  53),  // ash
		new Color(191, 171, 138),  // pine
		new Color(127,  73,  40),  // sequoia
		new Color(182, 109,  84),  // spruce
		new Color(185, 132,  55),  // sycamore
		new Color(193, 194, 181),  // white cedar
		new Color(140, 146,  89),  // white elm
		new Color( 40,  46,   5),  // willow
		new Color(143, 105, 131),  // kapok
		new Color(ItemDyeCustom.dyeColors[0]),  // dye
		new Color(ItemDyeCustom.dyeColors[1]),  // dye
		new Color(ItemDyeCustom.dyeColors[2]),  // dye
		new Color(ItemDyeCustom.dyeColors[3]),  // dye
		new Color(ItemDyeCustom.dyeColors[4]),  // dye
		new Color(ItemDyeCustom.dyeColors[5]),  // dye
		new Color(ItemDyeCustom.dyeColors[6]),  // dye
		new Color(ItemDyeCustom.dyeColors[7]),  // dye
		new Color(ItemDyeCustom.dyeColors[8]),  // dye
		new Color(ItemDyeCustom.dyeColors[9]),  // dye
		new Color(ItemDyeCustom.dyeColors[10]), // dye
		new Color(ItemDyeCustom.dyeColors[11]), // dye
		new Color(ItemDyeCustom.dyeColors[12]), // dye
		new Color(ItemDyeCustom.dyeColors[13]), // dye
		new Color(ItemDyeCustom.dyeColors[14]), // dye
		new Color(ItemDyeCustom.dyeColors[15]), // dye
	};

	public RenderWoodenCarving()
	{
		genImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	}

	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch(type)
		{
			case ENTITY:
			case EQUIPPED:
			case INVENTORY:
				// make sure we have all the necessary data to render properly
				if(item.hasTagCompound()
					&& item.getTagCompound().hasKey("carvingData"))
				{
					return true;
				}
		}
		return false;
	}
    
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		if(type.equals(ItemRenderType.ENTITY))
		{
			if(helper.equals(ItemRendererHelper.ENTITY_ROTATION) || helper.equals(ItemRendererHelper.ENTITY_BOBBING))
			{
				return true;
			}
		}
		return false;
	}
    
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		// already checked for these in handleRenderType, if they're gone now something went wrong
		NBTTagCompound tag = item.getTagCompound();
		byte[] carvingData = tag.getByteArray("carvingData");

		float minUV = 0.01F / 16.0F;
		float maxUV = 1.0F - minUV;

		// generate and bind texture
		int textureID = generateTexture(carvingData);
		if(textureID == -1)
		{
			RenderManager.instance.renderEngine.bindTexture("/gui/items.png");
		}
		else
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			RenderManager.instance.renderEngine.resetBoundTexture();
		}

		if(type == ItemRenderType.ENTITY)
		{
			if(RenderItem.renderInFrame)
			{
				GL11.glTranslatef(0.5F, -0.34375F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				GL11.glTranslatef(-0.5F, -0.25F, 0.0F);
			}

			ItemRenderer.renderItemIn2D(Tessellator.instance, maxUV, minUV, minUV, maxUV, 16, 16, 0.0625F);
		}
		else if(type == ItemRenderType.EQUIPPED)
		{
			ItemRenderer.renderItemIn2D(Tessellator.instance, maxUV, minUV, minUV, maxUV, 16, 16, 0.0625F);
		}
		else if(type == ItemRenderType.INVENTORY)
		{
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV( 0.0, 16.0, 0.0, minUV, maxUV);
			tessellator.addVertexWithUV(16.0, 16.0, 0.0, maxUV, maxUV);
			tessellator.addVertexWithUV(16.0,  0.0, 0.0, maxUV, minUV);
			tessellator.addVertexWithUV( 0.0,  0.0, 0.0, minUV, minUV);
			tessellator.draw();
		}

		RenderManager.instance.renderEngine.deleteTexture(textureID);
	}

	private int generateTexture(byte[] imageData)
	{
		for(int y = 0; y < 16; ++y)
		{
			for(int x = 0; x < 16; ++x)
			{
				int colorIndex = imageData[y * 16 + x];
				if(colorIndex > 0 && colorIndex < COLORS.length)
				{
					genImage.setRGB(x, y, COLORS[colorIndex].getRGB());
				}
				else
				{
					genImage.setRGB(x, y, COLORS[0].getRGB());
				}
			}
		}
		return RenderManager.instance.renderEngine.allocateAndSetupTexture(genImage);
	}

	private BufferedImage genImage;
}
