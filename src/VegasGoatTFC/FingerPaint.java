package VegasGoatTFC;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.registry.LanguageRegistry;
import TFC.Enums.EnumSize;
import TFC.Enums.EnumWeight;
import TFC.Items.ISize;
import TFC.Items.ItemTerra;

public class FingerPaint extends Item implements ISize
{
	public FingerPaint(int id)
	{
		super(id);
		setMaxStackSize(64);
		setHasSubtypes(true);
		setUnlocalizedName("fingerPaint");
		LanguageRegistry.addName(this, "Finger Paint");
	}

	@Override
	public Icon getIconFromDamage(int damage)
	{
		// use dye images for now (TFC replaces with its own class)
		return Item.itemsList[Item.dyePowder.itemID].getIconFromDamage(damage);
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		ItemTerra.addSizeInformation(this, arraylist);
	}

	@Override
	public EnumSize getSize()
	{
		return EnumSize.TINY;
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
