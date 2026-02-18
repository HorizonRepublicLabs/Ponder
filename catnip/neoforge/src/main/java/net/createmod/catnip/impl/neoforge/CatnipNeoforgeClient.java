package net.createmod.catnip.impl.neoforge;

import net.createmod.catnip.api.Catnip;
import net.createmod.catnip.api.client.animation.AnimationTickHolder;
import net.createmod.catnip.api.client.level.wrapper.WrappedClientLevel;
import net.createmod.catnip.api.client.render.StitchedSprite;
import net.createmod.catnip.impl.client.CatnipClient;
import net.createmod.catnip.impl.client.CatnipReloadListener;
import net.createmod.catnip.impl.client.placement.PlacementClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelAccessor;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent.Pre;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.level.LevelEvent.Load;
import net.neoforged.neoforge.event.level.LevelEvent.Unload;

@Mod(value = Catnip.ID, dist = Dist.CLIENT)
public final class CatnipNeoforgeClient {
	public CatnipNeoforgeClient(IEventBus bus) {
		bus.addListener(CatnipNeoforgeClient::loadCompleted);
		bus.addListener(CatnipNeoforgeClient::registerClientReloadListeners);
		bus.addListener(CatnipNeoforgeClient::onTextureStitchPost);
	}

	private static void loadCompleted(FMLLoadCompleteEvent event) {
		// FIXME: config
		// ModContainer modContainer = ModList.get()
		// 	.getModContainerById(Ponder.MOD_ID)
		// 	.orElseThrow(() -> new IllegalStateException("Ponder Mod Container missing after loadCompleted"));
		//
		// Supplier<IConfigScreenFactory> configScreen = () ->
		// 	(mc, previousScreen) -> new BaseConfigScreen(previousScreen, Ponder.MOD_ID);
		// modContainer.registerExtensionPoint(IConfigScreenFactory.class, configScreen);
		//
		// BaseConfigScreen.setDefaultActionFor(Ponder.MOD_ID, base -> base
		// 	.withButtonLabels("Client Settings", null, null)
		// 	.withSpecs(PonderConfig.client().specification, null, null)
		// );
	}

	@SubscribeEvent
	public static void registerClientReloadListeners(AddClientReloadListenersEvent event) {
		event.addListener(CatnipReloadListener.ID, CatnipReloadListener.INSTANCE);
	}

	@SubscribeEvent
	public static void onTextureStitchPost(TextureAtlasStitchedEvent event) {
		StitchedSprite.onTextureStitchPost(event.getAtlas());
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		public static void onTickPre(Pre event) {
			CatnipClient.onTick();
		}

		@SubscribeEvent
		public static void onRenderWorld(RenderLevelStageEvent.AfterParticles event) {
			CatnipClient.onRenderWorld(event.getPoseStack());
		}

		@SubscribeEvent
		public static void onLoadWorld(Load event) {
			LevelAccessor level = event.getLevel();

			if (!level.isClientSide())
				return;

			if (level instanceof ClientLevel && !(level instanceof WrappedClientLevel)) {
				CatnipClient.invalidateRenderers();
				AnimationTickHolder.reset();
			}
		}

		@SubscribeEvent
		public static void onUnloadWorld(Unload event) {
			if (!event.getLevel().isClientSide())
				return;

			CatnipClient.invalidateRenderers();
			AnimationTickHolder.reset();
		}

		@SubscribeEvent
		public static void afterRenderOverlayLayer(RenderGuiLayerEvent.Post event) {
			if (!event.getName().equals(VanillaGuiLayers.CROSSHAIR))
				return;

			PlacementClient.onRenderCrosshairOverlay(event.getGuiGraphics(), AnimationTickHolder.getPartialTicksUI());
		}
	}
}
