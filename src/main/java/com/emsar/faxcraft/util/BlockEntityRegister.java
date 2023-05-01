package com.emsar.faxcraft.util;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.blocks.entity.CompressorEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModMain.MODID);

    public static final RegistryObject<BlockEntityType<CompressorEntity>> compressor = ENTITIES.register("test_entity", () ->
            BlockEntityType.Builder.of(CompressorEntity::new, BlockRegister.compressor.get()).build(null)
    );

    public static void registerEntities(IEventBus bus){
        ENTITIES.register(bus);
    }
}
