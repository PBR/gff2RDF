package nl.wur.plantbreeding.gff2rdf2;

import com.hp.hpl.jena.iri.impl.Main;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This gives IO support for Jena Model (semantic graphes).
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class ModelIO {

    /** Logger used for outputing log information. */
    private final Logger log = Logger.getLogger(Main.class.getName());


    /**
     *
     * RDF Reading
     *
     */

    /**
     * Reads the RDF from a given file and returns the associated model.
     * @param filename the name of the file to read
     * @return a Jena model
     */
    public final Model readRdf(final String filename) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(filename);
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + filename + " not found");
        }

        // read the RDF/XML file
        model.read(in, null);

        return model;

    }


    /**
     *
     * Write Model (to stdout or file)
     *
     */

    /**
     * Print in standard output the RDF representation of the model.
     * @param model the Jena model to output
     */
    public final void displayModel(final Model model) {
        // now write the model in XML form to a file
        model.write(System.out);
    }

    /**
     * Write to a file with the given filename the RDF representation of the
     * model.
     * @param model the Jena model to output
     * @param filename the name of the file to print
     * @throws IOException when something goes wrong while outputing the model
     */
    public final void printModelToFile(final Model model, final String filename)
            throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        // now write the model in XML form to a file
        model.write(out);
        out.close();
        log.log(Level.INFO, "Write model in RDF in: {0}", filename);
    }

    /**
     *Print the model in N3 format.
     * @param filename the path to the file in which to write the model
     * @param model the Jena model to output
     * @throws IOException when something goes wrong while outputing the model
     */
    public final void printModelNtripleToFile(final Model model,
            final String filename)
            throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        // now write the model in N3 form to a file
        model.write(out, "N-TRIPLE");
        out.close();
        log.log(Level.INFO, "Write model in N3 in: {0}", filename);
    }

    /**
     * Print in standard output the N3 representation of the model.
     * @param model the Jena model to output
     * @throws IOException when something goes wrong while outputing the model
     */
    public final void displayModelN3(final Model model) throws IOException {
        model.write(System.out);
    }
}
