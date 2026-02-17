package net.createmod.catnip.api.client.gui.element;

import java.util.Objects;

import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.createmod.catnip.api.client.gui.ILightingSettings;
import net.createmod.catnip.api.client.gui.UIRenderHelper;
import net.createmod.catnip.api.client.level.SinglePosVirtualBlockGetter;
import net.createmod.catnip.api.client.platform.ModClientHooksHelper;
import net.createmod.catnip.api.client.render.model.BakedModelBufferer;
import net.createmod.catnip.api.math.VecHelper;
import net.createmod.catnip.impl.client.render.ColoringVertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
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
        return new GuiBlockStateRenderBuilder(
                fluid.defaultFluidState().createLegacyBlock().setValue(LiquidBlock.LEVEL, 0));
    }

    public abstract static class GuiRenderBuilder extends AbstractRenderElement {
        protected double xLocal, yLocal, zLocal;
        protected double xRot, yRot, zRot;
        protected double scale = 1;
        protected int color = 0xFFFFFF;
        protected Vec3 rotationOffset = Vec3.ZERO;

        @Nullable
        protected ILightingSettings customLighting = null;

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

        public GuiRenderBuilder lighting(ILightingSettings lighting) {
            customLighting = lighting;
            return this;
        }

        protected void prepareMatrix(PoseStack poseStack) {
            poseStack.pushPose();
            // RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            // RenderSystem.enableDepthTest();
            // RenderSystem.enableBlend();
            // RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
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

        protected void cleanUpMatrix(Matrix3x2fStack poseStack) {
            poseStack.popMatrix();
            cleanUpLighting();
        }

        protected void prepareLighting() {
            Objects.requireNonNullElse(customLighting, ILightingSettings.ITEMS_3D)
                    .apply();
        }

        protected void cleanUpLighting() {
            if (customLighting != null) {
                ILightingSettings.ITEMS_3D.apply();
            }
        }
    }

    protected static class GuiBlockModelRenderBuilder extends GuiRenderBuilder {
        protected BlockStateModel blockStateModel;
        protected BlockState blockState;

        @Nullable
        protected BlockEntity blockEntity;

        public GuiBlockModelRenderBuilder(
                BlockStateModel blockStateModel,
                @Nullable BlockState blockState,
                @Nullable BlockEntity blockEntity) {
            this.blockState = blockState == null ? Blocks.AIR.defaultBlockState() : blockState;
            this.blockStateModel = blockStateModel;
            this.blockEntity = blockEntity;
        }

        @Override
        public void render(GuiGraphics graphics) {
            // TODO
            //			PoseStack poseStack = graphics.pose();
            //			prepareMatrix(poseStack);
            //
            //			MultiBufferSource.BufferSource buffer = graphics.bufferSource();
            //
            //			transformMatrix(poseStack);
            //
            //			RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            //			renderModel(buffer, poseStack);
            //
            //			cleanUpMatrix(poseStack);
        }

        protected void renderModel(MultiBufferSource.BufferSource buffer, PoseStack ms) {
            SinglePosVirtualBlockGetter level = SinglePosVirtualBlockGetter.createFullBright();
            level.blockState(blockState);
            level.blockEntity(blockEntity);
            BakedModelBufferer.bufferModel(
                    blockStateModel, BlockPos.ZERO, level, blockState, ms, (layer, shade) -> {
                        RenderType type = layer == ChunkSectionLayer.TRANSLUCENT
                                ? Sheets.translucentBlockItemSheet()
                                : Sheets.cutoutBlockSheet();
                        return new ColoringVertexConsumer(
                                buffer.getBuffer(type),
                                ARGB.red(color) / 255f,
                                ARGB.green(color) / 255f,
                                ARGB.blue(color) / 255f,
                                1);
                    });

            buffer.endBatch();
        }
    }

    public static class GuiBlockEntityRenderBuilder extends GuiBlockModelRenderBuilder {
        public GuiBlockEntityRenderBuilder(
                BlockState blockState, @Nullable BlockEntity blockEntity) {
            super(
                    Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState),
                    blockState,
                    blockEntity);
        }

        @Override
        protected void renderModel(MultiBufferSource.BufferSource buffer, PoseStack ms) {
            renderBlockEntity(buffer, ms);

            super.renderModel(buffer, ms);
        }

        private void renderBlockEntity(MultiBufferSource.BufferSource buffer, PoseStack ms) {
            // TODO
            //			if (blockEntity == null)
            //				return;
            //
            //			BlockEntityRenderer<BlockEntity> renderer =
            // Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
            //			if (renderer == null)
            //				return;
            //
            //			BlockState stateBefore = blockEntity.getBlockState();
            //			blockEntity.setBlockState(blockState);
            //			renderer.render(blockEntity, /*partials*/0, ms, buffer, LightCoordsUtil.FULL_BRIGHT,
            // OverlayTexture.NO_OVERLAY);
            //			blockEntity.setBlockState(stateBefore);
        }
    }

    public static class GuiBlockStateRenderBuilder extends GuiBlockModelRenderBuilder {
        public GuiBlockStateRenderBuilder(BlockState blockstate) {
            super(
                    Minecraft.getInstance().getBlockRenderer().getBlockModel(blockstate),
                    blockstate,
                    null);
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

            if (blockState.getFluidState().isEmpty()) return;

            ModClientHooksHelper.INSTANCE.renderFullFluidState(
                    poseStack, buffer, blockState.getFluidState());

            buffer.endBatch();
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
            // PoseStack poseStack = graphics.pose();
            // prepareMatrix(poseStack);
            // transformMatrix(poseStack);
			// graphics.renderItem(this.stack, (int) this.x, (int) this.y);
            // cleanUpMatrix(poseStack);
        }
    }
}
