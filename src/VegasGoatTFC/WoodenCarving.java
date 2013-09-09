package VegasGoatTFC;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class WoodenCarving extends Item
{
	public WoodenCarving(int id)
	{
		super(id);
		setUnlocalizedName("woodenCarving");
		setMaxStackSize(1);
		setCreativeTab(null);
		LanguageRegistry.addName(this, "Wooden Carving");
	}

	@Override
	public void registerIcons(IconRegister registerer)
	{
		itemIcon = registerer.registerIcon("VegasGoatTFC:" + getUnlocalizedName());
	}
}
