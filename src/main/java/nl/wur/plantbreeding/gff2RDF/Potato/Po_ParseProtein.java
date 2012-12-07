/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF.Potato;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.ObjectToModel;
import nl.wur.plantbreeding.gff2RDF.object.Gene;

/**
 * This class parses the file "PGSC_gene_UniRef.txt" containing the association
 * between PGSC genes and UniProt proteins.
 *
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Po_ParseProtein {

    /**
     * Logger used for outputing log information.
     */
    private static final Logger LOG = Logger.getLogger(
            Po_ParseGeneDescription.class.getName());

    /**
     * Default constructor.
     */
    public Po_ParseProtein() {
    }

    public Model addProteinToModel(String inputfilename, Model model)
            throws IOException {
        System.out.println("Parsing: " + inputfilename
                + " and adding information to a model of size " + model.size());

        ObjectToModel obj2m = new ObjectToModel();

        int cnt = 0;
        int genecnt = 0;
        String strline = "";
        final FileInputStream fstream = new FileInputStream(inputfilename);
        // Get the object of DataInputStream
        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(in));
        String geneid = null;
        String proteineid = null;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            if (cnt == 0) {
                cnt = cnt + 1;
                continue;
            }
            strline = strline.trim();
            String[] content = strline.split("\t");
            if (content.length > 1 && cnt > 1) {
                geneid = content[0];
                proteineid = content[3].split("UniRef100_")[1];
                if (geneid == null) {
                    continue;
                }
                // We only keep uniprot proteins, not UP one.
                if (proteineid.startsWith("UP")) {
                    continue;
                }
                model = obj2m.addProteinToModel(model, geneid, proteineid);
                genecnt = genecnt + 1;
            }
            cnt = cnt + 1;
        }
        in.close();

        if (geneid != null && proteineid != null) {
            if (!proteineid.startsWith("UP")) {
                model = obj2m.addProteinToModel(model, geneid, proteineid);
                genecnt = genecnt + 1;
            }
        }

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, genecnt + " genes found");
        LOG.log(Level.FINE, "Model has size: " + model.size());

        return model;

    }
}
