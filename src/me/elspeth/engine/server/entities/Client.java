package me.elspeth.engine.server.entities;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.elspeth.Config;
import me.elspeth.engine.server.Packet;

public class Client extends PacketReceiver {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private       Socket socket = null;
	private final String host;
	private final Thread reader;
	
	public Client() {
		this("localhost");
	}
	
	public Client(String host) {
		super();
		this.host = host;
		reader = new Thread(() -> {
			try(var stream = socket.getInputStream()) {
				while (!socket.isClosed()) {
					Packet packet = this.packetHandler.read(stream);
					packet.sender = socket;
					this.receive(packet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public Socket getSocket() {
		
		return socket;
	}
	
	@Override
	public void send(Packet packet) {
		if(!send(socket, packet)) {
			logger.log(Level.WARNING, "Lost connection to server. Restarting socket");
			close();
			open();
		}
	}
	
	@Override
	public void open() {
		
		try {
			socket = new Socket(host, Config.PORT);
			reader.start();
			logger.log(Level.INFO, "Starting client connecting to " + host + ":" + Config.PORT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void close() {
		
		try {
			socket.close();
			reader.interrupt();
			logger.log(Level.INFO, "Closing client connection");
		} catch (IOException ignored) {
		} finally {
			socket = null;
		}
	}
}
