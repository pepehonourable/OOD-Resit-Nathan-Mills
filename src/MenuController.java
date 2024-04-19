import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import Constants.MenuControlButtons;
import org.xml.sax.SAXException;

public class MenuController extends MenuBar {

	private final Frame parentFrame;
	private final SlideViewerComponent slideViewerComponent;
	private final Menu fileMenu;
	private final Menu viewMenu;
	private final Menu helpMenu;

	public MenuController(Frame frame, SlideViewerComponent slideViewerComponent) {
		this.parentFrame = frame;
		this.slideViewerComponent = slideViewerComponent;
		this.fileMenu = createFileMenu();
		this.viewMenu = createViewMenu();
		this.helpMenu = createHelpMenu();

		addMenus();
	}

	private Menu createFileMenu() {
		Menu menu = new Menu(MenuControlButtons.FILE);
		menu.add(createMenuItem(MenuControlButtons.NEW, e -> { slideViewerComponent.clear(); parentFrame.repaint(); }));
		menu.add(createMenuItem(MenuControlButtons.SAVE, e -> savePresentation()));
		menu.add(createMenuItem(MenuControlButtons.OPEN, e -> loadPresentation()));
		menu.add(createMenuItem(MenuControlButtons.EXIT, e -> System.exit(0)));
		return menu;
	}

	private Menu createViewMenu() {
		Menu menu = new Menu(MenuControlButtons.VIEW);
		menu.add(createMenuItem(MenuControlButtons.NEXT, e -> slideViewerComponent.nextSlide()));
		menu.add(createMenuItem(MenuControlButtons.PREV, e -> slideViewerComponent.prevSlide()));
		menu.add(createMenuItem(MenuControlButtons.GOTO, e -> goToSlide()));
		return menu;
	}

	private Menu createHelpMenu() {
		Menu menu = new Menu(MenuControlButtons.HELP);
		menu.add(createMenuItem(MenuControlButtons.ABOUT, e -> showAboutBox()));
		return menu;
	}

	private void addMenus() {
		add(fileMenu);
		add(viewMenu);
		add(helpMenu);
	}

	private MenuItem createMenuItem(String label, ActionListener actionListener) {
		MenuItem item = new MenuItem(label, new MenuShortcut(label.charAt(0)));
		item.addActionListener(actionListener);
		return item;
	}

	private void savePresentation() {
		XMLAccessorSaveFile xmlAccessor = new XMLAccessorSaveFile();
		try {
			xmlAccessor.saveFile(slideViewerComponent.getPresentation(), MenuControlButtons.SAVEFILE);
		} catch (IOException exc) {
			showErrorDialog(MenuControlButtons.SAVEERR, MenuControlButtons.IOEX + exc);
		}
	}

	private void loadPresentation() {
		slideViewerComponent.clear();
		XMLAccessorLoadFile xmlAccessor = new XMLAccessorLoadFile();
		try {
			xmlAccessor.loadFile(slideViewerComponent.getPresentation(), MenuControlButtons.TESTFILE);
			slideViewerComponent.setSlideNumber(0);
		} catch (IOException exc) {
			showErrorDialog(MenuControlButtons.LOADERR, MenuControlButtons.IOEX + exc);
		} catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        parentFrame.repaint();
	}

	private void goToSlide() {
		String pageNumber = JOptionPane.showInputDialog(MenuControlButtons.PAGENR);
		try {
			int pageNumberParsed = Integer.parseInt(pageNumber);
			if (pageNumberParsed <= slideViewerComponent.getPresentation().getSize()) {
				slideViewerComponent.setSlideNumber(pageNumberParsed - 1);
			} else {
				showErrorDialog("Error","Number must be within bounds");
			}
		} catch (NumberFormatException ignored) {
			showErrorDialog("Error","Only Numbers Allowed for Page Numbers");
		}
	}

	private void showAboutBox() {
		AboutBox.show(parentFrame);
	}

	private void showErrorDialog(String title, String message) {
		JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
