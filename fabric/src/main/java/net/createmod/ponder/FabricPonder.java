package net.createmod.ponder;

import java.util.Map;
import java.util.Set;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.createmod.catnip.command.CatnipCommands;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.net.ConfigPathArgument;
import net.createmod.ponder.command.PonderCommands;
import net.createmod.ponder.enums.PonderConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.config.ModConfig;

import org.jetbrains.annotations.Nullable;

public class FabricPonder implements ModInitializer {
	@Nullable
	private static MinecraftServer server = null;

	@Override
	public void onInitialize() {
		Ponder.init();

		registerConfigs();

		ArgumentTypeRegistry.registerArgumentType(Ponder.id("config_path"), ConfigPathArgument.class,
			SingletonArgumentInfo.contextFree(ConfigPathArgument::new));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			PonderCommands.register(dispatcher);
			CatnipCommands.register(dispatcher);
		});

		ModConfigEvents.loading(Ponder.MOD_ID).register(PonderConfig::onLoad);
		ModConfigEvents.reloading(Ponder.MOD_ID).register(PonderConfig::onReload);

		ServerLifecycleEvents.SERVER_STARTED.register(s -> server = s);
		ServerLifecycleEvents.SERVER_STOPPED.register(s -> server = null);
	}

	private static void registerConfigs() {
		Set<Map.Entry<ModConfig.Type, ConfigBase>> entries = PonderConfig.registerConfigs();
		for (Map.Entry<ModConfig.Type, ConfigBase> entry : entries) {
			ConfigRegistry.INSTANCE.register(Ponder.MOD_ID, entry.getKey(), entry.getValue().specification);
		}
	}

	// TODO - Move maybe?
	@Nullable
	public static MinecraftServer getServer() {
		return server;
	}
}
