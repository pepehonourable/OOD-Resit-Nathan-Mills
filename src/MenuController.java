import Constants.MenuControlButtons;

import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.io.IOException;

import javax.swing.JOptionPane;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends MenuBar implements MenuControlButtons{

	private final Frame parentFrame; //The frame, only used as parent for the Dialogs
	private final SlideViewerComponent slideViewerComponent;
	private MenuItem menuItem;
	private final Menu fileMenu = new Menu(FILE);
	private final Menu viewMenu = new Menu(VIEW);
	private final Menu helpMenu = new Menu(HELP);

	public MenuController(Frame frame, SlideViewerComponent slideViewerComponent) {
		this.parentFrame = frame;
		this.slideViewerComponent = slideViewerComponent;
		add(fileMenu);
		newPresentation();
		savePresentation();
		exitPresentation();
		openPresentation();
		nextSlide();
		previousSlide();
		moveToSlide();
		help();
		setHelpMenu(helpMenu);
	}

	private void newPresentation() {
		fileMenu.add(menuItem = getNewMenuItem(NEW));
		menuItem.addActionListener(actionEvent -> {
			slideViewerComponent.clear();
			parentFrame.repaint();
		});
	}

	private void savePresentation() {
		fileMenu.add(menuItem = getNewMenuItem(SAVE));
		menuItem.addActionListener(e  ->  {
			XMLAccessorSaveFile xmlAccessor = new XMLAccessorSaveFile();
			try {
				xmlAccessor.saveFile(this.slideViewerComponent.getPresentation(), SAVEFILE);
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(parentFrame, IOEX + exc,
						SAVEERR, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void exitPresentation() {
		fileMenu.addSeparator();
		fileMenu.add(menuItem = getNewMenuItem(EXIT));
		menuItem.addActionListener(actionEvent -> {
			System.exit(0);
			add(fileMenu);
		});
	}
	private void openPresentation() {
		fileMenu.add(menuItem = getNewMenuItem(OPEN));
		menuItem.addActionListener(actionEvent -> {
			slideViewerComponent.clear();
			XMLAccessorLoadFile xmlAccessor = new XMLAccessorLoadFile();
			try {
				xmlAccessor.loadFile(this.slideViewerComponent.getPresentation(), TESTFILE);
				slideViewerComponent.setSlideNumber(0);
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(parentFrame, IOEX + exc,
						LOADERR, JOptionPane.ERROR_MESSAGE);
			}
			parentFrame.repaint();
		});
	}

	public void nextSlide() {
		viewMenu.add(menuItem = getNewMenuItem(NEXT));
		menuItem.addActionListener(actionEvent -> this.slideViewerComponent.nextSlide());
	}

	public void previousSlide() {
		viewMenu.add(menuItem = getNewMenuItem(PREV));
		menuItem.addActionListener(actionEvent -> this.slideViewerComponent.prevSlide());
	}

	public void moveToSlide() {
		viewMenu.add(menuItem = getNewMenuItem(GOTO));
		menuItem.addActionListener(actionEvent -> {
			String pageNumberStr = JOptionPane.showInputDialog(PAGENR);
			int pageNumber = Integer.parseInt(pageNumberStr);
			if(pageNumber < this.slideViewerComponent.getPresentation().getSize() + 1){
				this.slideViewerComponent.setSlideNumber(pageNumber - 1);
			}
		});
	}

	public void help() {
		add(viewMenu);
		Menu helpMenu = new Menu(HELP);
		helpMenu.add(menuItem = getNewMenuItem(ABOUT));
		menuItem.addActionListener(actionEvent -> AboutBox.show(parentFrame));
	}

//Creating a menu-item
	public MenuItem getNewMenuItem(String name) {
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}
}

