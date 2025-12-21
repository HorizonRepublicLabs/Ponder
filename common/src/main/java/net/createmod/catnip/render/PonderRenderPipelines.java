package net.createmod.catnip.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.createmod.ponder.Ponder;
import net.minecraft.client.renderer.RenderPipelines;

public class PonderRenderPipelines {
	public static final RenderPipeline POSITION_COLOR_TRIANGLES = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Ponder.id("pipeline/position_color_triangles"))
		.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
		.withCull(false)
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
		.build();

	public static final RenderPipeline POSITION_COLOR_STRIP = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
		.withLocation(Ponder.id("pipeline/position_color_strip"))
		.withCull(false)
		.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
		.build();
}
