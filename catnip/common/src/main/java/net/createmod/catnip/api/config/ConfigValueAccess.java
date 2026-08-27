package net.createmod.catnip.api.config;

/// A single configured value, independent of which config system backs it.
public interface ConfigValueAccess<V> {
	V get();

	void set(V value);

	/// Persists the value. Separate from [#set] because the backing systems
	/// treat writing and saving as distinct steps.
	void save();
}
