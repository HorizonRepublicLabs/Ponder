package net.createmod.catnip.animation;

import net.createmod.catnip.mixin.client.TimerAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

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
		Minecraft mc = Minecraft.getInstance();
		return mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
	}

	/**
	 * @return the fraction between the current tick to the next tick, not frozen during game pause [0-1]
	 */
	// TODO - Check if one of the getGameTimeDeltaPartialTick methods can be used here instead
	public static float getPartialTicksUI() {
		Minecraft mc = Minecraft.getInstance();
		DeltaTracker timer = mc.getDeltaTracker();

		if (timer instanceof TimerAccessor timerAccessor) {
			return timerAccessor.catnip$getDeltaTickResidual();
		} else {
			return getPartialTicks();
		}
	}
}
