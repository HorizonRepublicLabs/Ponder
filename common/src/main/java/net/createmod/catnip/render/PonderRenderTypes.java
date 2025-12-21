package net.createmod.catnip.render;

import java.util.function.BiFunction;

import net.createmod.ponder.Ponder;
import net.createmod.ponder.enums.PonderSpecialTextures;
import net.createmod.ponder.mixin.client.accessor.RenderTypeAccessor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public abstract class PonderRenderTypes {
	private static final RenderType OUTLINE_SOLID = RenderTypeAccessor.catnip$create(
		createLayerName("outline_solid"),
		RenderSetup.builder(RenderPipelines.ENTITY_SOLID)
			.bufferSize(256)
			.withTexture("Sampler0", PonderSpecialTextures.BLANK.getId())
			.useLightmap()
			.useOverlay()
			.createRenderSetup()
	);

	private static final BiFunction<Identifier, Boolean, RenderType> OUTLINE_TRANSLUCENT = Util.memoize((texture, cull) ->
		RenderTypeAccessor.catnip$create(
			createLayerName("outline_translucent" + (cull ? "_cull" : "")),
			RenderSetup.builder(cull ? RenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL : RenderPipelines.ENTITY_TRANSLUCENT)
				.bufferSize(256)
				.withTexture("Sampler0", texture)
				.sortOnUpload()
				.useLightmap()
				.useOverlay()
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.createRenderSetup()
		)
	);

	public static RenderType outlineSolid() {
		return OUTLINE_SOLID;
	}

	public static RenderType outlineTranslucent(Identifier texture, boolean cull) {
		return OUTLINE_TRANSLUCENT.apply(texture, cull);
	}

	private static String createLayerName(String name) {
		return Ponder.MOD_ID + ":" + name;
	}
}
