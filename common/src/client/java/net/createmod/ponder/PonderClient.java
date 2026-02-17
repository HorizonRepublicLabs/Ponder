package net.createmod.ponder;

import net.createmod.catnip.api.client.platform.ModClientHooksHelper;
import net.createmod.catnip.api.client.render.SuperByteBufferCache;
import net.createmod.catnip.api.platform.services.PlatformHelper;
import net.createmod.catnip.impl.network.ClientboundSimpleActionPacket;
import net.createmod.ponder.command.SimplePonderActions;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.content.BasePonderPlugin;
import net.createmod.ponder.foundation.content.DebugPonderPlugin;
import net.createmod.ponder.foundation.element.WorldSectionElementImpl;
import net.createmod.ponder.foundation.render.PonderSceneRenderState;
import net.createmod.ponder.foundation.render.PonderSceneRenderer;

public class PonderClient {
	public static void init() {
		SuperByteBufferCache.getInstance().registerCompartment(WorldSectionElementImpl.PONDER_WORLD_SECTION);

		ClientboundSimpleActionPacket.addAction("openPonder", () -> SimplePonderActions::openPonder);
		ClientboundSimpleActionPacket.addAction("reloadPonder", () -> SimplePonderActions::reloadPonder);

		ModClientHooksHelper.INSTANCE.registerPictureInPictureRenderer(PonderSceneRenderState.class, PonderSceneRenderer::new);

		PonderIndex.addPlugin(new BasePonderPlugin());

		if (PlatformHelper.INSTANCE.isDevelopmentEnvironment()) {
			PonderIndex.addPlugin(new DebugPonderPlugin());
		}
	}

	public static void modLoadCompleted() {
		PonderIndex.registerAll();
	}
}
