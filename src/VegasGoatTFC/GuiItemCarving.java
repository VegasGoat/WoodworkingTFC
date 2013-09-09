package VegasGoatTFC;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import TFC.TFCItems;

public class GuiItemCarving extends GuiCarvingBase
{
	private ContainerItemCarving carvingContainer;
	private byte[] drawImageData;
	private int drawColor;

	public GuiItemCarving(EntityPlayer player)
	{
		super(new ContainerItemCarving(player), 5, 16, 16, 7, 7);
		this.carvingContainer = (ContainerItemCarving) inventorySlots;
		this.xSize = 176;
		this.ySize = 174;
	}

	@Override
	protected boolean startDrawingTiles()
	{
		// show message if tool or carving slot empty
		if(this.carvingContainer.getSlot(1).getHasStack())
		{
			if(this.carvingContainer.getSlot(0).getHasStack())
			{
				this.messages = null;
			}
			else
			{
				this.messages = new String[] { "Place log", "or carving", "in slot" };
			}
		}
		else
		{
			this.messages = new String[] { "Place tool", "or paint", "in slot" };
		}

		// turn off blending and lighting
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);

		this.drawImageData = null;
		this.drawColor = 0;
		ItemStack carvedItem = carvingContainer.getSlot(0).getStack();
		if(carvedItem != null)
		{
			// check for attached image data
			if(carvedItem.hasTagCompound())
			{
				NBTTagCompound tag = carvedItem.getTagCompound();
				if(tag.hasKey("carvingData"))
				{
					this.drawImageData = tag.getByteArray("carvingData");
					return true;
				}
			}

			// check for log
			if(carvedItem.itemID == TFCItems.Logs.itemID)
			{
				this.drawColor = carvedItem.getItemDamage() + RenderWoodenCarving.WOOD_BASE;
				return true;
			}
		}

		// no item or wrong item, cancel drawing tiles
		GL11.glPopAttrib();
		return false;
	}

	@Override
	protected void drawTile(int tileX, int tileY, int x1, int y1, int x2, int y2, boolean highlight)
	{
		int colorIndex = 0;
		if(this.drawImageData != null)
		{
			colorIndex = this.drawImageData[tileY * 16 + tileX];
		}
		else
		{
			colorIndex = this.drawColor;
		}
		if((colorIndex > 0) && (colorIndex < RenderWoodenCarving.COLORS.length))
		{
			Color color = RenderWoodenCarving.COLORS[colorIndex];
			if(highlight)
			{
				this.drawRect(x1, y1, x2, y2, color.brighter().getRGB());
			}
			else
			{
				this.drawRect(x1, y1, x2, y2, color.getRGB());
			}
		}
	}

	@Override
	protected void finishDrawingTiles()
	{
		GL11.glPopAttrib();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/VegasGoatTFC/gui/woodworking.png");
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void tileClicked(int tileX, int tileY)
	{
		// don't bother unless there's an item in the slot
		ItemStack is = carvingContainer.getSlot(0).getStack();
		if(is != null)
		{
			carvingContainer.sendCarvingAction(tileX, tileY);
		}
	}
}
