/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.Map.Ma_ParserMapInfo;

/**
 * This is the action class which parses the given map file and transform it
 * to a RDF model.
 *
 * The main function is 'main' function.
 * Which reads the input file and convert its content in a Jena Model which is
 * then writen as RDF if not empty.
 *
 * @author Pierre-Yves Chibon --py@chibon.fr
 */
class MapAction {
     /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            MapAction.class.getName());
    /** Folder in which the files are/will be stored. */
    private String folder = "Map/";

    /**
     * Default constructor.
     *
     * Creates the folder 'Map' if necessary.
     */
    public MapAction() {
        App.makeFolder(this.folder);
    }

    /**
     * Constructor.
     *
     * Creates a specific folder if asked.
     * @param newfolder String of the new folder to use.
     */
    public MapAction(String newfolder) {
        App.makeFolder(newfolder);
        this.folder = newfolder;
    }

    /**
     * Set the folder in which the files will/are stored.
     * @param tmpfolder a String of the name of the folder.
     */
    public void setFolder(String tmpfolder) {
        this.folder = tmpfolder;
    }

    /**
     * This function reads the downloaded files and conver their content into
     * an RDF model which is stored in a Jena Model object.
     *
     * @param debug boolean to print the stack trace of the exceptions catched.
     */
    public void main(String inputfilename, boolean debug) {
        Model model = ModelFactory.createDefaultModel();

        try {
            // GFF file containing the gene information
            inputfilename = this.folder + "ITAG2_genomic_all.gff3";
            Ma_ParserMapInfo parser = new Ma_ParserMapInfo();
            model = parser.addMarkersToModel(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        System.out.println("Model final: " + model.size());
        try {
            if (!model.isEmpty()) {
                ModelIO mio = new ModelIO();
                String outputfilename = this.folder + "mapmodel.rdf";
                mio.printModelToFile(model, outputfilename);
            }
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "Error while writting: {0}", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }
    }

}
