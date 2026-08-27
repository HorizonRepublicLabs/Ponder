package net.createmod.catnip.api.config;

/// The slice of a mod config specification builder that [ConfigBase] needs.
///
/// ConfigBase lives in the multi-loader common source set, which cannot see
/// NeoForge's ModConfigSpec - that mismatch is why config compilation was
/// disabled wholesale. Rather than move config onto one loader, the handful of
/// builder operations actually used are named here and implemented per
/// platform.
public interface ConfigSpecBuilder {
	ConfigSpecBuilder comment(String... comment);

	ConfigSpecBuilder push(String path);

	ConfigSpecBuilder pop(int count);

	ConfigValueAccess<Boolean> define(String name, boolean defaultValue);

	<T extends Enum<T>> ConfigValueAccess<T> defineEnum(String name, T defaultValue);

	ConfigValueAccess<Double> defineInRange(String name, double defaultValue, double min, double max);

	ConfigValueAccess<Integer> defineInRange(String name, int defaultValue, int min, int max);
}
