package net.createmod.catnip.impl.neoforge;

import net.createmod.catnip.api.Catnip;
import net.createmod.catnip.api.config.ConfigPathArgument;
import net.createmod.catnip.impl.command.CatnipCommands;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Catnip.ID)
public final class CatnipNeoforge {
	private static final DeferredRegister<ArgumentTypeInfo<?, ?>> commandArgumentTypes = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Catnip.ID);

	private static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<ConfigPathArgument>> CONFIG_PATH_ARGUMENT_TYPE = commandArgumentTypes.register(
		"config_path", () -> ArgumentTypeInfos.registerByClass(ConfigPathArgument.class, SingletonArgumentInfo.contextFree(ConfigPathArgument::new))
	);

	public CatnipNeoforge(IEventBus bus) {
		commandArgumentTypes.register(bus);
		bus.addListener(CatnipNeoforge::setup);

		NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> CatnipCommands.register(event.getDispatcher()));
	}

	public static void setup(FMLCommonSetupEvent event) {
		Catnip.init();
	}
}
