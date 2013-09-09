package VegasGoatTFC;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.common.registry.LanguageRegistry;
import TFC.Items.ItemWeapon;

public class WoodenClub extends ItemWeapon
{
	public WoodenClub(int id, EnumToolMaterial mat)
	{
		super(id, mat);
		setUnlocalizedName("woodenClub");
		LanguageRegistry.addName(this, "Wooden Club");
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("VegasGoatTFC:" + getUnlocalizedName());
	}
}
