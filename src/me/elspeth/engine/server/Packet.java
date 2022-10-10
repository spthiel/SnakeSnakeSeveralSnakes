package me.elspeth.engine.server;

import java.net.Socket;

public abstract class Packet {
	
	public Socket sender;
	
	abstract public void decode(byte[] data);
	abstract public byte[] encode();
	
	// TODO: Make packet header
	
}
