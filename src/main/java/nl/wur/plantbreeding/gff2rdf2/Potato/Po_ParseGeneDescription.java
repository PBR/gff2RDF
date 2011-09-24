/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2rdf2.Potato;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2rdf2.ObjectToModel;

/**
 * This class parses the functional description file and
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Po_ParseGeneDescription {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            Po_ParseGeneDescription.class.getName());

    /**
     * This method parse the file PGSC_DM_v*_gene_func.txt from PGSC
     * and extrac the gene description which is then added to the Jena Model.
     *
     * @param inputfilename the path to the input file containing the gene
     * functional description
     * @param model a Jena model in which the gene description will be stored
     * @return a Jena model containing with its previous information the gene
     * description retrieved by this method.
     * @throws IOException When something goes wrong with a file.
     */
    public final Model addGeneDescriptionToModel(final String inputfilename,
            Model model) throws IOException {

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
        String description = null;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.trim();
            String[] content = strline.split("\t");
            if (content.length > 1 && cnt > 1) {
                geneid = content[0];
                description = content[1];
                // Add gene to model
                model = obj2m.addGeneDescriptionToModel(geneid, description,
                        model);
                genecnt = genecnt + 1;
            }
            cnt = cnt + 1;
        }
        in.close();

        if (geneid != null) {
            // add gene to model here
            model = obj2m.addGeneDescriptionToModel(geneid, description,
                    model);
        }

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, genecnt + " genes found");
        LOG.log(Level.FINE, "Model has size: " + model.size());

        return model;
    }
}
