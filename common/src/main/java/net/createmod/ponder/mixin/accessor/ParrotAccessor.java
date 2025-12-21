package net.createmod.ponder.mixin.accessor;

import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.parrot.Parrot.Variant;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Parrot.class)
public interface ParrotAccessor {
	@Invoker("setVariant")
	void ponder$setVariant(Variant variant);
}
