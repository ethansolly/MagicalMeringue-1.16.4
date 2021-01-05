package com.ethylol.magical_meringue.capabilities.join;

import io.netty.buffer.ByteBuf;

public class JoinMessage {

    boolean joined;

    public JoinMessage() { joined = false; }

    public JoinMessage(IJoinHandler handler) {
        this.joined = handler.hasJoined();
    }

    public JoinMessage(boolean hasJoined) {
        this.joined = hasJoined;
    }

    public boolean isJoined() {
        return joined;
    }
}
