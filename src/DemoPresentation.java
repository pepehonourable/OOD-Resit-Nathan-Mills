/**
 * A built-in demo presentation.
 *
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
class DemoPresentation implements AccessorLoadFile {
	// Constants for slide titles
	private static final String TITLE_DEMO = "Demo Presentation";
	private static final String TITLE_LEVELS = "Demonstration of levels and styles";
	private static final String TITLE_THIRD_SLIDE = "The third slide";

	@Override
	public void loadFile(Presentation presentation, String unusedFileName) {
		Slide slide1 = createFirstSlide();
		presentation.append(slide1);

		Slide slide2 = createSecondSlide();
		presentation.append(slide2);

		Slide slide3 = createThirdSlide();
		presentation.append(slide3);
	}

	private Slide createFirstSlide() {
		Slide slide = new Slide();
		slide.setTitle(TITLE_DEMO);
		slide.append(1, "The Java presentation tool");
		slide.append(2, "Copyright (c) 1996-2000: Ian Darwin");
		slide.append(2, "Copyright (c) 2000-now:");
		slide.append(2, "Gert Florijn and Sylvia Stuurman");
		slide.append(4, "Calling Jabberpoint without a filename");
		slide.append(4, "will show this presentation");
		slide.append(1, "Navigate:");
		slide.append(3, "Next slide: PgDn or Enter");
		slide.append(3, "Previous slide: PgUp or up-arrow");
		slide.append(3, "Quit: q or Q");
		return slide;
	}

	private Slide createSecondSlide() {
		Slide slide = new Slide();
		slide.setTitle(TITLE_LEVELS);
		slide.append(1, "Level 1");
		slide.append(2, "Level 2");
		slide.append(1, "Again level 1");
		slide.append(1, "Level 1 has style number 1");
		slide.append(2, "Level 2 has style number 2");
		slide.append(3, "This is how level 3 looks like");
		slide.append(4, "And this is level 4");
		return slide;
	}

	private Slide createThirdSlide() {
		Slide slide = new Slide();
		slide.setTitle(TITLE_THIRD_SLIDE);
		slide.append(1, "To open a new presentation,");
		slide.append(2, "use File->Open from the menu.");
		slide.append(1, " ");
		slide.append(1, "This is the end of the presentation.");
		slide.append(new BitmapItem(1, "JabberPoint.jpg"));
		return slide;
	}

	public void saveFile(Presentation presentation, String unusedFileName) {
		throw new IllegalStateException("Save As->Demo! called");
	}
}
