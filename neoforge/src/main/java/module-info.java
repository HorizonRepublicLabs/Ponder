import org.jspecify.annotations.NullMarked;

@NullMarked
open module net.createmod {
	requires flywheel.neoforge.api;
	requires fml_loader;
	requires neoforge;
	requires net.neoforged.bus;
	requires net.neoforged.mergetool.api;
	requires org.jetbrains.annotations;
	requires org.jspecify;
	requires org.spongepowered.mixin;
}
