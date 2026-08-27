package net.createmod.catnip.api.client.render;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;

/// Buffers geometry in three ordered phases and flushes it through a
/// [SubmitNodeCollector].
///
/// This used to extend MultiBufferSource, which 26.2 removed along with the whole
/// immediate-mode buffer source concept. The three phases now map onto
/// [SubmitNodeCollector#order], so early geometry still draws before default and
/// default before late.
///
/// Because submission happens inside a collector callback rather than at the call
/// site, the collector has to be handed over before anything is buffered - see
/// [#setCollector]. Callers that only draw are unaffected.
public interface SuperRenderTypeBuffer {
	int EARLY_ORDER = -1;
	int DEFAULT_ORDER = 0;
	int LATE_ORDER = 1;

	/// Supplies the collector that [#draw()] will submit through, for the current frame.
	void setCollector(@Nullable SubmitNodeCollector collector);

	VertexConsumer getEarlyBuffer(RenderType type);

	VertexConsumer getBuffer(RenderType type);

	VertexConsumer getLateBuffer(RenderType type);

	default VertexConsumer getEarlyBuffer(ChunkSectionLayer layer) {
		return getEarlyBuffer(RenderHelper.convertLayerToType(layer));
	}

	default VertexConsumer getBuffer(ChunkSectionLayer layer) {
		return getBuffer(RenderHelper.convertLayerToType(layer));
	}

	default VertexConsumer getLateBuffer(ChunkSectionLayer layer) {
		return getLateBuffer(RenderHelper.convertLayerToType(layer));
	}

	void draw();

	void draw(RenderType type);
}
