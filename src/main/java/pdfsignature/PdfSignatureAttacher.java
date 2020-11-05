package pdfsignature;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;

public class PdfSignatureAttacher {
    private byte[] pdfFile;
    private byte[] signature;
    private PDDocument pdDocument;
    ExternalSigningSupport externalSigningSupport;
    Calendar date = Calendar.getInstance();
    FileOutputStream fos = null;
    public PdfSignatureAttacher(String pdfFilePath) throws IOException {
        InputStream is = new FileInputStream(pdfFilePath);
        pdfFile = IOUtils.toByteArray(is);
    }

    public String generateDigestForExternalSigning() throws IOException, NoSuchAlgorithmException {
        File pdfFile = File.createTempFile("pdf", ".pdf");
        FileUtils.writeByteArrayToFile(pdfFile, this.pdfFile);
        File signedPdfFile = File.createTempFile("signed_pdf", ".pdf");
        try {
            fos = new FileOutputStream(signedPdfFile);
            pdDocument = PDDocument.load(pdfFile);
            PDSignature pdSignature = new PDSignature();
            pdSignature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            pdSignature.setName("ShreyeshTest");
            pdSignature.setReason("Test Signature");
            pdSignature.setSignDate(date);
            pdDocument.addSignature(pdSignature);
            externalSigningSupport = pdDocument.saveIncrementalForExternalSigning(fos);
            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(externalSigningSupport.getContent());
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(bytes);
            // convert the digested bytes as needed, either in HEX or Base64 as per the external signing solution requirement
            return DatatypeConverter.printHexBinary(digest);
        }
        finally {
            pdfFile.deleteOnExit();
        }
    }

    public void setSignature(String signature) {
        // I'm expecting a base64 encoded signature here.
        this.signature = Base64.getDecoder().decode(signature);
    }

    public void signPdf() throws IOException {
        // set the signature bytes
        externalSigningSupport.setSignature(signature);
    }
}
