import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import Constants.MenuControlButtons;
import org.xml.sax.SAXException;

public class MenuController extends MenuBar {

	private static final String ABOUT = "About";
	private static final String FILE = "File";
	private static final String EXIT = "Exit";
	private static final String GOTO = "Go to";
	private static final String HELP = "Help";
	private static final String NEW = "New";
	private static final String NEXT = "Next";
	private static final String OPEN = "Open";
	private static final String PAGENR = "Page number?";
	private static final String PREV = "Prev";
	private static final String SAVE = "Save";
	private static final String VIEW = "View";
	private static final String TESTFILE = "testPresentation.xml";
	private static final String SAVEFILE = "savedPresentation.xml";
	private static final String IOEX = "IO Exception: ";
	private static final String LOADERR = "Load Error";
	private static final String SAVEERR = "Save Error";
	
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
		Menu menu = new Menu(FILE);
		menu.add(createMenuItem(NEW, e -> { slideViewerComponent.clear(); parentFrame.repaint(); }));
		menu.add(createMenuItem(SAVE, e -> savePresentation()));
		menu.add(createMenuItem(OPEN, e -> loadPresentation()));
		menu.add(createMenuItem(EXIT, e -> System.exit(0)));
		return menu;
	}

	private Menu createViewMenu() {
		Menu menu = new Menu(VIEW);
		menu.add(createMenuItem(NEXT, e -> slideViewerComponent.nextSlide()));
		menu.add(createMenuItem(PREV, e -> slideViewerComponent.prevSlide()));
		menu.add(createMenuItem(GOTO, e -> goToSlide()));
		return menu;
	}

	private Menu createHelpMenu() {
		Menu menu = new Menu(HELP);
		menu.add(createMenuItem(ABOUT, e -> showAboutBox()));
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
			xmlAccessor.saveFile(slideViewerComponent.getPresentation(), SAVEFILE);
		} catch (IOException exc) {
			showErrorDialog(SAVEERR, IOEX + exc);
		}
	}

	private void loadPresentation() {
		slideViewerComponent.clear();
		XMLAccessorLoadFile xmlAccessor = new XMLAccessorLoadFile();
		try {
			xmlAccessor.loadFile(slideViewerComponent.getPresentation(), TESTFILE);
			slideViewerComponent.setSlideNumber(0);
		} catch (IOException exc) {
			showErrorDialog(LOADERR, IOEX + exc);
		} catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        parentFrame.repaint();
	}

	private void goToSlide() {
		String pageNumber = JOptionPane.showInputDialog(PAGENR);
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
