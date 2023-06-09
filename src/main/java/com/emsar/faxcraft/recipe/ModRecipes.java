package com.emsar.faxcraft.recipe;

import com.emsar.faxcraft.ModMain;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModMain.MODID);

    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> compressor = SERIALIZERS.register("compressor", () -> CompressorRecipe.Serializer.INSTANCE);

    public static void register(IEventBus bus){
        SERIALIZERS.register(bus);
    }
}
