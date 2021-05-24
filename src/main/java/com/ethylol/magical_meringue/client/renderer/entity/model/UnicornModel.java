package com.ethylol.magical_meringue.client.renderer.entity.model;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.ethylol.magical_meringue.entity.UnicornEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class UnicornModel extends EntityModel<UnicornEntity> {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer mr0;
	private final ModelRenderer mr1;
	private final ModelRenderer mr2;
	private final ModelRenderer horn;
	private final ModelRenderer legs;
	private final ModelRenderer front;
	private final ModelRenderer left_r1;
	private final ModelRenderer right_r1;
	private final ModelRenderer back;
	private final ModelRenderer left_r2;
	private final ModelRenderer right_r2;

	public UnicornModel() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 32).addBox(-5.0F, -20.0F, -10.0F, 10.0F, 10.0F, 22.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 24.0F, -17.0F);
		setRotationAngle(head, -0.5236F, 0.0F, -3.1416F);
		head.setTextureOffset(0, 35).addBox(-1.95F, 6.0885F, 11.0622F, 4.0F, 12.0F, 7.0F, 0.0F, false);

		mr0 = new ModelRenderer(this);
		mr0.setRotationPoint(0.0F, 0.0F, -2.0F);
		head.addChild(mr0);
		mr0.setTextureOffset(0, 13).addBox(-3.0F, 18.0885F, 13.0622F, 6.0F, 5.0F, 7.0F, 0.0F, false);

		mr1 = new ModelRenderer(this);
		mr1.setRotationPoint(0.0F, 0.0F, -2.0F);
		head.addChild(mr1);
		mr1.setTextureOffset(56, 36).addBox(-1.0F, 7.0885F, 20.0722F, 2.0F, 16.0F, 2.0F, 0.0F, false);

		mr2 = new ModelRenderer(this);
		mr2.setRotationPoint(0.0F, 0.0F, -2.0F);
		head.addChild(mr2);
		mr2.setTextureOffset(0, 25).addBox(-2.0F, 18.0885F, 8.0622F, 4.0F, 5.0F, 5.0F, 0.0F, false);

		horn = new ModelRenderer(this);
		horn.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(horn);
		horn.setTextureOffset(56, 0).addBox(-1.0F, 23.0885F, 16.5981F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		horn.setTextureOffset(56, 0).addBox(-1.0F, 24.7487F, 16.5981F, 2.0F, 2.0F, 2.0F, -0.1F, false);
		horn.setTextureOffset(56, 0).addBox(-1.0F, 25.7487F, 16.5981F, 2.0F, 3.0F, 2.0F, -0.2F, false);
		horn.setTextureOffset(56, 0).addBox(-1.0F, 27.7487F, 16.5981F, 2.0F, 3.0F, 2.0F, -0.3F, false);

		legs = new ModelRenderer(this);
		legs.setRotationPoint(0.0F, 24.0F, 2.0F);
		

		front = new ModelRenderer(this);
		front.setRotationPoint(0.0F, 0.0F, 0.0F);
		legs.addChild(front);
		

		left_r1 = new ModelRenderer(this);
		left_r1.setRotationPoint(-3.0F, -10.0F, 6.0F);
		front.addChild(left_r1);
		setRotationAngle(left_r1, 0.0F, 3.1416F, 0.0F);
		left_r1.setTextureOffset(48, 21).addBox(-1.95F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);

		right_r1 = new ModelRenderer(this);
		right_r1.setRotationPoint(3.0F, -10.0F, 6.0F);
		front.addChild(right_r1);
		setRotationAngle(right_r1, 0.0F, 3.1416F, 0.0F);
		right_r1.setTextureOffset(48, 21).addBox(-2.05F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);

		back = new ModelRenderer(this);
		back.setRotationPoint(0.0F, 0.0F, -2.0F);
		legs.addChild(back);
		

		left_r2 = new ModelRenderer(this);
		left_r2.setRotationPoint(-3.0F, -10.0F, -7.0F);
		back.addChild(left_r2);
		setRotationAngle(left_r2, 0.0F, 3.1416F, 0.0F);
		left_r2.setTextureOffset(48, 21).addBox(-1.95F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);

		right_r2 = new ModelRenderer(this);
		right_r2.setRotationPoint(3.0F, -10.0F, -7.0F);
		back.addChild(right_r2);
		setRotationAngle(right_r2, 0.0F, 3.1416F, 0.0F);
		right_r2.setTextureOffset(48, 21).addBox(-2.05F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(UnicornEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		head.render(matrixStack, buffer, packedLight, packedOverlay);
		legs.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setLivingAnimations(UnicornEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);

		this.right_r2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.left_r2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.right_r1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.left_r1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

	}
}