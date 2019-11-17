/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Utilidades;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 *
 * @author anton
 */
public class QRGenerator {

    public File generateQRCodeImage(String uuid)
            throws WriterException, IOException {
        File tempFile = File.createTempFile(uuid.toString(), ".jpg");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(uuid.toString(), BarcodeFormat.QR_CODE, 350, 350);

        Path path = FileSystems.getDefault().getPath(tempFile.getAbsolutePath());
        MatrixToImageWriter.writeToPath(bitMatrix, "JPG", path);
        
        return tempFile;
    }
}
