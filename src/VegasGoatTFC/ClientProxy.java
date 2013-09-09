package VegasGoatTFC;

import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
	@Override
	public void setupRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(WoodworkingTFC.woodenCarving.itemID, new RenderWoodenCarving());
	}
}

