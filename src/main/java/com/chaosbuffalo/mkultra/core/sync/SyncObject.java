package com.chaosbuffalo.mkultra.core.sync;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;

public class SyncObject<T> implements ISyncObject {
    String name;
    private T value;
    private boolean dirty;
    private BiConsumer<NBTTagCompound, SyncObject<T>> serializer;
    private BiConsumer<NBTTagCompound, SyncObject<T>> deserializer;

    public SyncObject(String name, T value, BiConsumer<NBTTagCompound, SyncObject<T>> serializer, BiConsumer<NBTTagCompound, SyncObject<T>> deserializer) {
        this.name = name;
        this.serializer = serializer;
        this.deserializer = deserializer;
        set(value);
    }

    public void set(T value) {
        this.value = value;
        this.dirty = true;
    }

    public T get() {
        return value;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void deserializeUpdate(NBTTagCompound tag) {
        if (tag.hasKey(name)) {
            deserializer.accept(tag, this);
        }
    }

    @Override
    public void serializeUpdate(NBTTagCompound tag) {
        if (dirty) {
            serializer.accept(tag, this);
            dirty = false;
        }
    }
}
