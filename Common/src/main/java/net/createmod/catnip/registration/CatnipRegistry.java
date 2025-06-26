package net.createmod.catnip.registration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.google.common.collect.Multimaps;

import net.createmod.catnip.registration.builder.BlockBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Function;

public class CatnipRegistry {
	public final String modId;

	private static final Multimap<Registry<?>, Registration<?, ?>> REGISTRATIONS = HashMultimap.create();
	public static final Multimap<Registry<?>, Registration<?, ?>> REGISTRATIONS_VIEW = Multimaps.unmodifiableMultimap(REGISTRATIONS);

	public CatnipRegistry(String modId) {
		this.modId = modId;
	}

	@Internal
	public static <R, S extends R> void addToRegistrationMap(Registry<R> registry, Registration<R, S> registration) {
		REGISTRATIONS.put(registry, registration);
	}

	public <R extends Block> BlockBuilder<R> block(String id, Function<BlockBehaviour.Properties, R> func) {
		return new BlockBuilder<>(this, id, func);
	}
}
