import java.util.ArrayList;


/**
 * <p>Presentations keeps track of the slides in a presentation.</p>
 * <p>Only one instance of this class is available.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Presentation {
	private String currentTitle; //The title of the presentation
	private ArrayList<Slide> slides = new ArrayList<>(); //An ArrayList with slides
	private int currentSlideNumber; //The number of the current slide

	public Presentation() {
	}

	public int getSize() {
		return this.slides.size();
	}

	public String getTitle() {
		return this.currentTitle;
	}

	public void setTitle(String newTitle) {
		this.currentTitle = newTitle;
	}

	//Returns the current slide number
	public int getSlideNumber() {
		return this.currentSlideNumber;
	}

	//Change the current slide number and report it in the window
	public void setSlideNumber(int number) {
		if (number >= 0 && number < this.slides.size()) {
			currentSlideNumber = number;
		}
	}

	public ArrayList<Slide> getSlideList(){
		return slides;
	}

	//Add a slide to the presentation
	public void append(Slide slide) {
		this.slides.add(slide);
	}

	//Return a slide with a specific number
	public Slide getSlide(int number) {
		if (number < 0 || number >= this.getSize()){
			return null;
	    }
		return this.slides.get(number);
	}

	//Return the current slide
	public Slide getCurrentSlide() {
		return getSlide(this.currentSlideNumber);
	}

	public void exit(int n) {
		System.exit(n);
	}
}
