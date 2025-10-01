package net.createmod.catnip.registration.builder;

import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.holder.ItemHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Function;

public class ItemBuilder<T extends Item> extends AbstractBuilder<Item, T, ItemHolder<T>> {
	private final Function<Properties, T> itemFunc;
	private Function<Properties, Properties> properties = Function.identity();

	public ItemBuilder(CatnipRegistry owner, String name, Function<Properties, T> itemFunc) {
		super(owner, name, BuiltInRegistries.ITEM);
		this.itemFunc = itemFunc;
	}

	public ItemBuilder<T> properties(Function<Properties, Properties> func) {
		properties = properties.andThen(func);
		return this;
	}

	@Override
	T build() {
		Properties properties = new Item.Properties();
		properties = this.properties.apply(properties);
		return itemFunc.apply(properties);
	}

	@Override
	ItemHolder<T> getHolder(HolderOwner<Item> owner, ResourceKey<Item> key) {
		//noinspection unchecked
		return (ItemHolder<T>) new ItemHolder<>(owner, key);
	}
}
