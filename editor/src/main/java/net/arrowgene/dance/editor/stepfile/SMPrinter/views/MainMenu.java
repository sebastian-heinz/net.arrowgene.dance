package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Printer;

/**
 * The menu bar at the top of the application. handle all actions that are
 * invoked on the menu.
 *
 * @author Dan
 */

public class MainMenu extends JMenuBar implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final String OPEN_COMMAND = "openFile";
    private static final String PRINT_COMMAND = "print";
    private static final String ABOUT_COMMAND = "about";
    private static final String ZOOM_IN_COMMAND = "zoomIn";
    private static final String ZOOM_OUT_COMMAND = "zoomOut";
    private static final String HIDE_LEADING_AND_TRALING_MEASURES_COMMAND = "hideLeadingAndTrailingMeasures";

    protected MainFrame main;

    public MainMenu(MainFrame main) {
        this.main = main;

        add(createFileMenu());
        add(createViewMenu());
        add(createHelpMenu());
    }

    private JMenu createFileMenu() {
        JMenuItem menuItem;
        JMenu fileMenu = new JMenu("File");

        menuItem = new JMenuItem("Open File");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menuItem.setActionCommand(OPEN_COMMAND);
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Print");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menuItem.setActionCommand(PRINT_COMMAND);
        fileMenu.add(menuItem);

        return fileMenu;
    }

    private JMenu createViewMenu() {
        JMenuItem menuItem;
        JMenu viewMenu = new JMenu("View");

        menuItem = new JMenuItem("Zoom In");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menuItem.setActionCommand(ZOOM_IN_COMMAND);
        viewMenu.add(menuItem);

        menuItem = new JMenuItem("Zoom Out");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menuItem.setActionCommand(ZOOM_OUT_COMMAND);
        viewMenu.add(menuItem);

        menuItem = new JCheckBoxMenuItem("Hide Leading and Trailing Empty Measures", main.getSettings().hideLeadingAndTrailingWhiteSpace);
        menuItem.addActionListener(this);
        menuItem.setActionCommand(HIDE_LEADING_AND_TRALING_MEASURES_COMMAND);
        viewMenu.add(menuItem);

        return viewMenu;
    }

    private JMenu createHelpMenu() {
        JMenuItem menuItem;
        JMenu helpMenu = new JMenu("Help");

        menuItem = new JMenuItem("About");
        menuItem.addActionListener(this);
        menuItem.setActionCommand(ABOUT_COMMAND);
        helpMenu.add(menuItem);

        return helpMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (OPEN_COMMAND.equals(e.getActionCommand())) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int selectedOption = fc.showDialog(main, "Open File or Directory");
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                main.openFile(file);
            }
        } else if (PRINT_COMMAND.equals(e.getActionCommand())) {
            PrinterJob printJob = Printer.getPrinterJob(main.getSettings());

            boolean doPrint = printJob.printDialog();
            if (doPrint) {
                try {
                    printJob.print();
                } catch (PrinterException e1) {
                    e1.printStackTrace();
                }
            }

        } else if (ZOOM_IN_COMMAND.equals(e.getActionCommand())) {
            main.zoomIn();
        } else if (ZOOM_OUT_COMMAND.equals(e.getActionCommand())) {
            main.zoomOut();
        } else if (HIDE_LEADING_AND_TRALING_MEASURES_COMMAND.equals(e.getActionCommand())) {
            main.invertMeasureTrimming();
        } else if (ABOUT_COMMAND.equals(e.getActionCommand())) {
            JOptionPane.showMessageDialog(null, new AboutDialog(), "About", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private class AboutDialog extends JPanel {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private static final String text = "<html>SimFile Printer version 0.0.1 created May 2014. "
                + "Source code is available on <a href='https://github.com/UberZoonie/sm-printer'>github</a>."
                + "<p>SimFile Printer is distributed freely as open source software under the MIT license. "
                + "More information is available <a href='http://opensource.org/licenses/MIT'>here</a></html>";

        public AboutDialog() {
            JTextPane textPane = new JTextPane();
            textPane.setEditable(false);
            textPane.setBackground(new Color(238, 238, 238));
            textPane.setPreferredSize(new Dimension(350, 110));
            textPane.setContentType("text/html");
            textPane.setText(text);
            textPane.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            });

            add(textPane);
        }
    }
}
