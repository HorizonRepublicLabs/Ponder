package net.createmod.catnip.platform.services;

import java.util.Iterator;
import java.util.Locale;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.client.render.model.ShadeSeparatedBufferSource;
import net.createmod.catnip.client.render.model.ShadeSeparatedResultConsumer;
import net.createmod.catnip.render.ShadedBlockSbbBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface ModClientHooksHelper {
	Locale getCurrentLocale();

	@Nullable
	<T extends ParticleOptions> Particle createParticleFromData(T data, ClientLevel level, double x, double y, double z,
																double mx, double my, double mz);

	Minecraft getMinecraftFromScreen(Screen screen);

	// note: implementations don't use isDown since we need this to work inside screens
	boolean isKeyPressed(KeyMapping mapping);

	void enableStencilBuffer(RenderTarget renderTarget);

	void renderFullFluidState(PoseStack ms, MultiBufferSource.BufferSource buffer, FluidState fluid);

	@ApiStatus.Internal
	void bufferModel(BlockStateModel model, BlockPos pos, BlockAndTintGetter level, BlockState state, @Nullable PoseStack poseStack, ShadeSeparatedBufferSource bufferSource);

	@ApiStatus.Internal
	void bufferModel(BlockStateModel model, BlockPos pos, BlockAndTintGetter level, BlockState state, @Nullable PoseStack poseStack, ShadeSeparatedResultConsumer resultConsumer);

	@ApiStatus.Internal
	void bufferBlocks(Iterator<BlockPos> posIterator, BlockAndTintGetter level, @Nullable PoseStack poseStack, boolean renderFluids, ShadeSeparatedBufferSource bufferSource);

	@ApiStatus.Internal
	void bufferBlocks(Iterator<BlockPos> posIterator, BlockAndTintGetter level, @Nullable PoseStack poseStack, boolean renderFluids, ShadeSeparatedResultConsumer resultConsumer);

	@Deprecated(forRemoval = true)
	default ShadedBlockSbbBuilder createSbbBuilder(BufferBuilder builder) {
		return ShadedBlockSbbBuilder.create();
	}
}
