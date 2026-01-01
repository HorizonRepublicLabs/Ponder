package net.createmod.catnip.registration;

import net.createmod.catnip.registration.holder.BlockHolder;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TestRegistration {
	public static CatnipRegistry REGISTRY = new CatnipRegistry("create");

	public static BlockHolder<RedstoneLampBlock> BLOCK = REGISTRY.block("test", RedstoneLampBlock::new)
		.properties(Properties::air)
		.register();

	public static void init() {}
}
