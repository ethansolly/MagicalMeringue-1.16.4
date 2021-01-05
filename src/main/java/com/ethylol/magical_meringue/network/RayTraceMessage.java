package com.ethylol.magical_meringue.network;

import io.netty.buffer.ByteBuf;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class RayTraceMessage {

    private RayTraceResult.Type hitType;
    private BlockPos pos;
    private Direction facing;
    private MessageType messageType;

    public RayTraceMessage() {}

    public RayTraceMessage(RayTraceResult result, MessageType messageType) {
        hitType = result.getType();
        if (hitType == RayTraceResult.Type.BLOCK) {
            pos = ((BlockRayTraceResult)result).getPos();
            facing = ((BlockRayTraceResult)result).getFace();
        }
        else {
            pos = new BlockPos(result.getHitVec());
            facing = null;
        }
        this.messageType = messageType;
    }

    public RayTraceMessage(RayTraceResult.Type hitType, BlockPos pos, Direction facing, MessageType messageType) {
        this.hitType = hitType;
        this.pos = pos;
        this.facing = facing;
        this.messageType = messageType;
    }


    public BlockPos getPos() {
        return pos;
    }

    public Direction getFacing() {return facing;}

    public RayTraceResult.Type getHitType() {
        return hitType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public enum MessageType {
        ACID_SPLASH,
        BREAK_BLOCK,
        DANCING_LIGHTS,
        EXCHANGE,
        LEVITATE
    }

}
