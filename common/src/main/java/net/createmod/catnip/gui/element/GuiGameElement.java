package net.createmod.catnip.gui.element;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import dev.engine_room.flywheel.lib.model.baked.SinglePosVirtualBlockGetter;
import net.createmod.catnip.client.render.model.BakedModelBufferer;
import net.createmod.catnip.gui.ILightingSettings;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.impl.client.render.ColoringVertexConsumer;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.CatnipClientServices;
import net.createmod.ponder.mixin.client.accessor.ItemRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

public class GuiGameElement {
	public static GuiRenderBuilder of(ItemStack stack) {
		return new GuiItemRenderBuilder(stack);
	}

	public static GuiRenderBuilder of(ItemLike itemProvider) {
		return new GuiItemRenderBuilder(itemProvider);
	}

	public static GuiRenderBuilder of(BlockState state) {
		return new GuiBlockStateRenderBuilder(state);
	}

	public static GuiRenderBuilder of(BlockState state, @Nullable BlockEntity blockEntity) {
		return new GuiBlockEntityRenderBuilder(state, blockEntity);
	}

	public static GuiRenderBuilder of(BlockEntity blockEntity) {
		return of(blockEntity.getBlockState(), blockEntity);
	}

	public static GuiRenderBuilder of(Fluid fluid) {
		return new GuiBlockStateRenderBuilder(fluid.defaultFluidState()
			.createLegacyBlock()
			.setValue(LiquidBlock.LEVEL, 0));
	}

	protected static abstract class GuiRenderState extends AbstractRenderElement implements PictureInPictureRenderState {

		protected @Nullable ScreenRectangle scissorArea;
		protected @Nullable ScreenRectangle bounds;
		protected int color;
		protected int x0, x1, y0, y1;
		protected float scale;

		@Override
		public int x0() {
			return x0;
		}

		@Override
		public int x1() {
			return x1;
		}

		@Override
		public int y0() {
			return y0;
		}

		@Override
		public int y1() {
			return y1;
		}

		@Override
		public float scale() {
			return scale;
		}

		@Override
		public @Nullable ScreenRectangle scissorArea() {
			return scissorArea;
		}

		@Override
		public @Nullable ScreenRectangle bounds() {
			return bounds;
		}
	}

	public static abstract class GuiRenderBuilder extends GuiRenderState {
		protected double xLocal, yLocal, zLocal;
		protected double xRot, yRot, zRot;
		protected double scale = 1;
		protected int color = 0xFFFFFF;
		protected Vec3 rotationOffset = Vec3.ZERO;
		@Nullable
		protected Lighting.Entry customLighting = null;

		public GuiRenderBuilder atLocal(double x, double y, double z) {
			this.xLocal = x;
			this.yLocal = y;
			this.zLocal = z;
			return this;
		}

		public GuiRenderBuilder rotate(double xRot, double yRot, double zRot) {
			this.xRot = xRot;
			this.yRot = yRot;
			this.zRot = zRot;
			return this;
		}

		public GuiRenderBuilder rotateBlock(double xRot, double yRot, double zRot) {
			return this.rotate(xRot, yRot, zRot)
				.withRotationOffset(VecHelper.getCenterOf(BlockPos.ZERO));
		}

		public GuiRenderBuilder scale(double scale) {
			this.scale = scale;
			return this;
		}

		public GuiRenderBuilder color(int color) {
			this.color = color;
			return this;
		}

		public GuiRenderBuilder withRotationOffset(Vec3 offset) {
			this.rotationOffset = offset;
			return this;
		}

		public GuiRenderBuilder lighting(Lighting.Entry lighting) {
			customLighting = lighting;
			return this;
		}

		protected void prepareMatrix(PoseStack poseStack) {
			poseStack.pushPose();
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//			RenderSystem.enableDepthTest();
//			RenderSystem.enableBlend();
//			RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			prepareLighting();
		}

		protected void transformMatrix(PoseStack poseStack) {
			poseStack.translate(x, y, z);
			poseStack.scale((float) scale, (float) scale, (float) scale);
			poseStack.translate(xLocal, yLocal, zLocal);
			UIRenderHelper.flipForGuiRender(poseStack);
			poseStack.translate(rotationOffset.x, rotationOffset.y, rotationOffset.z);
			poseStack.mulPose(Axis.ZP.rotationDegrees((float) zRot));
			poseStack.mulPose(Axis.XP.rotationDegrees((float) xRot));
			poseStack.mulPose(Axis.YP.rotationDegrees((float) yRot));
			poseStack.translate(-rotationOffset.x, -rotationOffset.y, -rotationOffset.z);
		}

		protected void cleanUpMatrix(PoseStack poseStack) {
			poseStack.popPose();
			cleanUpLighting();
		}

		protected void prepareLighting() {
			if (customLighting != null) {
				ILightingSettings.apply(customLighting);
			} else {
				ILightingSettings.ITEMS_3D.apply();
			}
		}

		protected void cleanUpLighting() {
			if (customLighting != null) {
				ILightingSettings.ITEMS_3D.apply();
			}
		}
	}

	protected static class GuiBlockModelRenderBuilder extends GuiRenderBuilder {

		protected BlockStateModel blockModel;
		protected BlockState blockState;
		@Nullable
		protected BlockEntity blockEntity;

		public GuiBlockModelRenderBuilder(BlockStateModel blockmodel, @Nullable BlockState blockState, @Nullable BlockEntity blockEntity) {
			this.blockState = blockState == null ? Blocks.AIR.defaultBlockState() : blockState;
			this.blockModel = blockmodel;
			this.blockEntity = blockEntity;
		}

		@Override
		public void render(GuiGraphics graphics) {
			graphics.guiRenderState.submitPicturesInPictureState(this);
		}

		protected void renderModel(MultiBufferSource.BufferSource buffer,
								   PoseStack ms) {
			SinglePosVirtualBlockGetter level = SinglePosVirtualBlockGetter.createFullBright();
			level.blockState(blockState);
			level.blockEntity(blockEntity);
			BakedModelBufferer.bufferModel(blockModel, BlockPos.ZERO, level, blockState, ms, (layer, shade) -> {
				return new ColoringVertexConsumer(buffer.getBuffer(layer == ChunkSectionLayer.TRANSLUCENT ? Sheets.translucentItemSheet() : Sheets.cutoutBlockSheet()), ARGB.red(color) / 255f, ARGB.green(color) / 255f, ARGB.blue(color) / 255f, 1);
			});

			buffer.endBatch();
		}

	}

	protected static abstract class GuiBlockModelPictureInPictureRenderer<T extends GuiBlockModelRenderBuilder> extends PictureInPictureRenderer<T> {

		protected final OrderedSubmitNodeCollector submitNodeCollector;

		public GuiBlockModelPictureInPictureRenderer(BufferSource bufferSource, OrderedSubmitNodeCollector submitNodeCollector) {
			super(bufferSource);
			this.submitNodeCollector = submitNodeCollector;
		}

		@Override
		protected void renderToTexture(T renderState, PoseStack ms) {
			renderState.prepareMatrix(ms);
			renderState.transformMatrix(ms);
			renderModel(renderState, ms);
			renderState.cleanUpMatrix(ms);
		}

		protected void renderModel(T renderState, PoseStack ms) {
			SinglePosVirtualBlockGetter level = SinglePosVirtualBlockGetter.createFullBright();
			level.blockState(renderState.blockState);
			level.blockEntity(renderState.blockEntity);
			MultiBufferSource.BufferSource buffer = bufferSource;
			int color = renderState.color;
			BakedModelBufferer.bufferModel(renderState.blockModel, BlockPos.ZERO, level, renderState.blockState, ms, (layer, shade) -> {
				return new ColoringVertexConsumer(buffer.getBuffer(layer == ChunkSectionLayer.TRANSLUCENT ? Sheets.translucentItemSheet() : Sheets.cutoutBlockSheet()), ARGB.red(color) / 255f, ARGB.green(color) / 255f, ARGB.blue(color) / 255f, 1);
			});
		}
	}

	public static class GuiBlockEntityRenderBuilder extends GuiBlockModelRenderBuilder {
		public GuiBlockEntityRenderBuilder(BlockState blockState, @Nullable BlockEntity blockEntity) {
			super(
				Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState),
				blockState,
				blockEntity
			);
		}

		@Override
		protected void renderModel(MultiBufferSource.BufferSource buffer, PoseStack ms) {
			renderBlockEntity(buffer, ms);

			super.renderModel(buffer, ms);
		}

		private void renderBlockEntity(MultiBufferSource.BufferSource buffer, PoseStack ms) {
			if (blockEntity == null)
				return;

			BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
			if (renderer == null)
				return;

			BlockState stateBefore = blockEntity.getBlockState();
			blockEntity.setBlockState(blockState);
			renderer.render(blockEntity, /*partials*/0, ms, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
			blockEntity.setBlockState(stateBefore);
		}
	}

	public static class GuiBlockEntityPictureInPictureRenderer extends GuiBlockModelPictureInPictureRenderer<GuiBlockEntityRenderBuilder> {
		public GuiBlockEntityPictureInPictureRenderer(BufferSource bufferSource, OrderedSubmitNodeCollector submitNode) {
			super(bufferSource, submitNode);
		}

		@Override
		public Class<GuiBlockEntityRenderBuilder> getRenderStateClass() {
			return GuiBlockEntityRenderBuilder.class;
		}

		@Override
		protected void renderModel(GuiBlockEntityRenderBuilder renderState, PoseStack ms) {
			renderBlockEntity(renderState, ms);

			super.renderModel(renderState, ms);
		}

		protected void renderBlockEntity(GuiBlockEntityRenderBuilder renderState, PoseStack ms) {
			BlockState blockState = renderState.blockState;
			BlockEntity blockEntity = renderState.blockEntity;

			if (blockEntity == null)
				return;

			BlockEntityRenderer<BlockEntity, ?> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
			if (renderer == null)
				return;

			BlockState stateBefore = blockEntity.getBlockState();
			blockEntity.setBlockState(blockState);
			renderer.submit(renderer.createRenderState(), ms, submitNodeCollector, null);
			blockEntity.setBlockState(stateBefore);
		}

		@Override
		protected String getTextureLabel() {
			return "block entity";
		}
	}

	public static class GuiBlockStateRenderBuilder extends GuiBlockModelRenderBuilder {
		public GuiBlockStateRenderBuilder(BlockState blockstate) {
			super(Minecraft.getInstance()
				.getBlockRenderer()
				.getBlockModel(blockstate), blockstate, null);
		}

		@Override
		protected void renderModel(MultiBufferSource.BufferSource buffer, PoseStack poseStack) {
			if (blockState.getBlock() instanceof BaseFireBlock) {
				ILightingSettings.ITEMS_FLAT.apply();
				super.renderModel(buffer, poseStack);
				ILightingSettings.ITEMS_3D.apply();
				return;
			}

			super.renderModel(buffer, poseStack);

			if (blockState.getFluidState().isEmpty())
				return;

			CatnipClientServices.CLIENT_HOOKS.renderFullFluidState(poseStack, buffer, blockState.getFluidState());

			buffer.endBatch();
		}
	}

	public static class GuiBlockStatePictureInPictureRenderer extends GuiBlockModelPictureInPictureRenderer<GuiBlockStateRenderBuilder> {
		public GuiBlockStatePictureInPictureRenderer(BufferSource bufferSource, OrderedSubmitNodeCollector submitNode) {
			super(bufferSource, submitNode);
		}

		@Override
		public Class<GuiBlockStateRenderBuilder> getRenderStateClass() {
			return GuiBlockStateRenderBuilder.class;
		}

		@Override
		protected void renderToTexture(GuiBlockStateRenderBuilder renderState, PoseStack poseStack) {
			if (renderState.blockState.getBlock() instanceof BaseFireBlock) {
				ILightingSettings.ITEMS_FLAT.apply();
				super.renderToTexture(renderState, poseStack);
				ILightingSettings.ITEMS_3D.apply();
				return;
			}

			super.renderToTexture(renderState, poseStack);

			if (renderState.blockState.getFluidState().isEmpty())
				return;

			FeatureRenderDispatcher featureRenderer = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();

			CatnipClientServices.CLIENT_HOOKS.submitFullFluidState(poseStack, submitNodeCollector, renderState.blockState.getFluidState());
			featureRenderer.renderAllFeatures();
		}

		@Override
		protected String getTextureLabel() {
			return "block state";
		}
	}

	public static class GuiItemRenderBuilder extends GuiRenderBuilder {
		private final ItemStack stack;

		public GuiItemRenderBuilder(ItemStack stack) {
			this.stack = stack;
		}

		public GuiItemRenderBuilder(ItemLike provider) {
			this(new ItemStack(provider));
		}

		@Override
		public void render(GuiGraphics graphics) {
			PoseStack poseStack = graphics.pose();
			prepareMatrix(poseStack);
			transformMatrix(poseStack);
			renderItemIntoGUI(poseStack, stack, customLighting == null);
			graphics.submitEntityRenderState();
			cleanUpMatrix(poseStack);
		}

		public static void renderItemIntoGUI(PoseStack poseStack, ItemStack stack, boolean useDefaultLighting) {
			ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
			BakedModel bakedModel = renderer.getModel(stack, null, null, 0);

			((ItemRendererAccessor) renderer).catnip$getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
			RenderSystem.enableBlend();
			RenderSystem.enableCull();
			RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.pushPose();
			poseStack.translate(0, 0, 100.0F);
			poseStack.translate(8.0F, -8.0F, 0.0F);
			poseStack.scale(16.0F, 16.0F, 16.0F);
			MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
			boolean flatLighting = !bakedModel.usesBlockLight();
			if (useDefaultLighting && flatLighting) {
				Lighting.setupForFlatItems();
			}

			renderer.render(stack, ItemDisplayContext.GUI, false, poseStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedModel);
			RenderSystem.disableDepthTest();
			buffer.endBatch();

			RenderSystem.enableDepthTest();
			if (useDefaultLighting && flatLighting) {
				Lighting.setupFor3DItems();
			}

			poseStack.popPose();
		}

	}

	public static class GuiGameElementPictureInPictureRenderer extends PictureInPictureRenderer<PictureInPictureRenderState> {
		public GuiGameElementPictureInPictureRenderer(BufferSource p_416185_) {
			super(p_416185_);
		}

		@Override
		public Class<PictureInPictureRenderState> getRenderStateClass() {
			return null;
		}

		@Override
		protected String getTextureLabel() {
			return "";
		}

		@Override
		protected void renderToTexture(PictureInPictureRenderState p_415826_, PoseStack p_415928_) {
		}
	}
}
