package VegasGoatTFC;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumToolMaterial;
import net.minecraftforge.common.EnumHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import TFC.TFCItems;
import TFC.Core.CraftingManagerTFC;

@Mod(modid="WoodworkingTFC", name="Woodworking for TFC", version="0.6.0",
     dependencies="required-after:TerraFirmaCraft")
@NetworkMod(clientSideRequired=true, serverSideRequired=false,
            channels={PacketHandler.CHANNEL_NAME}, packetHandler=PacketHandler.class)
public class WoodworkingTFC
{
	// The instance of your mod that Forge uses.
	@Instance("WoodworkingTFC")
	public static WoodworkingTFC instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="VegasGoatTFC.ClientProxy", serverSide="VegasGoatTFC.CommonProxy")
	public static CommonProxy proxy;

	// Tool material
	public static EnumToolMaterial weaponizedWood;

	// Blocks
	public static Block carvingBench;

	// Items
	public static Item woodenClub;
	public static Item stoneWoodworkingKnifeHead;
	public static Item stoneWoodworkingKnife;
	public static Item woodenCarving;
	public static Item fingerPaint;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		// TODO: load configuration
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		// setup blocks
		int nextBlock = 3166;
		carvingBench = new CarvingBench(nextBlock++);
		GameRegistry.registerBlock(carvingBench, carvingBench.getUnlocalizedName());

		// setup items
		int nextItem = 13166;
		weaponizedWood = EnumHelper.addToolMaterial("weaponizedWood", 1, 40, 1.0f, 85, 1);
		woodenClub = new WoodenClub(nextItem++, weaponizedWood);
		stoneWoodworkingKnifeHead = new StoneWoodworkingKnifeHead(nextItem++);
		stoneWoodworkingKnife = new StoneWoodworkingKnife(nextItem++, TFCItems.IgExToolMaterial);
		woodenCarving = new WoodenCarving(nextItem++);
		fingerPaint = new FingerPaint(nextItem++);

		// knapping tool heads
		CraftingManagerTFC craftTFC = CraftingManagerTFC.getInstance();
		craftTFC.addRecipe(new ItemStack(stoneWoodworkingKnifeHead), new Object[] {
			" X", "XX", "XX", "XX", 'X', new ItemStack(TFCItems.FlatRock, 1, 32767)
		});

		// tools (head + stick)
		GameRegistry.addRecipe(new ItemStack(stoneWoodworkingKnife), "X", "I",
			'X', new ItemStack(stoneWoodworkingKnifeHead), 'I', new ItemStack(Item.stick));

		// club (wood carving)
		CarvingRecipe.add(woodenClub.itemID, 1, false,
			"   XX",
			"  XXX",
			" XXX",
			" XX",
			"X");

		// carving bench (wood carving)
		CarvingRecipe.add(carvingBench.blockID, 1, false,
			"XXXXX",
			"X   X",
			"X   X",
			"X   X",
			"X   X");

		// wooden bowl (wood carving)
		CarvingRecipe.add(Item.bowlEmpty.itemID, 4, false,
			"X  X",
			"XXXX");

		// finger paints
		int ii;
		for(ii=0; ii<16; ++ii)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(fingerPaint.itemID, 16, ii),
				new ItemStack(Item.bowlEmpty), new ItemStack(Item.dyePowder, 1, ii));
		}

		// gui handler
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());

		// crafting handler
		GameRegistry.registerCraftingHandler(new CraftingHandler());

		// rendering setup
		proxy.setupRenderers();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
