package com.erdaldalkiran.producer;

public enum Type {
    Ad(AdViewAndClickProducer.class);

    Type(Class producer) {
        this.producer = producer;
    }

    private Class producer;

    public Class getProducer() {
        return this.producer;
    }

}
