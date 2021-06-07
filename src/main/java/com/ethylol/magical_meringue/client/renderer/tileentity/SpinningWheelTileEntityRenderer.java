package com.ethylol.magical_meringue.client.renderer.tileentity;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.block.ModBlocks;
import com.ethylol.magical_meringue.block.SpinningWheel;
import com.ethylol.magical_meringue.item.Wand;
import com.ethylol.magical_meringue.tileentity.SpinningWheelTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SpinningWheelTileEntityRenderer extends TileEntityRenderer<SpinningWheelTileEntity> {


    public SpinningWheelTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SpinningWheelTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        matrixStackIn.push();
        int progress = tileEntityIn.getProgress();
        IVertexBuilder vertexBuffer = bufferIn.getBuffer(RenderType.getSolid());
        BlockState state = tileEntityIn.getBlockState();
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        ModelManager manager = Minecraft.getInstance().getModelManager();

        Direction facing = state.get(HorizontalBlock.HORIZONTAL_FACING);

        float rotation;
        switch (facing) {
            case SOUTH:
                rotation = 1.0f;
                break;
            case WEST:
                rotation = 0.5f;
                break;
            case EAST:
                rotation = -0.5f;
                break;
            default:
                rotation = 0;
                break;
        }

        matrixStackIn.translate(0.5, 0, 0.5);
        matrixStackIn.rotate(Vector3f.YP.rotation((float) (rotation*Math.PI)));
        matrixStackIn.translate(-0.5, 0, -0.5);

        int light = 0;

        if (progress > 0 && progress < 160) {
            tileEntityIn.getCurrentRecipe().ifPresent(spinningWheelRecipe -> {
                Color c = spinningWheelRecipe.getStrandColor();
                float red = c.getRed()/255f;
                float green = c.getGreen()/255f;
                float blue = c.getBlue()/255f;
                float alpha = c.getAlpha()/255f;

                matrixStackIn.push();

                IBakedModel model = manager.getModel(new ResourceLocation(MagicalMeringueCore.MODID, "block/spinning_wheel_strip"));
                World world = tileEntityIn.getWorld();
                BlockPos pos = tileEntityIn.getPos();
                IModelData data = model.getModelData(world, pos, state, ModelDataManager.getModelData(world, pos));
                dispatcher.getBlockModelRenderer().renderModel(matrixStackIn.getLast(), vertexBuffer, state, model, red, green, blue, light, combinedOverlayIn, data);

                matrixStackIn.pop();
            });
        }


        matrixStackIn.push();

        matrixStackIn.translate(0.279994, 1.05777, 0.503746); //wheel center, radius = 0.4145
        matrixStackIn.rotate(Vector3f.ZP.rotation(getAngleFromProgress(progress)));

        IBakedModel model = manager.getModel(new ResourceLocation(MagicalMeringueCore.MODID, "block/spinning_wheel_wheel"));


        World world = tileEntityIn.getWorld();
        BlockPos pos = tileEntityIn.getPos();
        IModelData data = model.getModelData(world, pos, state, ModelDataManager.getModelData(world, pos));
        dispatcher.getBlockModelRenderer().renderModel(matrixStackIn.getLast(), vertexBuffer, state, model, 1,1,1, light, combinedOverlayIn, data);

        matrixStackIn.pop();


        matrixStackIn.push();

        model = manager.getModel(new ResourceLocation(MagicalMeringueCore.MODID, "block/spinning_wheel_base"));
        data = model.getModelData(world, pos, state, ModelDataManager.getModelData(world, pos));
        dispatcher.getBlockModelRenderer().renderModel(matrixStackIn.getLast(), vertexBuffer, state, model, 1,1,1, light, combinedOverlayIn, data);

        matrixStackIn.pop();

        matrixStackIn.pop();

    }

    public static float getAngleFromProgress(int progress) {
        float time = progress/20.0f;
        float ret;
        if (time >= 0 && time <= 2) {
            ret = (float) (2*time*time*time*Math.PI/3);
        }
        else if (time <= 8) {
            ret = (float) ((8*time - 32f/3)*Math.PI);
        }
        else if (time <= 10) {
            ret = (float) ((48 - (2*(10-time)*(10-time)*(10-time) - 32)/3)*Math.PI);
        }
        else {
            ret = (float) ((48 + 32f/3)*Math.PI);
        }
        //correction factor so that the end angle = 2pi
        return 87*ret/88;
    }

    private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, float y, float x, float z) {
        bufferIn.pos(matrixPos, x, y, z).color(red, green, blue, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public boolean isGlobalRenderer(SpinningWheelTileEntity tileEntityMBE21)
    {
        return false;
    }
}
