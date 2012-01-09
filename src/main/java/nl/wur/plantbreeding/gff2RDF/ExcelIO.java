/*
 * Copyright (c) 2012, Pierre-Yves Chibon
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the Wageningen University nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ''AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package nl.wur.plantbreeding.gff2RDF;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * This class handles the reading of excel files.
 *
 * This main function in this class is the function convertToCsv which goal is
 * to convert a given excel file into a csv file.
 *
 * @author Pierre-Yves Chibon -- py@chibon.fr
 * @author Richard Finkers
 */
public class ExcelIO {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(ExcelIO.class.getName());

    /**
     * Function to convert a specified excel file into a CSV (comma separated
     * value) file.
     * @param inputfile a String representing the name of the file
     * @param sheetname a String representing the name of the sheet in the file
     * @param outputfile a String reprensenting the name of the CSV file which
     * is outputed
     * @throws IOException if something goes wrong while reading/writing files
     * @throws InvalidFormatException if the system doesn't support UTF-8 format
     */
    public static void convertToCsv(String inputfile, String sheetname,
            String outputfile) throws IOException, InvalidFormatException {
        InputStream inp = null;
        inp = new FileInputStream(inputfile);
        Workbook wb;
        wb = WorkbookFactory.create(inp);
        wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        Sheet sheet1 = wb.getSheet(sheetname);
        ExcelIO.convertToCSV(sheet1, outputfile);
        inp.close();
    }

    /**
     * This is the function which actually does the job of reading an excel
     * sheet and outputing a CSV file.
     * @param sheet the Sheet you want to convert.
     * @param outputfile a String reprensenting the name of the CSV file which
     * is outputed
     * @throws IOException if something goes wrong while reading/writing files
     */
    private static void convertToCSV(Sheet sheet, String outputfile)
        throws IOException {
        System.out.println("Converting to CSV sheet: " + sheet.getSheetName());

        FileOutputStream fos = new FileOutputStream(outputfile);
        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

        for (Row row : sheet) {
            String rowout = "";
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                String cellcontent = "";
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        cellcontent = cell.getRichStringCellValue().getString();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellcontent = Double.toString(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        cellcontent = cell.getCellFormula();
                        break;
                    default:
                        LOG.log(Level.FINE, "No type found for cell: {0}",
                                cellRef);
                }
                if (rowout.isEmpty() || rowout.equals("")) {
                    rowout = cellcontent;
                } else {
                    rowout = rowout + "," + cellcontent;
                }
            }
            out.write(rowout + "\n");
        }
        out.close();
    }
}
