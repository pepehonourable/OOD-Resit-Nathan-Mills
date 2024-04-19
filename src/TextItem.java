import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.text.AttributedString;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/** <p>A text item.</p>
 * <p>A text item has drawing capabilities.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class TextItem extends SlideItem {

	private final String text;

	//A textitem of int level with text string
	public TextItem(int level, String string) {
		super(level);
		text = string;
	}

	//Returns the text
	public String getText() {
		return text == null ? "" : text;
	}

	public AttributedString getAttributedString(Style style, float scale) {
		AttributedString attrStr = new AttributedString(getText());
		attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, getText().length());
		return attrStr;
	}

	public Rectangle getBoundingBox(Graphics graphics, ImageObserver observer, float scale, Style style) {
		List<TextLayout> layouts = calculateLayouts(graphics, style, scale);
		return calculateBoundingBox(layouts, style, scale);
	}

	public void draw(int x, int y, float scale, Graphics graphics, Style style, ImageObserver observer) {
		List<TextLayout> layouts = calculateLayouts(graphics, style, scale);
		drawLayouts(x, y, scale, graphics, style, layouts);
	}

	private List<TextLayout> calculateLayouts(Graphics graphics, Style style, float scale) {
		List<TextLayout> layouts = new ArrayList<>();
		AttributedString attrStr = getAttributedString(style, scale);
		Graphics2D g2d = (Graphics2D) graphics;
		FontRenderContext frc = g2d.getFontRenderContext();
		LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
		float wrappingWidth = (Slide.WIDTH - style.indent) * scale;
		while (measurer.getPosition() < getText().length()) {
			TextLayout layout = measurer.nextLayout(wrappingWidth);
			layouts.add(layout);
		}
		return layouts;
	}

	private Rectangle calculateBoundingBox(List<TextLayout> layouts, Style style, float scale) {
		int xsize = 0;
		int ysize = (int) (style.leading * scale);
		for (TextLayout layout : layouts) {
			Rectangle2D bounds = layout.getBounds();
			xsize = Math.max(xsize, (int) bounds.getWidth());
			ysize += (int) (bounds.getHeight() + layout.getLeading() + layout.getDescent());
		}
		return new Rectangle((int) (style.indent * scale), 0, xsize, ysize);
	}

	private void drawLayouts(int x, int y, float scale, Graphics graphics, Style style, List<TextLayout> layouts) {
		if (text == null || text.isEmpty()) {
			return;
		}
		Point pen = new Point(x + (int) (style.indent * scale), y + (int) (style.leading * scale));
		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setColor(style.color);
		for (TextLayout layout : layouts) {
			pen.y += (int) layout.getAscent();
			layout.draw(g2d, pen.x, pen.y);
			pen.y += (int) layout.getDescent();
		}
	}

	public String toString() {
		return "TextItem[" + getLevel() + "," + getText() + "]";
	}
}