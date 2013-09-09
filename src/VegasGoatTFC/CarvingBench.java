package VegasGoatTFC;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CarvingBench extends Block
{
	private Icon topIcon;
	private Icon frontIcon;

	public CarvingBench(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(2.5F);
		setStepSound(Block.soundWoodFootstep);
		setUnlocalizedName("carvingBench");
		LanguageRegistry.addName(this, "Woodcarving Table");
	}

	public Icon getIcon(int side, int meta)
	{
		if(side == 1)
		{
			return topIcon;
		}
		if(side == 0)
		{
			return Block.planks.getBlockTextureFromSide(side);
		}
		if((side == 3) || (side == 5))
		{
			return blockIcon; // side icon
		}
		return frontIcon;
	}

	public void registerIcons(IconRegister registerer)
	{
		this.blockIcon = registerer.registerIcon("VegasGoatTFC:" + getUnlocalizedName() + "_side");
		this.topIcon = registerer.registerIcon("VegasGoatTFC:" + getUnlocalizedName() + "_top");
		this.frontIcon = registerer.registerIcon("VegasGoatTFC:" + getUnlocalizedName() + "_front");
	}

	public boolean onBlockActivated(
		World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		player.openGui(WoodworkingTFC.instance, 1, world, x, y, z);
		return true;
	}
}
