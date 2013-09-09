package VegasGoatTFC;

import net.minecraft.client.gui.inventory.GuiContainer;

public abstract class GuiCarvingBase extends GuiContainer
{
	private int tilesSize;
	private int tilesWide;
	private int tilesHigh;
	private int tilesStartX;
	private int tilesStartY;

	protected String[] messages;

	public GuiCarvingBase(ContainerCarvingBase container,
		int tilesSize, int tilesWide, int tilesHigh, int tilesStartX, int tilesStartY)
	{
		super(container);
		this.tilesSize = tilesSize;
		this.tilesWide = tilesWide;
		this.tilesHigh = tilesHigh;
		this.tilesStartX = tilesStartX;
		this.tilesStartY = tilesStartY;
		this.allowUserInput = false;
	}

	/**
	 * Called before tile drawing loop. Return false to skip tile drawing.
	 */
	protected boolean startDrawingTiles()
	{
		return true;
	}

	/**
	 * Called to draw each tile.
	 */
	protected abstract void drawTile(int tileX, int tileY, int x1, int y1, int x2, int y2, boolean highlight);

	/**
	 * Called after tile drawing loop.
	 */
	protected void finishDrawingTiles()
	{
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		// convert mouse to gui coordinates
		mouseX -= this.guiLeft;
		mouseY -= this.guiTop;

		// notify of start
		if(startDrawingTiles())
		{
			// draw tiles
			int y1 = this.tilesStartY;
			int y2 = y1 + this.tilesSize;
			for(int tileY = 0; tileY < this.tilesHigh; ++tileY)
			{
				int x1 = this.tilesStartX;
				int x2 = x1 + this.tilesSize;
				for(int tileX = 0; tileX < this.tilesWide; ++tileX)
				{
					boolean highlight = false;
					if(mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2)
					{
						highlight = true;
					}

					drawTile(tileX, tileY, x1, y1, x2, y2, highlight);

					x1 += this.tilesSize;
					x2 += this.tilesSize;
				}
				y1 += this.tilesSize;
				y2 += this.tilesSize;
			}

			finishDrawingTiles();
		}

		// show messages (if any)
		if(messages != null)
		{
			int msgX = 23;
			int msgY = 23;
			for(String msg : messages)
			{
				this.fontRenderer.drawStringWithShadow(msg, msgX, msgY, 0xFFFFFF);
				msgY += 16;
			}
		}
	}

	/**
	 * Called when a tile is clicked.
	 */
	protected abstract void tileClicked(int tileX, int tileY);

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button)
	{
		super.mouseClicked(mouseX, mouseY, button);
		// convert to grid coordinates
		int gridX = mouseX - this.guiLeft - this.tilesStartX;
		int gridY = mouseY - this.guiTop - this.tilesStartY;
		int maxX = this.tilesSize * this.tilesWide;
		int maxY = this.tilesSize * this.tilesHigh;
		if((button == 0) && (gridX >= 0) && (gridX < maxX) && (gridY >= 0) && (gridY < maxY))
		{
			int tileX = gridX / this.tilesSize;
			int tileY = gridY / this.tilesSize;
			tileClicked(tileX, tileY);
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
	}
}
