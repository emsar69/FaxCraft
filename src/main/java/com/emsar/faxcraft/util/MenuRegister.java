package com.emsar.faxcraft.util;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.gui.menu.CompressorMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegister {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModMain.MODID);

    public static final RegistryObject<MenuType<CompressorMenu>> compressor = MENUS.register("compressor_menu", () -> IForgeMenuType.create(CompressorMenu::new));

    public static void registerMenus(IEventBus bus){
        MENUS.register(bus);
    }
}
