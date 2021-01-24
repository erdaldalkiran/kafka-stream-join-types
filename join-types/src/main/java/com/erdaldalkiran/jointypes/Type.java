package com.erdaldalkiran.jointypes;

public enum Type {
    SSJ(StreamStreamJoin.class),
    SSLJ(StreamStreamLeftJoin.class),
    STJ(StreamTableJoin.class),
    STLJ(StreamTableLeftJoin.class),
    TTJ(TableTableJoin.class),
    TTLJ(TableTableLeftJoin.class),
    Pro(ProblemSolution.class);

    Type(Class streamer) {
        this.streamer = streamer;
    }

    private Class streamer;

    public Class getStreamer() {
        return this.streamer;
    }
}
