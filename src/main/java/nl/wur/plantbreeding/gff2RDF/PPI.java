/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.object.Gene;

/**
 *
 * @author pierrey
 */
public class PPI {

    /**
     * This URI is the based used to create the RDF of non-standard URI.
     * For example, gene will have a type: URI/GENE#
     * they will have a position: URI/POSITION# which will have a starting
     * position: URI/POSITION#Start.
     *
     * You may change it to fit your needs.
     */
    private String uri = "http://pbr.wur.nl/";
    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            ObjectToModel.class.getName());

    /**
     * Start-up function.
     * @param args command line argument given
     */
    public static void main(final String[] args) {
        String folder = "/home/pierrey/Documents/Projects/Marker2Sequence/";
        String filename = "intact.txt";

        Model model = ModelFactory.createDefaultModel();
        try {
            model = PPI.getModelFromGff(folder + filename, model);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        System.out.println("Model final: " + model.size());
        try {
            if (!model.isEmpty()) {
                ModelIO mio = new ModelIO();
                String outputfilename = "ppimodel.rdf";
                mio.printModelToFile(model, outputfilename);
            }
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "Error while writting: {0}", ex.getMessage());
                ex.printStackTrace(System.err);
        }
    }

    /**
     * Parse the tab delimited file and generate the model from it.
     * @param inputfilename the full path to the file.
     * @param model the Jena Model in which to add the info.
     * @return a Jena Model object containing the protein-protein interaction
     * information.
     * @throws IOException when something goes wrong while trying to read the
     * file.
     */
    public static Model getModelFromGff(final String inputfilename,
            Model model) throws IOException {

        System.out.println("Parsing: " + inputfilename
                + " and adding information to a model of size " + model.size());

        ObjectToModel obj2m = new ObjectToModel();

        int cnt = 0;
        int protcnt = 0;
        String strline = "";
        final FileInputStream fstream = new FileInputStream(inputfilename);
        // Get the object of DataInputStream
        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(in));
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.trim();
            String[] content = strline.split("\t");
            if (cnt > 1) {
                String prot1 = content[0].split("\\|")[0].split(":")[1];
                String prot2 = content[1].split("\\|")[0].split(":")[1];
                // Add ppi to model
                model = obj2m.addProteinProteinInteractionToModel(prot1, prot2,
                        model);
                protcnt = protcnt + 1;
            }
            cnt = cnt + 1;
        }
        in.close();

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, protcnt + " ppi found");
        LOG.log(Level.FINE, "Model has size: " + model.size());

        return model;
    }

}
