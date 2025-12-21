package net.createmod.catnip.lang;

import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.Font.DisplayMode;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.catnip.platform.CatnipClientServices;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

import org.jspecify.annotations.Nullable;

public class ClientFontHelper {
	public static List<String> cutString(Font font, String text, int maxWidthPerLine) {
		// Split words
		List<String> words = new LinkedList<>();
		BreakIterator iterator = BreakIterator.getLineInstance(CatnipClientServices.CLIENT_HOOKS.getCurrentLocale());
		iterator.setText(text);
		int start = iterator.first();
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
			String word = text.substring(start, end);
			words.add(word);
		}
		// Apply hard wrap
		List<String> lines = new LinkedList<>();
		StringBuilder currentLine = new StringBuilder();
		int width = 0;
		for (String word : words) {
			int newWidth = font.width(word);
			if (width + newWidth > maxWidthPerLine) {
				if (width > 0) {
					String line = currentLine.toString();
					lines.add(line);
					currentLine = new StringBuilder();
					width = 0;
				} else {
					lines.add(word);
					continue;
				}
			}
			currentLine.append(word);
			width += newWidth;
		}
		if (width > 0) {
			lines.add(currentLine.toString());
		}
		return lines;
	}

	public static void drawSplitString(GuiGraphics graphics, Font font, String text, int x, int y, int width, int color) {
		List<String> list = cutString(font, text, width);

		for (String s : list) {
			int f = x;
			if (font.isBidirectional()) {
				int i = font.width(font.bidirectionalShaping(s));
				f += (width - i);
			}

			draw(graphics, font, s, f, y, color);
			y += 9;
		}
	}

	private static void draw(GuiGraphics graphics, Font font, @Nullable String text, int x, int y, int color) {
		if (text != null) {
			graphics.drawString(font, text, x, y, color, false);
		}
	}
}
