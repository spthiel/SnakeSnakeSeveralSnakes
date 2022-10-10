package me.elspeth.engine.server;

import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketHeader {
	
	@PacketHeaderMeta(bytes = 0)
	private static final int headerByteCount = PacketHeader.getAllBytes();
	
	@PacketHeaderMeta(bytes = 4)
	public int size;
	
	@PacketHeaderMeta(bytes = 2)
	public short id;
	
	public static int getHeaderByteCount() {
		return headerByteCount;
	}
	
	public PacketHeader() {
	
	}
	
	private static int getAllBytes() {
		return Arrays.stream(PacketHeader.class.getDeclaredFields()).mapToInt(PacketHeader::getBytes).sum();
	}
	
	private static int getBytes(Field field) {
		PacketHeaderMeta meta = field.getAnnotation(PacketHeaderMeta.class);
		return meta.bytes();
	}
	
	public void read(byte[] bytes) throws SocketClosedException {
		if (bytes.length < getHeaderByteCount()) {
			throw new SocketClosedException();
		}
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		int index = 0;
		for (Field f : this.getClass().getDeclaredFields()) {
			int nextSize = getBytes(f);
			if (nextSize == 0) {
				continue;
			}
			
			try {
				Object value = mapBytesToType(f.getType().getName(), buffer.slice(index, nextSize));
				f.set(this, value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			index += nextSize;
		}
	}
	
	public byte[] write() {
		ByteBuffer out = ByteBuffer.allocate(getHeaderByteCount());
		int index = 0;
		for (Field f : this.getClass().getDeclaredFields()) {
			int nextSize = getBytes(f);
			if (nextSize == 0) {
				continue;
			}
			
			try {
				ByteBuffer bytes = mapTypeToBytes(f.getType().getName(), nextSize, f.get(this));
				out.put(bytes.array());
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			index += nextSize;
		}
		return out.array();
	}
	
	private Object mapBytesToType(String typeName, ByteBuffer bytes) {
		return switch (typeName) {
			case "int" -> bytes.getInt();
			case "short" -> bytes.getShort();
			case "java.lang.String" -> new String(bytes.array(), StandardCharsets.UTF_8);
			default -> throw new RuntimeException("Fieldtype not set: " + typeName);
		};
	}
	
	private ByteBuffer mapTypeToBytes(String typeName, int size, Object value) {
		ByteBuffer out = ByteBuffer.allocate(size);
		return switch (typeName) {
			case "int" -> out.putInt((int)value);
			case "short" -> out.putShort((short)value);
			case "java.lang.String" -> out.put(((String)value).getBytes(StandardCharsets.UTF_8));
			default -> throw new RuntimeException("Fieldtype not set: " + typeName);
		};
	}
	
	@Override
	public String toString() {
		
		return "PacketHeader{" +
			"size=" + size +
			", id=" + id +
			'}';
	}
}
