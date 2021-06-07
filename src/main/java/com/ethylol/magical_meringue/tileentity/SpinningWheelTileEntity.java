package com.ethylol.magical_meringue.tileentity;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.item.recipes.SpinningWheelRecipe;
import com.ethylol.magical_meringue.network.SyncSpinningWheelMessage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nullable;
import java.util.Optional;

public class SpinningWheelTileEntity extends TileEntity implements ITickableTileEntity, IInventory {

    private Optional<SpinningWheelRecipe> currentRecipe = Optional.empty();
    private ItemStack itemstack = ItemStack.EMPTY;
    private int progress = 0;

    public SpinningWheelTileEntity() {
        super(ModTileEntityTypes.spinning_wheel);
    }

    @Override
    public void tick() {

        if (currentRecipe.isPresent()) {
            progress++;
            if (progress >= 160) {
                //bonk the item out
                Vector3i facing = getBlockState().get(HorizontalBlock.HORIZONTAL_FACING).getDirectionVec();
                BlockPos itemPos = pos.add(facing);
                ItemStack out = currentRecipe.get().getRecipeOutput();
                ItemEntity entity = new ItemEntity(world, itemPos.getX(), itemPos.getY(),
                        itemPos.getZ(), out);
                world.addEntity(entity);
                clear();
            }
        }
        else if (progress >= 160 && progress < 200) {
            progress++;
        }
        else {
            progress=0;
        }


    }

    public void updateRecipe() {
        currentRecipe = world.getRecipeManager().getRecipe(SpinningWheelRecipe.TYPE, this, world);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Optional<SpinningWheelRecipe> getCurrentRecipe() {
        return currentRecipe;
    }

    public int getProgress() {
        return progress;
    }

    public void setItemstack(ItemStack itemstack) {
        this.itemstack = itemstack;
        updateRecipe();
        if (!world.isRemote) {
            for (PlayerEntity playerEntity : world.getPlayers())
                MagicalMeringueCore.network.sendTo(new SyncSpinningWheelMessage(pos, itemstack), ((ServerPlayerEntity)playerEntity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }


    public ItemStack getItemstackCopy() {
        return itemstack.copy();
    }

    @Override
    public boolean isEmpty() {
        return itemstack.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return itemstack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        itemstack.shrink(count);
        updateRecipe();
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack ret = itemstack.copy();
        itemstack = ItemStack.EMPTY;
        currentRecipe = Optional.empty();
        return ret;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        itemstack = stack;
        updateRecipe();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clear() {
        itemstack = ItemStack.EMPTY;
        updateRecipe();
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 42;
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState blockState = world.getBlockState(pos);
        read(blockState, pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT tag)
    {
        this.read(blockState, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);
        parentNBTTagCompound.putInt("progress", progress);
        CompoundNBT itemstackCompound = new CompoundNBT();
        itemstack.write(itemstackCompound);
        parentNBTTagCompound.put("itemstack", itemstackCompound);
        return parentNBTTagCompound;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        super.read(blockState, parentNBTTagCompound);

        final int NBT_TYPE_INT = 3;
        final int NBT_TYPE_COMPOUND = 10;

        if (parentNBTTagCompound.contains("progress", NBT_TYPE_INT))
            progress = parentNBTTagCompound.getInt("progress");
        else
            progress = 0;

        if (parentNBTTagCompound.contains("itemstack", NBT_TYPE_COMPOUND))
            itemstack = ItemStack.read(parentNBTTagCompound.getCompound("itemstack"));
        else
            itemstack = ItemStack.EMPTY;
    }
}
