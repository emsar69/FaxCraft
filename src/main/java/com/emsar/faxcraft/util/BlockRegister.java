package com.emsar.faxcraft.util;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.blocks.CompressorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MODID);


    public static final RegistryObject<Block> bor_block = register("bor_block", () -> new Block(
            BlockBehaviour.Properties.of(Material.METAL).strength(5f).requiresCorrectToolForDrops()
    ));

    public static final RegistryObject<Block> compressor = register("compressor", () -> new CompressorBlock(
            BlockBehaviour.Properties.of(Material.METAL).strength(5f).requiresCorrectToolForDrops().dynamicShape().noOcclusion()
                    .lightLevel((state) -> state.getValue(CompressorBlock.LIT) ? 10 : 0)
    ));

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supp){
        RegistryObject<T> toRet = BLOCKS.register(name, supp);
        ItemRegister.ITEMS.register(name, () -> new BlockItem(toRet.get(), new Item.Properties().tab(ModMain.TAB)));
        return toRet;
    }

    public static void registerBlocks(IEventBus bus){
        BLOCKS.register(bus);
    }
}
