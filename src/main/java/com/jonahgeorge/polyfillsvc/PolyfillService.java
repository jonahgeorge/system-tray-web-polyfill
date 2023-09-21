package com.jonahgeorge.polyfillsvc;

import org.apache.pdfbox.io.RandomAccessReadBufferedFile;

import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.PopupMenu;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.Arrays;

class PolyfillService {
    public static void main(String[] args) throws java.awt.AWTException, java.lang.ClassNotFoundException,
            java.lang.InstantiationException, java.lang.IllegalAccessException, java.io.IOException,
            javax.swing.UnsupportedLookAndFeelException {
        // Disable logging
        Logger.getLogger("org.apache.pdfbox").setLevel(java.util.logging.Level.OFF);
        Logger.getLogger("org.apache.fontbox").setLevel(java.util.logging.Level.OFF);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        var pathToFile = new File("icon.png");
        var image = ImageIO.read(pathToFile);

        var trayIcon = new TrayIcon(image, "tray icon");

        var tray = SystemTray.getSystemTray();
        var popup = new PopupMenu();

        var quitItem = new MenuItem("Quit Funcard Helper");
        quitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });

        var testPrintItem = new MenuItem("Test Print");
        testPrintItem.addActionListener(e -> {
            try {
                var printService = Printers.findPrintService("HP LaserJet M110w (642118)");
                var file = new RandomAccessReadBufferedFile("./example.pdf");
                Printers.print(printService, file);
            } catch (IOException | PrinterException ex) {
                // TODO
            }
        });

        var printersItem = new MenuItem("List Printers");
        printersItem.addActionListener(e -> {
            var printerNames = Arrays.stream(Printers.getPrintServices()).map(PrintService::getName).toArray(String[]::new);
            var message = String.join(",", printerNames);
            JOptionPane.showMessageDialog(null, message);
        });

        var aboutItem = new MenuItem("About Funcard Helper");
        aboutItem.addActionListener(e -> {
            // var message = PolyfillService.getPrintServices().joinToString { it.name };
            // JOptionPane.showMessageDialog(null, message);
        });

        var runningItem = new MenuItem("Running on port XXX...");
        runningItem.setEnabled(false);

        popup.add(runningItem);
        popup.addSeparator();
        popup.add(printersItem);
        popup.add(testPrintItem);
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(quitItem);

        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);
    }
}
