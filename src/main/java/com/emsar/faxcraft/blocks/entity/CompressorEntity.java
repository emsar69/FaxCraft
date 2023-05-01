package com.emsar.faxcraft.blocks.entity;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.blocks.CompressorBlock;
import com.emsar.faxcraft.gui.menu.CompressorMenu;
import com.emsar.faxcraft.util.BlockEntityRegister;
import com.emsar.faxcraft.util.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompressorEntity extends BlockEntity implements MenuProvider {
    private int progress,maxProgress;

    public ItemStackHandler handler = new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.empty();
    public CompressorEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegister.compressor.get(), p_155229_, p_155230_);
        this.progress = 0;
        this.maxProgress = 50;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyOptional = LazyOptional.of(() -> handler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        p_187471_.put("inventory", handler.serializeNBT());
        super.saveAdditional(p_187471_);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        handler.deserializeNBT(p_155245_.getCompound("inventory"));
    }

    public void drop(){
        SimpleContainer container = new SimpleContainer(handler.getSlots());
        for(int i = 0; i < handler.getSlots(); i++){
            container.setItem(i, handler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, container);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return this.lazyOptional.cast();
        }

        return super.getCapability(cap, side);
    }

    public boolean isValid(int index, ItemStack stack){
        if(isInput(index)){
            if(stack.is(ItemRegister.bor.get())){
                return true;
            }
        }
        return false;
    }

    boolean isInput(int index){
        return index == 1 || index == 0;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("title.compressor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CompressorMenu(id, inv, this);
    }

    public static <E extends CompressorEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, E blockEntity) {
        if(level.isClientSide()) return;
        BlockState state = blockState.setValue(CompressorBlock.LIT, blockEntity.handler.getStackInSlot(1).is(ItemRegister.bor.get()));
        level.setBlock(blockPos, state, 3);
        ModMain.LOGGER.info(state.getValue(CompressorBlock.LIT) ? "true" : "false");
    }
}
