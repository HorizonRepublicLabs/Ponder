package net.createmod.catnip.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.createmod.catnip.codecs.CatnipCodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

// TODO - Everything here needs to be rethought with how codecs exist now and should be used everywhere they can
@Deprecated(forRemoval = true)
public class NBTHelper {
	public static void putMarker(CompoundTag nbt, String marker) {
		nbt.putBoolean(marker, true);
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

	public static ListTag writeItemList(Iterable<ItemStack> stacks, HolderLookup.Provider registries) {
		ListTag listNBT = new ListTag();
		for (ItemStack stack : stacks)
			CatnipCodecUtils.encode(ItemStack.CODEC, registries, stack).ifPresent(listNBT::add);
		return listNBT;
	}

	public static List<ItemStack> readItemList(ListTag stacks, HolderLookup.Provider registries) {
		List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < stacks.size(); i++)
			CatnipCodecUtils.decode(ItemStack.CODEC, registries, stacks.getCompoundOrEmpty(i)).ifPresent(list::add);
		return list;
	}

	public static ListTag writeAABB(AABB bb) {
		ListTag bbtag = new ListTag();
		bbtag.add(FloatTag.valueOf((float) bb.minX));
		bbtag.add(FloatTag.valueOf((float) bb.minY));
		bbtag.add(FloatTag.valueOf((float) bb.minZ));
		bbtag.add(FloatTag.valueOf((float) bb.maxX));
		bbtag.add(FloatTag.valueOf((float) bb.maxY));
		bbtag.add(FloatTag.valueOf((float) bb.maxZ));
		return bbtag;
	}

	@Nullable
	public static AABB readAABB(ListTag bbTag) {
		if (bbTag.isEmpty())
			return null;
		return new AABB(
			bbTag.getFloat(0).orElseThrow(),
			bbTag.getFloat(1).orElseThrow(),
			bbTag.getFloat(2).orElseThrow(),
			bbTag.getFloat(3).orElseThrow(),
			bbTag.getFloat(4).orElseThrow(),
			bbTag.getFloat(5).orElseThrow()
		);
	}

	public static ListTag writeVec3i(Vec3i vec) {
		ListTag tag = new ListTag();
		tag.add(IntTag.valueOf(vec.getX()));
		tag.add(IntTag.valueOf(vec.getY()));
		tag.add(IntTag.valueOf(vec.getZ()));
		return tag;
	}

	public static Vec3i readVec3i(ListTag tag) {
		return new Vec3i(
			tag.getInt(0).orElseThrow(),
			tag.getInt(1).orElseThrow(),
			tag.getInt(2).orElseThrow()
		);
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
}
