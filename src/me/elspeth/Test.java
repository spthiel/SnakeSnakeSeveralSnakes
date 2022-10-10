package me.elspeth;

import me.elspeth.engine.server.listener.PacketListener;
import me.elspeth.engine.server.entities.Client;
import me.elspeth.engine.server.entities.Server;
import me.elspeth.impl.server.packets.TestPackage;

public class Test {
	
	public static void main(String[] args) {
		
Server server = new Server();
server.registerPacket(TestPackage.class);
server.open();

Client client = new Client();
client.registerPacket(TestPackage.class);
client.open();
		
Client client2 = new Client();
client2.registerPacket(TestPackage.class);
client2.open();

TestPackage toServer = new TestPackage("This package is sent to the server");
TestPackage toClient = new TestPackage("This package is sent to the client");
TestPackage reply = new TestPackage("This is a reply");

server.on(TestPackage.class, (packet) -> {
	System.out.println("Server: " + packet);
	server.send(packet.sender, reply);
});
client.on(TestPackage.class, (packet) -> {
	System.out.println("Client: " + packet);
});
client2.on(TestPackage.class, (packet) -> {
	System.out.println("Client2: " + packet);
});

client.send(toServer);
server.send(toClient);
	}
	
}
