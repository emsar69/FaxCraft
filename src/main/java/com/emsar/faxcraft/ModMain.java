package com.emsar.faxcraft;

import com.emsar.faxcraft.util.ItemRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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

        MinecraftForge.EVENT_BUS.register(this);
    }
}
