package com.emsar.faxcraft.gui.menu;

import com.emsar.faxcraft.blocks.entity.CompressorEntity;
import com.emsar.faxcraft.gui.slot.CompressorInputSlot;
import com.emsar.faxcraft.gui.slot.CompressorOutputSlot;
import com.emsar.faxcraft.util.MenuRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;

public class CompressorMenu extends AbstractContainerMenu {
    private final CompressorEntity entity;
    private final Inventory inv;

    public CompressorMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory, inventory.player.level.getBlockEntity(friendlyByteBuf.readBlockPos()));
    }
    public CompressorMenu(int id, Inventory inv, BlockEntity entity) {
        super(MenuRegister.compressor.get(), id);
        this.entity = (CompressorEntity)entity;
        this.inv = inv;

        addPlayerHotBar(inv);
        addPlayerInventory(inv);

        this.entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
            this.addSlot(new CompressorInputSlot(iItemHandler,0,21,32, this.entity));
            this.addSlot(new CompressorInputSlot(iItemHandler, 1, 65, 32, this.entity));
            this.addSlot(new CompressorOutputSlot(iItemHandler, 2, 115, 32));
        });
    }

    public void addPlayerInventory(Inventory inv){
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public void addPlayerHotBar(Inventory inv){
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
