package it.bestapp.paganino.utility;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import it.bestapp.paganino.utility.db.bin.BustaPaga;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExportXls {

    private WritableCellFormat timesBold;
    private WritableCellFormat times;


    private File file = null;
    private Workbook workbook;
    private WritableSheet getsht;

    public ExportXls(File f, Fragment frag) {
        this.file = f;
        File temp;
        InputStream tmpStream;
        FileOutputStream dest;

        AssetManager assetManager = frag.getActivity().getAssets();
        try {
            tmpStream = assetManager.open("excel/templeate.xls");
            int size = tmpStream.available();
            byte[] buffer = new byte[size];
            tmpStream.read(buffer);
            tmpStream.close();

            dest = new FileOutputStream(file);
            dest.write(buffer);
            dest.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("it", "IT"));
        try {
            workbook = Workbook.getWorkbook(file, wbSettings);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    public void create(BustaPaga bP) throws IOException, WriteException {
        WritableWorkbook mod = Workbook.createWorkbook(file, workbook);
        workbook.close();
        WritableSheet excelSheet = mod.getSheet(1);
        createContent(excelSheet, bP);
        mod.write();
        mod.close();
    }


    private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label = new Label(column, row, s, timesBold);
        sheet.addCell(label);
    }

    private void createContent(WritableSheet sheet, BustaPaga bP) throws WriteException, RowsExceededException {
        WritableFont timesFont = new WritableFont(WritableFont.TIMES, 12);
        times = new WritableCellFormat(timesFont);
        times.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setAutosize(true);

        //WritableCell cell = sheet2.getWritableCell(1, 2);


        Label lb;
        lb = new Label(0, 0, bP.getId(), times);
        sheet.addCell(lb);
        lb = new Label(0, 1, String.valueOf(bP.getPagaBase()), times);
        sheet.addCell(lb);
        lb = new Label(0, 2, String.valueOf(bP.getNetto()), times);
        sheet.addCell(lb);
        lb = new Label(0, 3, String.valueOf(bP.getTotRit()), times);
        sheet.addCell(lb);
        lb = new Label(0, 4, String.valueOf(bP.getPereq()), times);
        sheet.addCell(lb);
        lb = new Label(0, 5, String.valueOf(bP.getTrasf()), times);
        sheet.addCell(lb);


    }
}