package net.createmod.catnip.platform;

import java.util.Iterator;
import java.util.Locale;

import com.mojang.blaze3d.platform.Window;

import net.createmod.catnip.render.RenderTargetExtensions;
import net.createmod.ponder.mixin.client.ParticleEngineAccessor;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.language.LanguageManager;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.catnip.client.render.model.ShadeSeparatedBufferSource;
import net.createmod.catnip.client.render.model.ShadeSeparatedResultConsumer;
import net.createmod.catnip.impl.client.render.model.BakedModelBuffererImpl;
import net.createmod.catnip.platform.services.ModClientHooksHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class FabricClientHooksHelper implements ModClientHooksHelper {
	@Override
	public Locale getCurrentLocale() {
		LanguageManager languageManager = Minecraft.getInstance().getLanguageManager();
		String[] split = languageManager.getSelected().split("_", 2);
		return split.length == 1 ? new Locale(split[0]) : new Locale(split[0], split[1]);
	}

	@Override
	@Nullable
	public <T extends ParticleOptions> Particle createParticleFromData(T data, ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
		return ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine).catnip$makeParticle(data, x, y, z, mx, my, mz);
	}

	@Override
	public Minecraft getMinecraftFromScreen(Screen screen) {
		return Screens.getClient(screen);
	}

	@Override
	public boolean isKeyPressed(KeyMapping mapping) {
		int keyCode = KeyBindingHelper.getBoundKeyOf(mapping).getValue();
		Window window = Minecraft.getInstance().getWindow();
		return InputConstants.isKeyDown(window, keyCode);
	}

	@Override
	public void enableStencilBuffer(RenderTarget renderTarget) {
		((RenderTargetExtensions) renderTarget).catnip$enableStencil();
	}

	@Override
	public void renderFullFluidState(PoseStack ms, MultiBufferSource.BufferSource buffer, FluidState fluid) {
		CatnipServices.FLUID_RENDERER.renderFluidBox(fluid, 0, 0, 0, 1, 1, 1, buffer, ms, LightTexture.FULL_BRIGHT, false, true);
	}

	@Override
	public void bufferModel(BlockStateModel model, BlockPos pos, BlockAndTintGetter level, BlockState state, @Nullable PoseStack poseStack, ShadeSeparatedBufferSource bufferSource) {
		BakedModelBuffererImpl.bufferModel(model, pos, level, state, poseStack, bufferSource);
	}

	@Override
	public void bufferModel(BlockStateModel model, BlockPos pos, BlockAndTintGetter level, BlockState state, @Nullable PoseStack poseStack, ShadeSeparatedResultConsumer resultConsumer) {
		BakedModelBuffererImpl.bufferModel(model, pos, level, state, poseStack, resultConsumer);
	}

	@Override
	public void bufferBlocks(Iterator<BlockPos> posIterator, BlockAndTintGetter level, @Nullable PoseStack poseStack, boolean renderFluids, ShadeSeparatedBufferSource bufferSource) {
		BakedModelBuffererImpl.bufferBlocks(posIterator, level, poseStack, renderFluids, bufferSource);
	}

	@Override
	public void bufferBlocks(Iterator<BlockPos> posIterator, BlockAndTintGetter level, @Nullable PoseStack poseStack, boolean renderFluids, ShadeSeparatedResultConsumer resultConsumer) {
		BakedModelBuffererImpl.bufferBlocks(posIterator, level, poseStack, renderFluids, resultConsumer);
	}
}
