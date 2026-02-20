package net.createmod.catnip.impl.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.Nullable;

import net.createmod.catnip.api.event.CatnipEvent;

public final class CatnipEventImpl<Callback> implements CatnipEvent<Callback> {
	private final List<Callback> callbacks;
	private final InvokerFactory<Callback> invokerFactory;

	@Nullable
	private Callback cachedInvoker;

	public CatnipEventImpl(InvokerFactory<Callback> invokerFactory) {
		this.callbacks = new ArrayList<>();
		this.invokerFactory = invokerFactory;
	}

	@Override
	public synchronized void subscribe(Callback callback) {
		this.callbacks.add(callback);
		this.cachedInvoker = null;
	}

	@Override
	public Callback invoker() {
		if (this.cachedInvoker == null) {
			this.cachedInvoker = this.createInvoker();
		}

		return this.cachedInvoker;
	}

	private Callback createInvoker() {
		if (this.callbacks.size() == 1) {
			return this.callbacks.getFirst();
		}

		return this.invokerFactory.create(Collections.unmodifiableList(this.callbacks));
	}
}
