package com.erdaldalkiran.jointypes;

public enum Type {
    SSJ(StreamStreamJoin.class);

    Type(Class streamer) {
        this.streamer = streamer;
    }

    private Class streamer;

    public Class getStreamer() {
        return this.streamer;
    }
}
