package com.jonahgeorge.polyfillsvc;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterJob;
import java.util.Objects;

public class Printers {
    /**
     * Prints {file} at {printService}
     */
    public static void print(PrintService printService, RandomAccessReadBufferedFile file) throws java.io.IOException, java.awt.print.PrinterException {
        var document = Loader.loadPDF(file);
        var pageable = new PDFPageable(document);

        var job = PrinterJob.getPrinterJob();
        job.setPageable(pageable);
        job.setPrintService(printService);
        job.print();
    }

    /**
     * Returns all print services
     */
    public static PrintService[] getPrintServices() {
        return PrintServiceLookup.lookupPrintServices(null, null);
    }

    /**
     * Returns the PrintService matching name
     */
    public static PrintService findPrintService(String name) {
        for (var ps : getPrintServices()) {
            if (Objects.equals(ps.getName(), name)) {
                return ps;
            }
        }
        return null;
    }
}
