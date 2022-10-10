package me.elspeth.engine.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHeaderMeta {
	
	int bytes();
	
}
