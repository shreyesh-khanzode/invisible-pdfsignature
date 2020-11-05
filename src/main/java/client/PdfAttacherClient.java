package client;

import pdfsignature.PdfSignatureAttacher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PdfAttacherClient {
    public static void main(String[] args) {
        try {
            PdfSignatureAttacher attacher = new PdfSignatureAttacher(args[0]);
            String digest = attacher.generateDigestForExternalSigning();
            // now send this digest to the signing solution running elsewhere and get the signed data.
            // for now I'm expecting the signed data to be inputted on the System.in
            Scanner scanner = new Scanner(System.in);
            String signatureStr = scanner.nextLine();
            //set the signature in attacher object
            attacher.setSignature(signatureStr);
            // command the attacher to sign the pdf.
            attacher.signPdf();
            // you should get the pdf signed at temporary location
            // in windows: %USER%\APPDATA\LOCAL\TEMP
            // in linux: /tmp
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
