package net.createmod.ponder.foundation.content;

import net.createmod.ponder.Ponder;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.Identifier;

public class DebugPonderPlugin implements PonderPlugin {
	@Override
	public String getModId() {
		return Ponder.MOD_ID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
		DebugScenes.registerAll(helper);
	}
}
