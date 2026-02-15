package net.createmod.catnip;

import net.createmod.catnip.render.SuperByteBufferCache;

public final class CatnipClient {
	public static void invalidateRenderers() {
		SuperByteBufferCache.getInstance().invalidate();
	}
}
