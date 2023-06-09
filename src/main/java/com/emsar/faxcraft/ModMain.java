package com.emsar.faxcraft;

import com.emsar.faxcraft.gui.screen.CompressorScreen;
import com.emsar.faxcraft.recipe.CompressorRecipe;
import com.emsar.faxcraft.recipe.ModRecipes;
import com.emsar.faxcraft.util.BlockEntityRegister;
import com.emsar.faxcraft.util.BlockRegister;
import com.emsar.faxcraft.util.ItemRegister;
import com.emsar.faxcraft.util.MenuRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ModMain.MODID)
public class ModMain
{
    public static final String MODID = "faxcraft";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab TAB = new CreativeModeTab("main") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegister.bor.get());
        }
    };

    public ModMain()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegister.registerItems(bus);
        BlockRegister.registerBlocks(bus);
        BlockEntityRegister.registerEntities(bus);
        MenuRegister.registerMenus(bus);
        ModRecipes.register(bus);

        bus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event){
        MenuScreens.register(MenuRegister.compressor.get(), CompressorScreen::new);
    }
}
