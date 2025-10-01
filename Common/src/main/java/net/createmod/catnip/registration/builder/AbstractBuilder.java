package net.createmod.catnip.registration.builder;

import net.createmod.catnip.platform.Loader;
import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.Registration;
import net.createmod.catnip.registration.holder.BaseHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractBuilder<R, T extends R, H extends BaseHolder<T>> {
	private final CatnipRegistry owner;
	private final ResourceLocation id;
	private final Registry<R> registry;

	public AbstractBuilder(CatnipRegistry owner, String name, Registry<R> registry) {
		this.owner = owner;
		this.id = ResourceLocation.fromNamespaceAndPath(owner.modId, name);
		this.registry = registry;
	}

	abstract T build();

	abstract H getHolder(HolderOwner<R> owner, ResourceKey<R> key);

	public H register() {
		ResourceKey<R> key = ResourceKey.create(registry.key(), id);
		H holder = getHolder(registry.holderOwner(), key);
		Registration<R, T> registration = new Registration<>(id, registry, this::build, holder);

		if (Loader.FABRIC.isCurrent()) {
			registration.register();
		} else {
			CatnipRegistry.addToRegistrationMap(registry, registration);
		}

		return holder;
	}
}
