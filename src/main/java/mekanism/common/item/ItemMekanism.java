package mekanism.common.item;

import java.util.Locale;
import mekanism.common.Mekanism;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemMekanism extends Item implements IItemMekanism {

    public ItemMekanism(String name) {
        super();
        //Ensure the name is lower case as with concatenating with values from enums it may not be
        name = name.toLowerCase(Locale.ROOT);
        setCreativeTab(Mekanism.tabMekanism);
        setTranslationKey(Mekanism.MODID + "." + name);
        setRegistryName(new ResourceLocation(Mekanism.MODID, name));
    }

    @Override
    public void registerOreDict() {

    }
}