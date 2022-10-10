package me.elspeth.impl.server.packets;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import me.elspeth.engine.server.Packet;
import me.elspeth.engine.server.PacketMeta;

@PacketMeta(id = 1)
public class TestPackage extends Packet {
	
	private String text;
	
	public TestPackage() {}
	
	public TestPackage(String toSend) {
		this.text = toSend;
	}
	
	@Override
	public void decode(byte[] data) {
		this.text = new String(data, StandardCharsets.UTF_16);
	}
	
	@Override
	public byte[] encode() {
		
		return text.getBytes(StandardCharsets.UTF_16);
	}
	
	@Override
	public String toString() {
		
		return "TestPackage[" + this.text + "]";
	}
}
