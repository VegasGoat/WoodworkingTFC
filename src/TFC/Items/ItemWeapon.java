package TFC.Items;

import net.minecraft.item.ItemSword;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.client.renderer.texture.IconRegister;

public class ItemWeapon extends ItemSword
{
	public ItemWeapon(int id, EnumToolMaterial mat)
	{
		super(id, mat);
	}

	@Override
	public void registerIcons(IconRegister registerer)
	{
	}
}
