package net.createmod.catnip.impl.neoforge;

import net.createmod.catnip.api.Catnip;
import net.createmod.catnip.api.client.event.AtlasStitchedCallback;
import net.createmod.catnip.api.client.event.ClientTickCallback;
import net.createmod.catnip.api.client.event.LevelRenderCallback;
import net.createmod.catnip.impl.neoforge.service.NeoForgeRenderPipelineRegistry;
import net.createmod.catnip.impl.neoforge.service.NeoforgeHudElements;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@Mod(value = Catnip.ID, dist = Dist.CLIENT)
public final class CatnipNeoforgeClient {
	public CatnipNeoforgeClient(IEventBus bus) {
		bus.addListener(CatnipNeoforgeClient::loadCompleted);
		bus.addListener(CatnipNeoforgeClient::afterAtlasStitch);
		bus.addListener(NeoforgeHudElements::registerEvent);
		bus.addListener(NeoForgeRenderPipelineRegistry::registerEvent);
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

	public static void afterAtlasStitch(TextureAtlasStitchedEvent event) {
		AtlasStitchedCallback.EVENT.invoker().afterStitch(event.getAtlas());
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		public static void beforeClientTick(ClientTickEvent.Pre event) {
			ClientTickCallback.EVENT.pre().invoker().onTick();
		}

		@SubscribeEvent
		public static void afterClientTick(ClientTickEvent.Post event) {
			ClientTickCallback.EVENT.post().invoker().onTick();
		}

		@SubscribeEvent
		public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentFeatures event) {
			LevelRenderCallback.AFTER_TRANSLUCENT_FEATURES.invoker().onRender(
				event.getLevelRenderer(), event.getLevelRenderState(), event.getPoseStack()
			);
		}
	}
}
