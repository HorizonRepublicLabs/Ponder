package net.createmod.catnip.placement;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;

import com.mojang.math.Constants;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.gui.render.FadedArrowRenderState;
import net.createmod.catnip.gui.render.TexturedArrowRenderState;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.enums.PonderGuiTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class PlacementClient {
	static final LerpedFloat angle = LerpedFloat.angular()
		.chase(0, 0.25f, LerpedFloat.Chaser.EXP);
	@Nullable
	static BlockPos target = null;
	@Nullable
	static BlockPos lastTarget = null;
	static int animationTick = 0;

	public static void tick() {
		setTarget(null);
		checkHelpers();

		if (target == null) {
			if (animationTick > 0)
				animationTick = Math.max(animationTick - 2, 0);

			return;
		}

		if (animationTick < 10)
			animationTick++;

	}

	private static void checkHelpers() {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel world = mc.level;

		if (world == null)
			return;

		if (!(mc.hitResult instanceof BlockHitResult ray))
			return;

		if (mc.player == null)
			return;

		if (mc.player.isShiftKeyDown())// for now, disable all helpers when sneaking TODO add helpers that respect
			// sneaking but still show position
			return;

		for (InteractionHand hand : InteractionHand.values()) {

			ItemStack heldItem = mc.player.getItemInHand(hand);

			List<IPlacementHelper> filteredForHeldItem = new ArrayList<>();
			for (IPlacementHelper helper : PlacementHelpers.getHelpersView()) {
				if (helper.matchesItem(heldItem))
					filteredForHeldItem.add(helper);
			}

			if (filteredForHeldItem.isEmpty())
				continue;

			BlockPos pos = ray.getBlockPos();
			BlockState state = world.getBlockState(pos);

			List<IPlacementHelper> filteredForState = new ArrayList<>();
			for (IPlacementHelper helper : filteredForHeldItem) {
				if (helper.matchesState(state))
					filteredForState.add(helper);
			}

			if (filteredForState.isEmpty())
				continue;

			boolean atLeastOneMatch = false;
			for (IPlacementHelper h : filteredForState) {
				PlacementOffset offset = h.getOffset(mc.player, world, state, pos, ray, heldItem);

				if (offset.isSuccessful()) {
					h.renderAt(pos, state, ray, offset);
					setTarget(offset.getBlockPos());
					atLeastOneMatch = true;
					break;
				}

			}

			// at least one helper activated, no need to check the offhand if we are still
			// in the mainhand
			if (atLeastOneMatch)
				return;

		}
	}

	static void setTarget(@Nullable BlockPos target) {
		PlacementClient.target = target;

		if (target == null)
			return;

		if (lastTarget == null) {
			lastTarget = target;
			return;
		}

		if (!lastTarget.equals(target))
			lastTarget = target;
	}

	public static void onRenderCrosshairOverlay(GuiGraphics graphics, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if (player != null && animationTick > 0) {
			float screenY = graphics.guiHeight() / 2f;
			float screenX = graphics.guiWidth() / 2f;
			float progress = getCurrentAlpha();

			drawDirectionIndicator(graphics, partialTicks, screenX, screenY, progress);
		}
	}

	public static float getCurrentAlpha() {
		return Math.min(animationTick / 10f/* + event.getPartialTicks() */, 1f);
	}

	private static void drawDirectionIndicator(GuiGraphics graphics, float partialTicks, float centerX, float centerY,
											   float progress) {
		float r = .8f;
		float g = .8f;
		float b = .8f;
		float a = progress * progress;

		Vec3 projTarget = VecHelper.projectToPlayerView(VecHelper.getCenterOf(lastTarget), partialTicks);

		Vec3 target = new Vec3(projTarget.x, projTarget.y, 0);
		if (projTarget.z > 0)
			target = target.reverse();

		Vec3 norm = target.normalize();
		Vec3 ref = new Vec3(0, 1, 0);
		float targetAngle = AngleHelper.deg(-Math.acos(norm.dot(ref)));

		if (norm.x < 0)
			targetAngle = 360 - targetAngle;

		if (animationTick < 10)
			angle.setValue(targetAngle);

		angle.chase(targetAngle, .25f, LerpedFloat.Chaser.EXP);
		angle.tickChaser();

		float snapSize = 22.5f;
		float snappedAngle = (snapSize * Math.round(angle.getValue(0f) / snapSize)) % 360f;

		float length = 10;

		// FIXME: config
		// CClient.PlacementIndicatorSetting mode = PonderConfig.client().placementIndicator.get();
		// if (mode == CClient.PlacementIndicatorSetting.TRIANGLE) {
			// fadedArrow(graphics, centerX, centerY, r, g, b, a, length);
		// } else if (mode == CClient.PlacementIndicatorSetting.TEXTURE) {
			textured(graphics, centerX, centerY, a, snappedAngle);
		// }
	}

	private static void fadedArrow(GuiGraphics graphics, float centerX, float centerY, float r, float g, float b, float a, float length) {
		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();
		poseStack.translate(centerX, centerY);
		poseStack.rotate(angle.getValue(0) * Constants.DEG_TO_RAD);
		// FIXME: config
		double scale = 1;//PonderConfig.client().indicatorScale.get();
		poseStack.scale((float) scale, (float) scale);

		int size = (int) ((10 + length) * scale);
		graphics.guiRenderState.submitGuiElement(new FadedArrowRenderState(
			new Matrix3x2f(graphics.pose()), size, length, r, g, b, a
		));

		poseStack.popMatrix();
	}

	public static void textured(GuiGraphics graphics, float centerX, float centerY, float alpha, float snappedAngle) {
		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();
		poseStack.translate(centerX, centerY);
		// FIXME: config
		float scale = /*PonderConfig.client().indicatorScale.get().floatValue()*/ 1 * .75f;
		poseStack.scale(scale, scale);
		poseStack.scale(12, 12);

		float index = snappedAngle / 22.5f;
		float texSize = 16f / 256f;

		float tx = 0;
		float ty = index * texSize;
		float tw = 1;
		float th = texSize;

		int size = (int) (36 * scale);
		graphics.guiRenderState.submitGuiElement(new TexturedArrowRenderState(
			new Matrix3x2f(graphics.pose()),
			PonderGuiTextures.PLACEMENT_INDICATOR_SHEET.bind(),
			size,
			alpha,
			tx,
			ty,
			tw,
			th
		));

		poseStack.popMatrix();
	}
}
