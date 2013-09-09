package VegasGoatTFC;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;
import TFC.TFCBlocks;
import TFC.Enums.EnumSize;
import TFC.Enums.EnumWeight;
import TFC.Items.ISize;
import TFC.Items.ItemTerra;

public class StoneWoodworkingKnife extends ItemTool implements ISize
{
	private static final int USAGE_MULTIPLIER = 8;

	public StoneWoodworkingKnife(int id, EnumToolMaterial mat)
	{
		super(id, 0, mat, new Block[0]);
		setMaxDamage(mat.getMaxUses() * USAGE_MULTIPLIER);
		setMaxStackSize(1);
		setUnlocalizedName("stoneWoodworkingKnife");
		LanguageRegistry.addName(this, "Stone Woodworking Knife");
	}

	private boolean isPlacedWood(int blockID)
	{
		if((blockID == TFCBlocks.WoodVert.blockID)
			|| (blockID == TFCBlocks.WoodHoriz.blockID)
			|| (blockID == TFCBlocks.WoodHoriz2.blockID))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean onItemUse(
		ItemStack is, EntityPlayer player, World world,
		int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		// only works on placed wood blocks
		if(isPlacedWood(world.getBlockId(x, y, z)))
		{
			if(!world.isRemote)
			{
				// TODO: Allow for 2x carving
				// make sure there's no other placed wood next to it
				// (prevent use as fast wood removal)
				if(isPlacedWood(world.getBlockId(x+1, y, z))) return true;
				if(isPlacedWood(world.getBlockId(x-1, y, z))) return true;
				if(isPlacedWood(world.getBlockId(x, y+1, z))) return true;
				if(isPlacedWood(world.getBlockId(x, y-1, z))) return true;
				if(isPlacedWood(world.getBlockId(x, y, z+1))) return true;
				if(isPlacedWood(world.getBlockId(x, y, z-1))) return true;

				// save the wood type before destroying the block
				int woodType = world.getBlockMetadata(x, y, z);

				// check for permission to destroy block
				if(!TFCBlocks.Wood.canHarvestBlock(player, woodType)) return true;

				// destroy the block
				world.setBlockToAir(x, y, z);

				// open woodworking gui, pass block type as X coordinate
				player.openGui(WoodworkingTFC.instance, 0, world, woodType, 0, 0);
			}

			return true;
		}

		return false;
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("VegasGoatTFC:" + getUnlocalizedName());
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		ItemTerra.addSizeInformation(this, arraylist);
	}

	@Override
	public EnumSize getSize()
	{
		return EnumSize.MEDIUM;
	}

	@Override
	public EnumWeight getWeight()
	{
		return EnumWeight.LIGHT;
	}

	@Override
	public boolean canStack()
	{
		return false;
	}
}
