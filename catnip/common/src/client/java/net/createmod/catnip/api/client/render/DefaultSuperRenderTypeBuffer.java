package net.createmod.catnip.api.client.render;

import java.util.Map;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.createmod.catnip.impl.client.render.RecordedGeometry;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;

public class DefaultSuperRenderTypeBuffer implements SuperRenderTypeBuffer {
	private static final DefaultSuperRenderTypeBuffer INSTANCE = new DefaultSuperRenderTypeBuffer();

	public static DefaultSuperRenderTypeBuffer getInstance() {
		return INSTANCE;
	}

	protected final SuperRenderTypeBufferPhase earlyBuffer = new SuperRenderTypeBufferPhase(EARLY_ORDER);
	protected final SuperRenderTypeBufferPhase defaultBuffer = new SuperRenderTypeBufferPhase(DEFAULT_ORDER);
	protected final SuperRenderTypeBufferPhase lateBuffer = new SuperRenderTypeBufferPhase(LATE_ORDER);

	private @Nullable SubmitNodeCollector collector;

	@Override
	public void setCollector(@Nullable SubmitNodeCollector collector) {
		this.collector = collector;
	}

	@Override
	public VertexConsumer getEarlyBuffer(RenderType type) {
		return earlyBuffer.buffer(type);
	}

	@Override
	public VertexConsumer getBuffer(RenderType type) {
		return defaultBuffer.buffer(type);
	}

	@Override
	public VertexConsumer getLateBuffer(RenderType type) {
		return lateBuffer.buffer(type);
	}

	@Override
	public void draw() {
		SubmitNodeCollector collector = this.collector;
		if (collector == null) {
			// Nothing to submit through; drop the recordings rather than letting
			// them leak into whatever frame does supply a collector.
			earlyBuffer.discard();
			defaultBuffer.discard();
			lateBuffer.discard();
			return;
		}

		earlyBuffer.submitAll(collector);
		defaultBuffer.submitAll(collector);
		lateBuffer.submitAll(collector);
	}

	@Override
	public void draw(RenderType type) {
		SubmitNodeCollector collector = this.collector;
		if (collector == null) {
			earlyBuffer.discard(type);
			defaultBuffer.discard(type);
			lateBuffer.discard(type);
			return;
		}

		earlyBuffer.submit(collector, type);
		defaultBuffer.submit(collector, type);
		lateBuffer.submit(collector, type);
	}

	public static class SuperRenderTypeBufferPhase {
		private final Map<RenderType, RecordedGeometry> recordings = new Object2ObjectLinkedOpenHashMap<>();
		private final int order;

		public SuperRenderTypeBufferPhase(int order) {
			this.order = order;
		}

		VertexConsumer buffer(RenderType type) {
			return recordings.computeIfAbsent(type, ignored -> new RecordedGeometry());
		}

		void submitAll(SubmitNodeCollector collector) {
			recordings.forEach((type, recording) -> submit(collector, type, recording));
		}

		void submit(SubmitNodeCollector collector, RenderType type) {
			RecordedGeometry recording = recordings.get(type);
			if (recording != null) {
				submit(collector, type, recording);
			}
		}

		private void submit(SubmitNodeCollector collector, RenderType type, RecordedGeometry recording) {
			if (recording.isEmpty()) {
				return;
			}

			// Positions were baked into world space as they were recorded, so the
			// pose handed to the collector is deliberately an identity one.
			collector.order(order)
					.submitCustomGeometry(new PoseStack(), type, (pose, out) -> recording.replayInto(out));
			recording.clear();
		}

		void discard() {
			recordings.values().forEach(RecordedGeometry::clear);
		}

		void discard(RenderType type) {
			RecordedGeometry recording = recordings.get(type);
			if (recording != null) {
				recording.clear();
			}
		}
	}
}
