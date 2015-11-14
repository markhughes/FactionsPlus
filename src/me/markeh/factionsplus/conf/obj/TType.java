package me.markeh.factionsplus.conf.obj;

public abstract class TType<T> {
	public abstract String asRawString();
	public abstract T valueOf(String raw);
}
