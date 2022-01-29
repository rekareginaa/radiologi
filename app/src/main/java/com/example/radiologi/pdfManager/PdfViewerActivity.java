package com.example.radiologi.pdfManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.radiologi.R;
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class PdfViewerActivity extends PDFViewerActivity implements PDFUtil.PDFUtilListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pdf Viewer");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.colorTransparentBlack)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pdf_viewer, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.menuPrintPdf: {
                File fileToPrint = getPdfFile();
                if (fileToPrint == null || !fileToPrint.exists()) {
                    Toast.makeText(this, R.string.text_generated_file_error, Toast.LENGTH_SHORT).show();
                    break;
                }

                PrintAttributes.Builder printAttributeBuilder = new PrintAttributes.Builder();
                printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
                printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

                PDFUtil.printPdf(PdfViewerActivity.this, fileToPrint, printAttributeBuilder.build());
                break;
            }
            case R.id.menuSharePdf: {
                File fileToShare = getPdfFile();
                if (fileToShare == null || !fileToShare.exists()) {
                    Toast.makeText(this, R.string.text_generated_file_error, Toast.LENGTH_SHORT).show();
                    break;
                }

                /*Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                Uri apkURI = FileProvider.getUriForFile(
                        getApplicationContext(),
                        getApplicationContext()
                                .getPackageName() + ".provider", fileToShare);
                intentShareFile.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(fileToShare.getName()));
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intentShareFile.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file://" + fileToShare.getAbsolutePath()));

                startActivity(Intent.createChooser(intentShareFile, "Share File"));*/

                Uri uri = FileProvider.getUriForFile(PdfViewerActivity.this,
                        "com.example.radiologi.fileprovider",
                        fileToShare);

                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, uri);
//                share.setPackage("com.whatsapp");
                startActivity(share);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void pdfGenerationSuccess(File savedPDFFile) {
        finish();
    }

    @Override
    public void pdfGenerationFailure(Exception exception) {
        Toast.makeText(this, "Failed To Generate PDF", Toast.LENGTH_SHORT).show();
    }
}
