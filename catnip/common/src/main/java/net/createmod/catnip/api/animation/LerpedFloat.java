package net.createmod.catnip.api.animation;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;

import net.createmod.catnip.api.math.AngleHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class LerpedFloat {

	protected Interpolator interpolator;
	protected float previousValue;
	protected float value;

	@Nullable
	protected Chaser chaseFunction;
	protected float chaseTarget;
	protected float chaseSpeed;
	protected boolean angularChase;

	protected boolean forcedSync;

	public LerpedFloat(Interpolator interpolator) {
		this.interpolator = interpolator;
		startWithValue(0);
		forcedSync = true;
	}

	public static LerpedFloat linear() {
		return new LerpedFloat((p, c, t) -> (float) Mth.lerp(p, c, t));
	}

	public static LerpedFloat angular() {
		LerpedFloat lerpedFloat = new LerpedFloat(AngleHelper::angleLerp);
		lerpedFloat.angularChase = true;
		return lerpedFloat;
	}

	public LerpedFloat startWithValue(double value) {
		float f = (float) value;
		this.previousValue = f;
		this.chaseTarget = f;
		this.value = f;
		return this;
	}

	public LerpedFloat chase(double value, double speed, Chaser chaseFunction) {
		updateChaseTarget((float) value);
		this.chaseSpeed = (float) speed;
		this.chaseFunction = chaseFunction;
		return this;
	}

	public LerpedFloat chaseTimed(double value, int ticks) {
		double diff = value - this.value;
		return chase(value, Math.abs(diff / ticks), Chaser.LINEAR);
	}

	public LerpedFloat disableSmartAngleChasing() {
		angularChase = false;
		return this;
	}

	public void updateChaseTarget(float target) {
		if (angularChase)
			target = value + AngleHelper.getShortestAngleDiff(value, target);
		this.chaseTarget = target;
	}

	public boolean updateChaseSpeed(double speed) {
		float prevSpeed = this.chaseSpeed;
		this.chaseSpeed = (float) speed;
		return !Mth.equal(prevSpeed, speed);
	}

	public void tickChaser() {
		previousValue = value;
		if (chaseFunction == null)
			return;
		if (Mth.equal((double) value, chaseTarget)) {
			value = chaseTarget;
			return;
		}
		value = chaseFunction.chase(value, chaseSpeed, chaseTarget);
	}

	public void setValueNoUpdate(double value) {
		this.value = (float) value;
	}

	public void setValue(double value) {
		this.previousValue = this.value;
		this.value = (float) value;
	}

	public float getValue() {
		return getValue(1);
	}

	public float getValue(float partialTicks) {
		return interpolator.interpolate(partialTicks, previousValue, value);
	}

	public boolean settled() {
		return Mth.equal((double) previousValue, value) && (chaseFunction == null || Mth.equal((double) value, chaseTarget));
	}

	public float getChaseTarget() {
		return chaseTarget;
	}

	public void forceNextSync() {
		forcedSync = true;
	}

	public void write(ValueOutput output) {
		output.putFloat("Speed", chaseSpeed);
		output.putFloat("Target", chaseTarget);
		output.putFloat("Value", value);
		if (forcedSync)
			output.putBoolean("Force", true);
		forcedSync = false;
	}

	public void read(ValueInput input, boolean clientPacket) {
		if (!clientPacket || input.getBooleanOr("Force", false))
			// ValueInput only provides "getFloatOr", but we need the optional behaviour...
			input.read("Value", Codec.FLOAT).ifPresent(this::startWithValue);
		readChaser(input);
	}

	protected void readChaser(ValueInput input) {
		input.read("Speed", Codec.FLOAT).ifPresent(i -> chaseSpeed = i);
		input.read("Target", Codec.FLOAT).ifPresent(i -> chaseTarget = i);
	}

	@FunctionalInterface
	public interface Interpolator {
		float interpolate(double progress, double current, double target);
	}

	@FunctionalInterface
	public interface Chaser {

		Chaser IDLE = (c, s, t) -> (float) c;
		Chaser EXP = exp(Double.MAX_VALUE);
		Chaser LINEAR = (c, s, t) -> (float) (c + Mth.clamp(t - c, -s, s));

		static Chaser exp(double maxEffectiveSpeed) {
			return (c, s, t) -> (float) (c + Mth.clamp((t - c) * s, -maxEffectiveSpeed, maxEffectiveSpeed));
		}

		float chase(double current, double speed, double target);
	}

}
