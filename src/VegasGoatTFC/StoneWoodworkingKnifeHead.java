package VegasGoatTFC;

import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.registry.LanguageRegistry;
import TFC.Enums.EnumSize;
import TFC.Enums.EnumWeight;
import TFC.Items.ISize;
import TFC.Items.ItemTerra;

public class StoneWoodworkingKnifeHead extends Item implements ISize
{
	public StoneWoodworkingKnifeHead(int id)
	{
		super(id);
		setMaxStackSize(4);
		setUnlocalizedName("stoneWoodworkingKnifeHead");
		LanguageRegistry.addName(this, "Stone Woodworking Knife Blade");
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
		return EnumSize.SMALL;
	}

	@Override
	public EnumWeight getWeight()
	{
		return EnumWeight.LIGHT;
	}

	@Override
	public boolean canStack()
	{
		return true;
	}
}
