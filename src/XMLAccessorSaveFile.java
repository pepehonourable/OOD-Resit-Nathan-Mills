import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * XMLAccessorSaveFile saves presentation data to an XML file.
 *
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version 1.6 2014/05/16
 */
public class XMLAccessorSaveFile implements AccessorSaveFile {

    private static final String XML_VERSION = "<?xml version=\"1.0\"?>";
    private static final String DOCTYPE = "<!DOCTYPE presentation SYSTEM \"jabberpoint.dtd\">";
    private static final String OPEN_PRESENTATION_TAG = "<presentation>";
    private static final String CLOSE_PRESENTATION_TAG = "</presentation>";
    private static final String SHOW_TITLE_TAG = "<showtitle>";
    private static final String CLOSE_SHOW_TITLE_TAG = "</showtitle>";
    private static final String SLIDE_TAG = "<slide>";
    private static final String CLOSE_SLIDE_TAG = "</slide>";
    private static final String TITLE_TAG = "<title>";
    private static final String CLOSE_TITLE_TAG = "</title>";
    private static final String ITEM_TAG_OPEN = "<item kind=\"";
    private static final String ITEM_LEVEL_ATTR = " level=\"";
    private static final String ITEM_TAG_CLOSE = "\">";
    private static final String CLOSE_ITEM_TAG = "</item>";

    public void saveFile(Presentation presentation, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            writeHeader(out);
            writePresentationContent(presentation, out);
            writeFooter(out);
        }
    }

    private void writeHeader(PrintWriter out) {
        out.println(XML_VERSION);
        out.println(DOCTYPE);
        out.println(OPEN_PRESENTATION_TAG);
    }

    private void writePresentationContent(Presentation presentation, PrintWriter out) {
        out.print(SHOW_TITLE_TAG);
        out.print(presentation.getTitle());
        out.println(CLOSE_SHOW_TITLE_TAG);

        for (Slide slide : presentation.getSlideList()) {
            writeSlide(slide, out);
        }
    }

    private void writeSlide(Slide slide, PrintWriter out) {
        out.println(SLIDE_TAG);
        out.print(TITLE_TAG);
        out.print(slide.getTitle());
        out.println(CLOSE_TITLE_TAG);

        for (SlideItem slideItem : slide.getSlideItems()) {
            writeSlideItem(slideItem, out);
        }

        out.println(CLOSE_SLIDE_TAG);
    }

    private void writeSlideItem(SlideItem slideItem, PrintWriter out) {
        out.print(ITEM_TAG_OPEN);
        out.print(getItemType(slideItem));
        out.print(ITEM_LEVEL_ATTR);
        out.print(slideItem.getLevel());
        out.print(ITEM_TAG_CLOSE);

        if (slideItem instanceof TextItem) {
            out.print(((TextItem) slideItem).getText());
        } else if (slideItem instanceof BitmapItem) {
            out.print(((BitmapItem) slideItem).getName());
        }

        out.println(CLOSE_ITEM_TAG);
    }

    private String getItemType(SlideItem slideItem) {
        if (slideItem instanceof TextItem) {
            return "text";
        } else if (slideItem instanceof BitmapItem) {
            return "image";
        }
        return "unknown";
    }

    private void writeFooter(PrintWriter out) {
        out.println(CLOSE_PRESENTATION_TAG);
    }
}
