package VegasGoatTFC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
	{
		switch(id)
		{
			// Crafting with single wood block
			case 0:
				return new ContainerBlockCarving(player, x);

			// Carving wooden items
			case 1:
				return new ContainerItemCarving(player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(id)
		{
			// Crafting with single wood block
			case 0:
				return new GuiBlockCarving(player, x);

			// Carving wooden items
			case 1:
				return new GuiItemCarving(player);
		}
		return null;
	}
}

