package com.emsar.faxcraft.blocks;

import com.emsar.faxcraft.blocks.entity.CompressorEntity;
import com.emsar.faxcraft.util.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CompressorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public CompressorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CompressorBlock.LIT, false));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!level.isClientSide()){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof CompressorEntity){
                NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) entity, blockPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(player.isLocalPlayer());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return this.defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_60528_, Mirror p_60529_) {
        return p_60528_.rotate(p_60529_.getRotation(p_60528_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
        p_49915_.add(LIT);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState p_60518_, boolean p_60519_) {
        if(blockState.getBlock() != p_60518_.getBlock()){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof CompressorEntity){
                ((CompressorEntity)entity).drop();
            }
        }
        super.onRemove(blockState, level, blockPos, p_60518_, p_60519_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CompressorEntity(p_153215_, p_153216_);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegister.compressor.get(), CompressorEntity::tick);
    }
}
