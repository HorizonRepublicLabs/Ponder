package net.createmod.catnip.api.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Unit;

// TODO - Everything here needs to be rethought with how codecs exist now and should be used everywhere they can
@Deprecated(forRemoval = true)
public class NBTHelper {
	public static void putMarker(CompoundTag nbt, String marker) {
		nbt.store(marker, Unit.CODEC, Unit.INSTANCE);
	}

	// Backwards compatible with 1.20
	public static BlockPos readBlockPos(CompoundTag nbt, String key) {
		Optional<BlockPos> pos = nbt.read(key, BlockPos.CODEC);
		if (pos.isPresent())
			return pos.get();
		CompoundTag oldTag = nbt.getCompoundOrEmpty(key);
		return new BlockPos(
			oldTag.getIntOr("X", 0),
			oldTag.getIntOr("Y", 0),
			oldTag.getIntOr("Z", 0)
		);
	}

	public static <T> ListTag writeCompoundList(Iterable<T> list, Function<T, CompoundTag> serializer) {
		ListTag listNBT = new ListTag();
		list.forEach(t -> {
			CompoundTag apply = serializer.apply(t);
			if (apply == null)
				return;
			listNBT.add(apply);
		});
		return listNBT;
	}

	public static <T> List<T> readCompoundList(ListTag listNBT, Function<CompoundTag, T> deserializer) {
		List<T> list = new ArrayList<>(listNBT.size());
		listNBT.forEach(inbt -> list.add(deserializer.apply((CompoundTag) inbt)));
		return list;
	}

	public static void iterateCompoundList(ListTag listNBT, Consumer<CompoundTag> consumer) {
		listNBT.forEach(inbt -> consumer.accept((CompoundTag) inbt));
	}

	public static Tag getINBT(CompoundTag nbt, String id) {
		Tag inbt = nbt.get(id);
		if (inbt != null)
			return inbt;
		return new CompoundTag();
	}

	public static CompoundTag intToCompound(int i) {
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.putInt("V", i);
		return compoundTag;
	}

	public static int intFromCompound(CompoundTag compoundTag) {
		return compoundTag.getIntOr("V", 0);
	}

	/// Writes an enum constant by name.
	///
	/// Storing the name rather than the ordinal keeps saves readable and, more
	/// importantly, survives constants being reordered.
	public static <T extends Enum<T>> void writeEnum(CompoundTag nbt, String key, T enumConstant) {
		nbt.putString(key, enumConstant.name());
	}

	/// Reads an enum constant written by [#writeEnum].
	///
	/// Falls back to the first constant when the key is missing or names a
	/// constant that no longer exists, so an old save cannot crash the load.
	public static <T extends Enum<T>> T readEnum(CompoundTag nbt, String key, Class<T> enumClass) {
		T[] constants = enumClass.getEnumConstants();
		if (constants == null || constants.length == 0) {
			throw new IllegalArgumentException("Non-enum class passed to readEnum: " + enumClass);
		}
		String name = nbt.getStringOr(key, "");
		for (T constant : constants) {
			if (constant.name().equals(name)) {
				return constant;
			}
		}
		return constants[0];
	}

	public static void writeResourceLocation(CompoundTag nbt, String key, Identifier location) {
		nbt.putString(key, location.toString());
	}

	public static Identifier readResourceLocation(CompoundTag nbt, String key) {
		return Identifier.parse(nbt.getStringOr(key, ""));
	}

	/// Writes item stacks as a list, skipping empty ones.
	///
	/// ItemStack#save and #parse were replaced by codecs in 26.2, so this goes
	/// through ItemStack.CODEC with a registry-aware ops.
	public static ListTag writeItemList(List<ItemStack> stacks, HolderLookup.Provider registries) {
		RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
		ListTag tag = new ListTag();
		for (ItemStack stack : stacks) {
			if (!stack.isEmpty()) {
				ItemStack.CODEC.encodeStart(ops, stack).result().ifPresent(tag::add);
			}
		}
		return tag;
	}

	/// Reads a list written by [#writeItemList], dropping entries that no longer parse.
	public static List<ItemStack> readItemList(ListTag tag, HolderLookup.Provider registries) {
		RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
		List<ItemStack> stacks = new ArrayList<>();
		for (Tag entry : tag) {
			ItemStack.CODEC.parse(ops, entry).result().ifPresent(stacks::add);
		}
		return stacks;
	}
}
