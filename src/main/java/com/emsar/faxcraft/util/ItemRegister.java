package com.emsar.faxcraft.util;

import com.emsar.faxcraft.ModMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);

    public static final RegistryObject<Item> bor = ITEMS.register("bor", () ->
            new Item(new Item.Properties().tab(ModMain.TAB))
    );

    public static void registerItems(IEventBus bus){
        ITEMS.register(bus);
    }
}
