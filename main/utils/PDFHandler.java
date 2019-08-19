package nl.essent.automation.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class PDFHandler {
    public String save_pdf_from_byte_array(byte[] pdf_source) throws Exception {
        String file_location = System.getProperty("user.dir") + "/src/test/resources/data/content/output.pdf";
        OutputStream out = new FileOutputStream(file_location);
        out.write(pdf_source);
        out.close();
        return file_location;
    }
}
