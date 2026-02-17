package net.createmod.catnip.impl;

import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

public class ServiceHelper {
	public static <T> T load(Class<T> clazz) {
		ServiceLoader<T> loader = ServiceLoader.load(clazz);
		List<Provider<T>> providers = loader.stream().toList();
		if (providers.isEmpty()) {
			throw new IllegalStateException("Failed to find implementation of " + clazz);
		} else if (providers.size() > 1) {
			throw new IllegalStateException("Found more than one implementation of " + clazz);
		}

		// if an error occurs during instantiation the error message is reasonable
		return providers.getFirst().get();
	}
}
