/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF.Potato;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class parse the conversion table from PGSC GeneID to PGSC Peptide ID,
 * table provided by the guys from MSU.
 *
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Po_ParseGeneToProtein {

    /**
     * Logger used for outputing log information.
     */
    private static final Logger LOG = Logger.getLogger(
            Po_ParseGeneDescription.class.getName());

    /**
     * Default constructor.
     */
    public Po_ParseGeneToProtein() {
    }

    /**
     * Method parsing the provided input file which should be
     * "PGSC_DM_v3.4_g2t2c2p2func.txt" to extract a conversion table from PGSC
     * peptide identifier to PGSC gene identifier.
     *
     * @param inputfilename a String of the location of the input file to read.
     * @return a HashMap of String:String containing as key PGSC peptide
     * identifier and for value PGSC gene identifier.
     * @throws IOException This is throws when something goes wrong while
     * reading the file.
     */
    public HashMap<String, String> parseGeneToProtein(String inputfilename)
            throws IOException {
        System.out.println("Parsing: " + inputfilename);


        HashMap<String, String> conversion_table = new HashMap<String, String>();

        int cnt = 0;
        int genecnt = 0;
        String strline = "";
        final FileInputStream fstream = new FileInputStream(inputfilename);
        // Get the object of DataInputStream
        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(in));
        String geneid = null;
        String peptideid = null;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.trim();
            String[] content = strline.split("\t");
            if (content.length > 1 && cnt > 1) {
                geneid = content[0];
                peptideid = content[3];
                conversion_table.put(peptideid, geneid);
                genecnt = genecnt + 1;
            }
            cnt = cnt + 1;
        }
        in.close();

        if (geneid != null && peptideid != null) {
            conversion_table.put(geneid, peptideid);
        }

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, genecnt + " genes found");
        LOG.log(Level.FINE, "Conversion found : " + conversion_table.size());

        return conversion_table;
    }
}
