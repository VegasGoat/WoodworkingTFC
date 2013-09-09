package VegasGoatTFC;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import TFC.TFCBlocks;

public class GuiBlockCarving extends GuiCarvingBase
{
	private ContainerBlockCarving carvingContainer;
	private int woodType;
	private Icon woodIcon;

	public GuiBlockCarving(EntityPlayer player, int woodType)
	{
		super(new ContainerBlockCarving(player, woodType), 16, 5, 5, 7, 7);
		this.carvingContainer = (ContainerBlockCarving) inventorySlots;
		this.woodType = woodType;
		this.xSize = 176;
		this.ySize = 174;
	}

	@Override
	protected boolean startDrawingTiles()
	{
		// show message if tool slot empty
		if(this.carvingContainer.getSlot(1).getHasStack())
		{
			this.messages = null;
		}
		else
		{
			this.messages = new String[] { "Place tool", "in slot" };
		}

		// turn off blending and lighting
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/terrain.png");
		this.woodIcon = TFCBlocks.Wood.getIcon(2, woodType);
		return true;
	}

	@Override
	protected void drawTile(int tileX, int tileY, int x1, int y1, int x2, int y2, boolean highlight)
	{
		if(this.carvingContainer.carvingData[tileY * 5 + tileX] == 1)
		{
			itemRenderer.renderIcon(x1, y1, this.woodIcon, x2-x1, y2-y1);
			if(highlight)
			{
				this.drawRect(x1, y1, x2, y2, 0x60FFFFFF);
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
		carvingContainer.sendCarvingAction(tileX, tileY);
	}
}
