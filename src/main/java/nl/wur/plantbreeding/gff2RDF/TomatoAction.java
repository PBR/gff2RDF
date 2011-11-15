/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2RDF;

import nl.wur.plantbreeding.gff2RDF.Tomato.To_ParseGeneInfo;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  * This is the action class which enables to download and export the genome
 * annotation from Tomato to RDF.
 *
 * The main function are the download and main functions.
 * The first one just download the files into the 'Tomato' directory from ITAG.
 * The second one reads them and export them into the Jena Model which is then
 * writen as RDF if not empty.
 *
 * @author Pierre-Yves Chibon --py@chibon.fr
 */
class TomatoAction {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            PotatoAction.class.getName());
    /** Folder in which the files are/will be stored. */
    private String folder = "Tomato/";

    /**
     * Default constructor.
     *
     * Creates the folder 'Tomato' if necessary.
     */
    public TomatoAction() {
        App.makeFolder(this.folder);
    }

    /**
     * Constructor.
     *
     * Creates a specific folder if asked.
     * @param newfolder String of the new folder to use.
     */
    public TomatoAction(String newfolder) {
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
     * This function downloads the file of the Potato genome annotation from
     * the PGSC.
     *
     * It downloads the release 3.4 of PGSC (latest available at the moment).
     * It downloads two files from PGSC:
     *  - PGSC_DM_v3.4_gene.gffcontains all the genes with their position on
     * the genome
     *  - PGSC_DM_v3.4_gene_func.txt contains the functional description for
     * all these genes
     *
     * @param force boolean, whether to force the download of the files or not.
     * @throws IOException if something goes wrong when trying to download the
     * files.
     */
    public void download(boolean force) throws IOException {
        HashMap<String, String> urls = new HashMap<String, String>();
        urls.put("ftp://ftp.solgenomics.net/tomato_genome/annotation/ITAG2_release/ITAG2_genomic_all.gff3",
                this.folder + "ITAG2_genomic_all.gff3");
        urls.put("ftp://ftp.solgenomics.net/tomato_genome/annotation/ITAG2_release/ITAG2_protein_functional.gff3",
                this.folder + "ITAG2_protein_functional.gff3");

        Set<String> urlset = urls.keySet();
        int cnt = 0;
        for (Iterator<String> it = urlset.iterator(); it.hasNext();) {
            String key = it.next();
            cnt = cnt + 1;
            App.downaloadFile(key, urls.get(key), force);
        }
        System.out.println();
    }

    /**
     * This function reads the downloaded files and conver their content into
     * an RDF model which is stored in a Jena Model object.
     *
     * @param debug boolean to print the stack trace of the exceptions catched.
     */
    public void main(boolean debug) {
        Model model = ModelFactory.createDefaultModel();
        String inputfilename = "";

        try {
            // GFF file containing the gene information
            inputfilename = this.folder + "ITAG2_genomic_all.gff3";
            To_ParseGeneInfo parser = new To_ParseGeneInfo();
            model = parser.addGenesToModel(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        try {
            // GFF file containing the protein information
            inputfilename = this.folder + "ITAG2_protein_functional.gff3";
            To_ParseGeneInfo parser = new To_ParseGeneInfo();
            model = parser.addProteinsToModel(inputfilename, model);
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
                String outputfilename = this.folder + "genemodel.rdf";
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
