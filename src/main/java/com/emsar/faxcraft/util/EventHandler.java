package com.emsar.faxcraft.util;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.recipe.CompressorRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModMain.MODID)
public class EventHandler {
    @SubscribeEvent
    public static void RecipeRegister(RegistryEvent.Register<RecipeSerializer<?>> event){
        Registry.register(Registry.RECIPE_TYPE, CompressorRecipe.Type.ID, CompressorRecipe.Type.INSTANCE);
    }
}
