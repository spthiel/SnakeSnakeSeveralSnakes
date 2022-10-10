package me.elspeth.engine.server.entities;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.elspeth.Config;
import me.elspeth.engine.server.Packet;
import me.elspeth.engine.server.SocketClosedException;

public class Server extends PacketReceiver {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private       ServerSocket                  server;
	private final LinkedList<Socket>            sockets = new LinkedList<>();
	private final Thread                        acceptClients;
	
	public Server() {
		super();
		acceptClients = new Thread(() -> {
			try {
				socketReceiver();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	private void socketReceiver() throws IOException {
		while (!server.isClosed()) {
			var socket = server.accept();
			sockets.add(socket);
			logger.log(Level.INFO, "Received socket. Open sockets: " + sockets.size());
			Thread socketReader = new Thread(() -> {
				try(InputStream inputSteam = socket.getInputStream()) {
					readStream(socket, inputSteam);
				} catch (IOException | SocketClosedException e) {
					if (!socket.isClosed()) {
						try {
							socket.close();
							sockets.remove(socket);
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			});
			socketReader.start();
		}
	}
	
	private void readStream(Socket socket, InputStream stream) throws SocketClosedException, IOException {
		while (!socket.isClosed()) {
			Packet packet = packetHandler.read(stream);
			packet.sender = socket;
			this.receive(packet);
		}
	}
	
	@Override
	public boolean send(Socket socket, Packet packet) {
		if(!super.send(socket, packet)) {
			sockets.remove(socket);
			return false;
		}
		return true;
	}
	
	@Override
	public void send(Packet packet) {
		byte[] data = this.packetHandler.encode(packet);
		sockets.removeIf(socket -> !send(socket, data));
	}
	
	public void open() {
		try {
			server = new ServerSocket(Config.PORT);
			logger.log(Level.INFO, "Opened server on port: " + Config.PORT);
			acceptClients.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		try {
			acceptClients.interrupt();
			for (Socket socket : sockets) {
				socket.close();
			}
			server.close();
			server = null;
			logger.log(Level.INFO, "Closing server.");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
