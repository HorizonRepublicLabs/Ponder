package net.createmod.ponder.foundation.render;

import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.ponder.foundation.PonderScene;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;

public record PonderSceneRenderState(
	Matrix3x2f pose, PonderScene scene, int width, int height, double slide, LerpedFloat finishingFlash,
	float partialTicks
) implements PictureInPictureRenderState {
	@Override
	public int x0() {
		return 0;
	}

	@Override
	public int x1() {
		return 0;
	}

	@Override
	public int y0() {
		return 0;
	}

	@Override
	public int y1() {
		return 0;
	}

	@Override
	public float scale() {
		return 0;
	}

	@Override
	public @Nullable ScreenRectangle scissorArea() {
		return null;
	}

	@Override
	public @Nullable ScreenRectangle bounds() {
		return null;
	}
}
