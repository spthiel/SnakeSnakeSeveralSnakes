package me.elspeth.engine.server.listener;

import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

import me.elspeth.engine.server.Packet;

public class PacketListener<P extends Packet> {
	
	private final Consumer<P> callback;
	private final Class<P>    packetClass;
	
	public PacketListener(Class<P> packetClass, Consumer<P> callback) {
		this.callback = callback;
		this.packetClass = packetClass;
	}
	
	public boolean matches(Packet packet) {
		return this.packetClass.isInstance(packet);
	}
	
	public void execute(Packet packet) {
		//noinspection unchecked
		this.callback.accept((P)packet);
	}
	
}
