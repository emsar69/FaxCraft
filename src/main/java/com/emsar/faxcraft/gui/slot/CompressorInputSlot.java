package com.emsar.faxcraft.gui.slot;

import com.emsar.faxcraft.blocks.entity.CompressorEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CompressorInputSlot extends SlotItemHandler {
    final CompressorEntity entity;
    public CompressorInputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, CompressorEntity entity) {
        super(itemHandler, index, xPosition, yPosition);
        this.entity = entity;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return entity.isValid(getSlotIndex(), stack);
    }
}
