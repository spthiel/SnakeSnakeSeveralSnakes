package me.elspeth.engine.server.entities;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.elspeth.engine.server.Packet;
import me.elspeth.engine.server.PacketHandler;
import me.elspeth.engine.server.listener.PacketListener;

public abstract class PacketReceiver {
	
	private static Logger logger = Logger.getLogger(PacketReceiver.class.getName());
	
	private final   LinkedList<PacketListener<?>> listeners;
	protected final PacketHandler                 packetHandler;
	
	public PacketReceiver() {
		
		packetHandler = new PacketHandler();
		listeners = new LinkedList<>();
		
	}
	
	public void registerPacket(Class<? extends Packet> packetClass) {
		
		this.packetHandler.addPacketType(packetClass);
	}
	
	public<P extends Packet> PacketListener<P> on(Class<P> packetClass, Consumer<P> callback) {
		var listener = new PacketListener<P>(packetClass, callback);
		this.listeners.push(listener);
		return listener;
	}
	
	public void remove(PacketListener<?> listener) {
		
		this.listeners.remove(listener);
	}
	
	abstract public void send(Packet packet);
	
	protected void receive(Packet packet) {
		
		listeners.stream()
				 .filter(listener -> listener.matches(packet))
				 .forEach(listener -> listener.execute(packet));
	}
	
	protected boolean send(Socket socket, byte[] data) {
		
		try {
			socket.getOutputStream().write(data);
			return true;
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException ignored) {
			}
			return false;
		}
	}
	
	public boolean send(Socket socket, Packet packet) {
		byte[] data = this.packetHandler.encode(packet);
		return send(socket, data);
	}
	
	abstract public void open();
	abstract public void close();
	
}
