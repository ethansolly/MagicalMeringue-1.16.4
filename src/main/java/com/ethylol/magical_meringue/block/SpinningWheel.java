package com.ethylol.magical_meringue.block;

import com.ethylol.magical_meringue.item.recipes.SpinningWheelRecipe;
import com.ethylol.magical_meringue.tileentity.SpinningWheelTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class SpinningWheel extends ContainerBlock {

    private static final Property<Direction> FACING = HorizontalBlock.HORIZONTAL_FACING;

    public SpinningWheel(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof SpinningWheelTileEntity) {
                SpinningWheelTileEntity swte = (SpinningWheelTileEntity) tileentity;
                if (swte.getProgress() == 0) {
                    ItemStack stack = player.getHeldItem(handIn);
                    if (swte.isEmpty() && getRecipe(worldIn, stack).isPresent()) {
                        swte.setItemstack(stack.split(1));
                    }
                }
            }
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new SpinningWheelTileEntity();
    }

    public static Optional<SpinningWheelRecipe> getRecipe(World world, ItemStack stack) {
        SpinningWheelTileEntity temp = new SpinningWheelTileEntity();
        temp.setWorld(world);
        temp.setItemstack(stack);
        return temp.getCurrentRecipe();
    }
}
