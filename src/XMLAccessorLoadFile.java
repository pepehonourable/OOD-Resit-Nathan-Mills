import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Constants.XMLTags;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * XMLAccessorLoadFile reads XML files to load presentation data.
 *
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version 1.6 2014/05/16
 */
public class XMLAccessorLoadFile implements AccessorLoadFile {

    private static final String SHOWTITLE = "showtitle";
    private static final String SLIDE = "slide";
    private static final String SLIDETITLE = "title";
    private static final String ITEM = "item";
    private static final String LEVEL = "level";
    private static final String KIND = "kind";
    private static final String TEXT = "text";
    private static final String IMAGE = "image";

    private String getTitle(Element element, String tagName) {
        NodeList titles = element.getElementsByTagName(tagName);
        return titles.item(0).getTextContent();
    }

    public void loadFile(Presentation presentation, String filename) throws IOException, SAXException, ParserConfigurationException {
        try {
            Document document = parseXmlFile(filename);
            Element doc = document.getDocumentElement();
            presentation.setTitle(getTitle(doc, SHOWTITLE));

            NodeList slides = doc.getElementsByTagName(SLIDE);
            int maxSlides = slides.getLength();
            for (int slideNumber = 0; slideNumber < maxSlides; slideNumber++) {
                Element xmlSlide = (Element) slides.item(slideNumber);
                Slide slide = new Slide();
                slide.setTitle(getTitle(xmlSlide, SLIDETITLE));
                presentation.append(slide);

                NodeList slideItems = xmlSlide.getElementsByTagName(ITEM);
                int maxItems = slideItems.getLength();
                for (int itemNumber = 0; itemNumber < maxItems; itemNumber++) {
                    Element item = (Element) slideItems.item(itemNumber);
                    loadSlideItem(slide, item);
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException exception) {
            throw exception;
        }
    }

    private Document parseXmlFile(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(new File(filename));
    }

    protected void loadSlideItem(Slide slide, Element item) {
        NamedNodeMap attributes = item.getAttributes();
        int level = parseLevelAttribute(attributes.getNamedItem(LEVEL));
        String type = attributes.getNamedItem(KIND).getTextContent();

        if (TEXT.equals(type)) {
            slide.append(new TextItem(level, item.getTextContent()));
        } else if (IMAGE.equals(type)) {
            slide.append(new BitmapItem(level, item.getTextContent()));
        } else {
            System.err.println(XMLTags.UNKNOWNTYPE);
        }
    }

    private int parseLevelAttribute(org.w3c.dom.Node levelNode) {
        int level = 1; // default
        if (levelNode != null) {
            try {
                level = Integer.parseInt(levelNode.getTextContent());
            } catch (NumberFormatException e) {
                System.err.println(XMLTags.NFE);
            }
        }
        return level;
    }
}
