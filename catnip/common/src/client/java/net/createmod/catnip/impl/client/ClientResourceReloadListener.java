package net.createmod.catnip.impl.client;

import net.createmod.catnip.api.client.lang.LangNumberFormat;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ClientResourceReloadListener implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		LangNumberFormat.numberFormat.update();
		CatnipClient.invalidateRenderers();
	}
}
