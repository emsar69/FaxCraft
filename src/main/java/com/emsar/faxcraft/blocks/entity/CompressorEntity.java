package com.emsar.faxcraft.blocks.entity;

import com.emsar.faxcraft.ModMain;
import com.emsar.faxcraft.blocks.CompressorBlock;
import com.emsar.faxcraft.gui.menu.CompressorMenu;
import com.emsar.faxcraft.recipe.CompressorRecipe;
import com.emsar.faxcraft.util.BlockEntityRegister;
import com.emsar.faxcraft.util.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CompressorEntity extends BlockEntity implements MenuProvider {
    private int progress,maxProgress;
    public final ContainerData data;

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

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index){
                    case 0: return CompressorEntity.this.maxProgress;
                    case 1: return CompressorEntity.this.progress;
                }
                return 0;
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 1:
                        CompressorEntity.this.progress = value;
                }
            }

            @Override
            public int getCount() {
                return 0;
            }
        };
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        load(pkt.getTag());
    }
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
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
            if(stack.is(ItemRegister.bor.get()) || stack.is(Items.DIRT)){
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

    public boolean outputValid(ItemStack stack){
        if(handler.getStackInSlot(2).isEmpty()) return true;
        if(!handler.getStackInSlot(2).is(stack.getItem())) return false;
        if(stack.getCount()+handler.getStackInSlot(2).getCount() > handler.getStackInSlot(2).getMaxStackSize()) return false;
        return true;
    }

    public static <E extends CompressorEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, E blockEntity) {
        SimpleContainer inv = new SimpleContainer(blockEntity.handler.getSlots());
        for(int i = 0; i < blockEntity.handler.getSlots(); i++){
            inv.setItem(i, blockEntity.handler.getStackInSlot(i));
        }

        Optional<CompressorRecipe> recipe = level.getRecipeManager().getRecipeFor(CompressorRecipe.Type.INSTANCE, inv, level);

        Boolean condition = recipe.isPresent() && blockEntity.outputValid(recipe.get().getResultItem());

        if(condition){
            blockEntity.data.set(1, blockEntity.data.get(1)+1);
            if(blockEntity.data.get(1) >= blockEntity.data.get(0)){
                blockEntity.handler.extractItem(0, 1, false);
                blockEntity.handler.extractItem(1, 1, false);
                blockEntity.handler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(), blockEntity.handler.getStackInSlot(2).getCount()+1));
                blockEntity.data.set(1,0);
            }
        }else{
            blockEntity.data.set(1, 0);
        }

        if(!level.isClientSide()) {

            BlockState state = blockState.setValue(CompressorBlock.LIT, !blockState.getValue(CompressorBlock.LIT));
            level.setBlock(blockPos, state, 3);
            setChanged(level,blockPos,state);
        }

        BlockState state = blockState.setValue(CompressorBlock.LIT, condition);
        level.setBlock(blockPos, state, 3);
        setChanged(level,blockPos,state);
    }
}
