package net.createmod.ponder.foundation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;

import org.joml.Matrix4fStack;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public class PonderWorldParticles {
	private final Map<ParticleRenderType, Queue<Particle>> byType = Maps.newIdentityHashMap();
	private final Queue<Particle> queue = Queues.newArrayDeque();

	PonderLevel world;

	public PonderWorldParticles(PonderLevel world) {
		this.world = world;
	}

	public void addParticle(Particle p) {
		this.queue.add(p);
	}

	public void tick() {
		this.byType.forEach((renderType, queue) -> this.tickParticleList(queue));

		Particle particle;
		if (queue.isEmpty())
			return;
		while ((particle = this.queue.poll()) != null)
			this.byType.computeIfAbsent(particle.getGroup(), $ -> EvictingQueue.create(16384))
				.add(particle);
	}

	private void tickParticleList(Collection<Particle> particles) {
		if (particles.isEmpty())
			return;

		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle particle = iterator.next();
			particle.tick();
			if (!particle.isAlive())
				iterator.remove();
		}
	}

	public void renderParticles(PoseStack ms, SubmitNodeCollector queue, Camera camera,
								CameraRenderState cameraRenderState, float pt) {
		Minecraft mc = Minecraft.getInstance();
		LightTexture lightTexture = mc.gameRenderer.lightTexture();

		lightTexture.turnOnLightLayer();
		RenderSystem.enableDepthTest();
		Matrix4fStack stack = RenderSystem.getModelViewStack();
		stack.pushMatrix();
		stack.mul(ms.last().pose());
		RenderSystem.applyModelViewMatrix();

		for (ParticleRenderType iparticlerendertype : this.byType.keySet()) {
			if (iparticlerendertype == ParticleRenderType.NO_RENDER)
				continue;
			Iterable<Particle> iterable = this.byType.get(iparticlerendertype);
			if (iterable != null) {
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShader(GameRenderer::getParticleShader);

				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder bufferBuilder = iparticlerendertype.begin(tesselator, mc.getTextureManager());

				if (bufferBuilder != null) {
					for (Particle particle : iterable)
						particle.render(bufferBuilder, camera, pt);

					MeshData meshData = bufferBuilder.build();
					if (meshData != null)
						BufferUploader.drawWithShader(meshData);
				}
			}
		}

		stack.popMatrix();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
		lightTexture.turnOffLightLayer();
	}

	public void clearEffects() {
		this.byType.clear();
	}

}
