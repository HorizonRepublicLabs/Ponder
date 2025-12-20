package net.createmod.ponder.utility;

import net.createmod.catnip.event.ClientResourceReloadListener;
import net.createmod.ponder.Ponder;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.Identifier;

public class FabricClientResourceReloadListener extends ClientResourceReloadListener implements IdentifiableResourceReloadListener {
	@Override
	public Identifier getFabricId() {
		return Ponder.id("client_resource_reloader");
	}
}
