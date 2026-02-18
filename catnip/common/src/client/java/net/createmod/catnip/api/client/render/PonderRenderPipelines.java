package net.createmod.catnip.api.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.createmod.catnip.api.Catnip;
import net.minecraft.client.renderer.RenderPipelines;

public class PonderRenderPipelines {
	public static final RenderPipeline POSITION_COLOR_TRIANGLES = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Catnip.id("pipeline/position_color_triangles"))
		.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
		.withCull(false)
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, Mode.TRIANGLES)
		.build();

	public static final RenderPipeline POSITION_COLOR_STRIP = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Catnip.id("pipeline/position_color_strip"))
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, Mode.TRIANGLE_STRIP)
		.build();

	public static final RenderPipeline TRIANGLE_FAN = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Catnip.id("pipeline/trangle_fan"))
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, Mode.TRIANGLE_FAN)
		.build();
}
