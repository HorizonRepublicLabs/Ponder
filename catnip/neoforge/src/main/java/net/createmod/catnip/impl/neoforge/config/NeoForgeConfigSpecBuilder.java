package net.createmod.catnip.impl.neoforge.config;

import net.createmod.catnip.api.config.ConfigSpecBuilder;
import net.createmod.catnip.api.config.ConfigValueAccess;

import net.neoforged.neoforge.common.ModConfigSpec;

/// Backs catnip's config API with NeoForge's ModConfigSpec.
///
/// The common source set cannot see ModConfigSpec, so ConfigBase talks to
/// [ConfigSpecBuilder] and this adapts it. Every method here is a direct
/// forward - the abstraction exists to keep the type out of common, not to
/// change behaviour.
public final class NeoForgeConfigSpecBuilder implements ConfigSpecBuilder {
	private final ModConfigSpec.Builder builder;

	public NeoForgeConfigSpecBuilder(ModConfigSpec.Builder builder) {
		this.builder = builder;
	}

	public ModConfigSpec.Builder unwrap() {
		return builder;
	}

	@Override
	public ConfigSpecBuilder comment(String... comment) {
		builder.comment(comment);
		return this;
	}

	@Override
	public ConfigSpecBuilder push(String path) {
		builder.push(path);
		return this;
	}

	@Override
	public ConfigSpecBuilder pop(int count) {
		builder.pop(count);
		return this;
	}

	@Override
	public ConfigValueAccess<Boolean> define(String name, boolean defaultValue) {
		return wrap(builder.define(name, defaultValue));
	}

	@Override
	public <T extends Enum<T>> ConfigValueAccess<T> defineEnum(String name, T defaultValue) {
		return wrap(builder.defineEnum(name, defaultValue));
	}

	@Override
	public ConfigValueAccess<Double> defineInRange(String name, double defaultValue, double min, double max) {
		return wrap(builder.defineInRange(name, defaultValue, min, max));
	}

	@Override
	public ConfigValueAccess<Integer> defineInRange(String name, int defaultValue, int min, int max) {
		return wrap(builder.defineInRange(name, defaultValue, min, max));
	}

	private static <V> ConfigValueAccess<V> wrap(ModConfigSpec.ConfigValue<V> value) {
		return new ConfigValueAccess<>() {
			@Override
			public V get() {
				return value.get();
			}

			@Override
			public void set(V newValue) {
				value.set(newValue);
			}

			@Override
			public void save() {
				value.save();
			}
		};
	}
}
