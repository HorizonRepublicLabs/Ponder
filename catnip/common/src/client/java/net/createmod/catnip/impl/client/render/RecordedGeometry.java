package net.createmod.catnip.impl.client.render;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.blaze3d.vertex.VertexConsumer;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/// A [VertexConsumer] that records what was written to it so it can be replayed later.
///
/// 26.2 removed MultiBufferSource, so there is no longer an immediate-mode buffer
/// source handing out a VertexConsumer per RenderType. Geometry now has to be
/// emitted from inside a [net.minecraft.client.renderer.SubmitNodeCollector]
/// callback, which runs after the caller has finished.
///
/// Callers that still want to draw imperatively write into one of these, and the
/// owning buffer replays the recording into the real consumer once the collector
/// invokes it. Positions are already baked into world space by the PoseStack
/// overloads on VertexConsumer, so a recording can be replayed under an identity
/// pose without losing its transform.
@ApiStatus.Internal
public final class RecordedGeometry implements VertexConsumer {
	private static final int OP_VERTEX = 0;
	private static final int OP_COLOR = 1;
	private static final int OP_UV = 2;
	private static final int OP_UV1 = 3;
	private static final int OP_UV2 = 4;
	private static final int OP_NORMAL = 5;
	private static final int OP_LINE_WIDTH = 6;

	private final IntArrayList ops = new IntArrayList();
	private final FloatArrayList floats = new FloatArrayList();
	private final IntArrayList ints = new IntArrayList();

	public boolean isEmpty() {
		return ops.isEmpty();
	}

	public void clear() {
		ops.clear();
		floats.clear();
		ints.clear();
	}

	public void replayInto(VertexConsumer out) {
		int floatIdx = 0;
		int intIdx = 0;

		for (int i = 0; i < ops.size(); i++) {
			switch (ops.getInt(i)) {
				case OP_VERTEX -> {
					out.addVertex(floats.getFloat(floatIdx), floats.getFloat(floatIdx + 1), floats.getFloat(floatIdx + 2));
					floatIdx += 3;
				}
				case OP_COLOR -> out.setColor(ints.getInt(intIdx++));
				case OP_UV -> {
					out.setUv(floats.getFloat(floatIdx), floats.getFloat(floatIdx + 1));
					floatIdx += 2;
				}
				case OP_UV1 -> {
					out.setUv1(ints.getInt(intIdx), ints.getInt(intIdx + 1));
					intIdx += 2;
				}
				case OP_UV2 -> {
					out.setUv2(ints.getInt(intIdx), ints.getInt(intIdx + 1));
					intIdx += 2;
				}
				case OP_NORMAL -> {
					out.setNormal(floats.getFloat(floatIdx), floats.getFloat(floatIdx + 1), floats.getFloat(floatIdx + 2));
					floatIdx += 3;
				}
				case OP_LINE_WIDTH -> out.setLineWidth(floats.getFloat(floatIdx++));
				default -> throw new IllegalStateException("Unknown recorded vertex op");
			}
		}
	}

	@Override
	public VertexConsumer addVertex(float x, float y, float z) {
		ops.add(OP_VERTEX);
		floats.add(x);
		floats.add(y);
		floats.add(z);
		return this;
	}

	@Override
	public VertexConsumer setColor(int r, int g, int b, int a) {
		// Normalise to the packed form so replay only has to handle one opcode.
		return setColor((a << 24) | (r << 16) | (g << 8) | b);
	}

	@Override
	public VertexConsumer setColor(int color) {
		ops.add(OP_COLOR);
		ints.add(color);
		return this;
	}

	@Override
	public VertexConsumer setUv(float u, float v) {
		ops.add(OP_UV);
		floats.add(u);
		floats.add(v);
		return this;
	}

	@Override
	public VertexConsumer setUv1(int u, int v) {
		ops.add(OP_UV1);
		ints.add(u);
		ints.add(v);
		return this;
	}

	@Override
	public VertexConsumer setUv2(int u, int v) {
		ops.add(OP_UV2);
		ints.add(u);
		ints.add(v);
		return this;
	}

	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		ops.add(OP_NORMAL);
		floats.add(x);
		floats.add(y);
		floats.add(z);
		return this;
	}

	@Override
	public VertexConsumer setLineWidth(float width) {
		ops.add(OP_LINE_WIDTH);
		floats.add(width);
		return this;
	}
}
