package net.createmod.ponder.foundation;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.ponder.Ponder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import org.joml.Matrix3x2fStack;

public class PonderTag implements ScreenElement {
	/**
	 * Highlight.ALL is a special PonderTag, used to indicate that all Tags
	 * for a certain Scene should be highlighted instead of selected single ones
	 */
	public static final class Highlight {
		public static final Identifier ALL = Ponder.id("_all");
	}

	private final Identifier id;
	@Nullable
	private final Identifier textureIconLocation;
	private final ItemStack itemIcon;
	private final ItemStack mainItem;


	public PonderTag(Identifier id, @Nullable Identifier textureIconLocation, ItemStack itemIcon,
					 ItemStack mainItem) {
		this.id = id;
		this.textureIconLocation = textureIconLocation;
		this.itemIcon = itemIcon;
		this.mainItem = mainItem;
	}

	public Identifier getId() {
		return id;
	}

	public ItemStack getMainItem() {
		return mainItem;
	}

	public String getTitle() {
		return PonderIndex.getLangAccess().getTagName(id);
	}

	public String getDescription() {
		return PonderIndex.getLangAccess().getTagDescription(id);
	}

	public void render(GuiGraphics graphics, int x, int y) {
		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();
		poseStack.translate(x, y);
		if (textureIconLocation != null) {
			poseStack.scale(0.25f, 0.25f);
			graphics.blit(RenderPipelines.GUI_TEXTURED, textureIconLocation, 0, 0, 0, 0, 0, 64, 64, 64, 64);
		} else if (!itemIcon.isEmpty()) {
			GuiGameElement.of(itemIcon)
				.scale(1.25f)
				.at(-2, -2)
				.render(graphics);
		}
		poseStack.popMatrix();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof PonderTag otherTag))
			return false;

		return getId().equals(otherTag.getId());
	}
}
