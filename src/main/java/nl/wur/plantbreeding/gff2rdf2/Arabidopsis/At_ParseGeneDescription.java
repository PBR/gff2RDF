/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2rdf2.Arabidopsis;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
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
public class At_ParseGeneDescription {

    /** Logger used for outputing log information. */
    private final Logger log = Logger.getLogger(
            At_ParseGeneDescription.class.getName());

    /**
     * This method parse the TAIRx_functional_descriptions file and extract from
     * it the gene id (TAIR ID before the '.') and the function description.
     * The description is then added to the Jena Model.
     *
     * @param inputfilename the path to the input file containing the gene
     * functional description
     * @param model a Jena model in which the gene description will be stored
     * @return a Jena model containing with its previous information the gene
     * description retrieved by this method.
     */
    public final Model getModelFromTbl(final String inputfilename,
            Model model) {

        log.log(Level.INFO, "Parsing: {0} and adding information to a model "
                + "of size " + model.size(), inputfilename);

        ObjectToModel obj2m = new ObjectToModel();

        int cnt = 0;
        int genecnt = 0;
        String strline = "";
        try {
            final FileInputStream fstream = new FileInputStream(inputfilename);
            // Get the object of DataInputStream
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            ArrayList<String> genes = new ArrayList<String>();
            String geneid = null;
            String description = null;
            //Read File Line By Line
            while ((strline = br.readLine()) != null) {
                strline = strline.trim();
                String[] content = strline.split("\t");
                if (content.length > 1 && cnt > 1) {
                    geneid = content[0].split("\\.")[0];
                    if (!genes.contains(geneid)) {
                        genes.add(geneid);
                        if (content.length == 2) {
                            description = content[1];
                        } else if (content.length == 3) {
                            description = content[2];
                        } else {
                            description = content[3];
                            if (content[3] == null || content[3].isEmpty()) {
                                description = content[4];
                            }
                        }
                        // Add gene to model
                        model = obj2m.addGeneDescriptionToModel(geneid,
                                description, model);
                        genecnt = genecnt + 1;
                    }
                }
                cnt = cnt + 1;
            }
            in.close();

            if (geneid != null) {
                // add gene to model here
                model = obj2m.addGeneDescriptionToModel(geneid, description,
                        model);
            }

            log.log(Level.INFO, cnt + " lines read");
            log.log(Level.INFO, genecnt + " genes found");
            log.log(Level.INFO, "Model has size: " + model.size());
        } catch (Exception e) { //Catch exception if any
            log.log(Level.SEVERE, "Line: {0}", strline);
            log.log(Level.SEVERE, "Caught an exception: ", e);
        }


        return model;
    }
}
