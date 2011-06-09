/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF.Arabidopsis;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.ObjectToModel;

/**
 * This class parses the Arabidopsis thaliana GO annotation.
 *
 * See ftp://ftp.arabidopsis.org/Ontologies/Gene_Ontology/ATH_GO.README.txt for
 * more information.
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_ParseGoGene {

    /** Logger used for outputing log information. */
    private final Logger log = Logger.getLogger(
            At_ParseGoGene.class.getName());

    /**
     * This method parse the tbl files from TAIR and add the information
     * retrieved about the genes into the model given as parameter.
     * The gene information retrieved by this method are:
     *   - Gene ID
     *   - Gene position on the genome (physical position):
     *       - Chromosome
     *       - Start position
     *       - Stop position
     *   - Gene description
     *   - GO terms associated with this gene
     *
     * @param inputfilename the path to the input file containing the gene
     * information
     * @param model a Jena model in which the gene information will be stored
     * @return a Jena model containing with its previous information the gene
     * information retrieved by this method.
     */
    public final Model getModelFromAthGo(final String inputfilename,
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
            At_Gene gene = null;
            //Read File Line By Line
            while ((strline = br.readLine()) != null) {
                strline = strline.trim();
                String[] content = strline.split("\t");
                if (content.length > 1 && content[0].startsWith("AT")) {
                    gene = new At_Gene();
                    gene.setLocus(content[0].trim());
                    gene.addGoTerm(content[5].trim());

                    // Add gene to model
                    model = obj2m.addToModel(gene, model);
                    genecnt = genecnt + 1;
                }
                cnt = cnt + 1;
            }
            in.close();

            if (gene != null) {
                // add gene to model here
                model = obj2m.addToModel(gene, model);
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
