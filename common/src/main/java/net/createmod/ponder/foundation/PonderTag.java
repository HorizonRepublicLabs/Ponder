package net.createmod.ponder.foundation;

import javax.annotation.Nullable;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.ponder.Ponder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

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
		PoseStack poseStack = graphics.pose();
		poseStack.pushPose();
		poseStack.translate(x, y, 0);
		if (textureIconLocation != null) {
			graphics.pose().pushMatrix();
			graphics.pose().scale(0.25f, 0.25f);
			graphics.blit(RenderPipelines.GUI_TEXTURED, textureIconLocation, 0, 0, 0, 0, 0, 64, 64, 64, 64);
			graphics.pose().popMatrix();
		} else if (!itemIcon.isEmpty()) {
			poseStack.translate(-2, -2, 0);
			poseStack.scale(1.25f, 1.25f, 1.25f);
			GuiGameElement.of(itemIcon)
				.render(graphics);
		}
		poseStack.popPose();
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
