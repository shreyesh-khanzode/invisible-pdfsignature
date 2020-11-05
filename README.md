# invisible-pdfsignature

This gives a very easy solution for creating invisible external pdf signatures.
Usage is pretty simple.
- Create PdfSignatureAttacher object
```Java
PdfSignatureAttacher attacher = new PdfSignatureAttacher("path-to-pdf-file");
```
- Create Digest for the pdf
```Java
String digest = attacher.generateDigestForExternalSigning();
```
- Send the digest to the appropriate signing service and get the signature back (pkcs7)
- attach the signature the attacher
```Java
attacher.setSignature(signatureStr);
```
- Command the attacher to sign the pdf.
```Java
attacher.signPdf();
```
