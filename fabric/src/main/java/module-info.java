import org.jspecify.annotations.NullMarked;

@NullMarked
open module net.createmod {
	requires mixinextras.fabric;
	requires net.fabricmc.loader;
	requires org.jetbrains.annotations;
	requires org.jspecify;
	requires org.spongepowered.mixin;
}
