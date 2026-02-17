package net.createmod.catnip.impl.client;

import net.createmod.catnip.api.client.render.SuperByteBufferCache;

public final class CatnipClient {
	public static void init() {
	    CatnipClientPayloadHandlers.register();
	}

	public static void invalidateRenderers() {
		SuperByteBufferCache.getInstance().invalidate();
	}
}
