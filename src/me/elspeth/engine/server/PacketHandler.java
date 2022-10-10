package me.elspeth.engine.server;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

public class PacketHandler {
	
	private final HashMap<Short, Class<? extends Packet>> packetTypes;
	
	public PacketHandler() {
		packetTypes = new HashMap<>();
	}
	
	public void addPacketType(Class<? extends Packet> packetClass) {
		packetTypes.put(getId(packetClass), packetClass);
	}
	
	public byte[] encode(Packet packet) {
		PacketHeader header = new PacketHeader();
		header.id = getId(packet.getClass());
		var bytes = packet.encode();
		header.size = bytes.length;
		var out = new byte[bytes.length + PacketHeader.getHeaderByteCount()];
		var headerBytes = header.write();
		System.arraycopy(headerBytes, 0, out, 0, headerBytes.length);
		System.arraycopy(bytes, 0, out, PacketHeader.getHeaderByteCount(), bytes.length);
		return out;
	}
	
	public Packet read(InputStream stream) throws SocketClosedException, IOException {
		PacketHeader header = this.readHeader(stream);
		var packetClass = packetTypes.get(header.id);
		if (packetClass == null) {
			return null;
		}
		try {
			Packet packet = (Packet)packetClass.getConstructor().newInstance();
			byte[] packetBody = stream.readNBytes(header.size);
			packet.decode(packetBody);
			return packet;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			new RuntimeException("Exception occured while decoding packet of type " + header.id, e).printStackTrace();
			return null;
		}
	}
	
	public PacketHeader readHeader(InputStream stream) throws IOException, SocketClosedException {
		byte[] headerBytes = stream.readNBytes(PacketHeader.getHeaderByteCount());
		PacketHeader header = new PacketHeader();
		header.read(headerBytes);
		return header;
	}
	
	private short getId(Class<? extends Packet> packetClass) {
		PacketMeta annotation = packetClass.getAnnotation(PacketMeta.class);
		if (annotation == null) {
			throw new RuntimeException("Packet class " + packetClass.getName() + " does not have the packet meta annotation.");
		}
		return annotation.id();
	}
	
}
