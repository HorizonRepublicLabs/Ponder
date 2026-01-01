package net.createmod.ponder.mixin.accessor;

import net.minecraft.core.Holder;

import net.minecraft.core.HolderOwner;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface Holder$ReferenceAccessor<T> {
	@Accessor("owner")
	HolderOwner<T> ponder$getOwner();

	@Invoker("bindValue")
	void ponder$bindValue(T value);
}
