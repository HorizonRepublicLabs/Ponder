package net.createmod.catnip.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface Environment {
	EnvType value();

	enum EnvType {
		CLIENT, SERVER;
	}
}
