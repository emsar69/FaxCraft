package com.emsar.faxcraft;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ModMain.MODID)
public class ModMain
{
    public static final String MODID = "faxcraft";

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModMain()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
