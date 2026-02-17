package net.createmod.catnip.api.client.platform;

import net.createmod.catnip.api.platform.CatnipServices;

public class CatnipClientServices extends CatnipServices {
	public static final ModClientHooksHelper CLIENT_HOOKS = load(ModClientHooksHelper.class);
}
