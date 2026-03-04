package net.createmod.catnip.api.client.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;

public class AnimationTickHolder {
	private static int ticks;
	private static int pausedTicks;

	public static void reset() {
		ticks = 0;
		pausedTicks = 0;
	}

	public static void tick() {
		if (!Minecraft.getInstance()
			.isPaused()) {
			ticks = (ticks + 1) % 1_728_000; // wrap around every 24 hours so we maintain enough floating point precision
		} else {
			pausedTicks = (pausedTicks + 1) % 1_728_000;
		}
	}

	public static int getTicks() {
		return getTicks(false);
	}

	public static int getTicks(boolean includePaused) {
		return includePaused ? ticks + pausedTicks : ticks;
	}

	public static float getRenderTime() {
		return getTicks() + getPartialTicks();
	}

	/**
	 * @return the fraction between the current tick to the next tick, frozen during game pause [0-1]
	 */
	public static float getPartialTicks() {
		return Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
	}

	/// In `Screen.render`, the partialTicks value is actually incorrect.
	///
	/// In other cases, like entity rendering, partialTicks is an accumulated fraction of ticks that have
	/// passed since the last game tick. It should range from 0-1, but may be larger during lag spikes.
	///
	/// `Screen.render` is instead given a simple frame delta, which is not very useful for smooth animations.
	/// The value will pretty much always be the same.
	///
	/// This method provides access to the accumulated delta. This is actually what vanilla
	/// does in [EnchantmentScreen], which needs a smooth animation for the book opening.
	public static float getGuiPartialTicks() {
		return getPartialTicks();
	}
}
