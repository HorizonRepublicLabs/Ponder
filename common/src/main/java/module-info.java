import org.jspecify.annotations.NullMarked;

@NullMarked
open module net.createmod {
	requires brigadier;
	requires com.electronwill.nightconfig.core;
	requires com.google.common;
	requires datafixerupper;
	requires flywheel.common.mojmap.api;
	requires forgeconfigapiport.common.neoforgeapi;
	requires io.netty.buffer;
	requires it.unimi.dsi.fastutil;
	requires java.desktop;
	requires jopt.simple;
	requires org.apache.commons.lang3;
	requires org.apache.logging.log4j;
	requires org.jetbrains.annotations;
	requires org.joml;
	requires org.jspecify;
	requires org.lwjgl.glfw;
	requires org.lwjgl.opengl;
	requires org.slf4j;
	requires org.spongepowered.mixin;
	requires vanilla;
}
