/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_GeneticMap;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGeneDescription;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGeneInfo;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGeneProtein;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGoGene;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_PhysicalMap;

/**
 * This is the action class which enables to download and export the genome
 * annotation from Arabidopsis to RDF.
 *
 * The main function are the download and main functions.
 * The first one just download the files into the 'At' directory from TAIR.
 * The second one reads them and export them into the Jena Model which is then
 * writen as RDF if not empty.
 *
 * @author Pierre-Yves Chibon --py@chibon.fr
 */
public class ArabidopsisAction {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            ArabidopsisAction.class.getName());

    /**
     * Default constructor.
     *
     * Creates the folder 'At' if necessary.
     */
    public ArabidopsisAction() {
        App.makeFolder("At");
    }

    /**
     * This function downloads the file of the Arabidopsis thaliana genome
     * annotation from TAIR.
     *
     * It downloads the release 10 of TAIR (latest available at the moment).
     * It downloads four files from TAIR:
     *  - TAIR10_GFF3_genes.gff contains all the genes with their position on
     * the genome
     *  - TAIR10_functional_descriptions contains the functional description for
     * all these genes
     *  - ATH_GO_GOSLIM.txt contains the GO annotation for each gene
     *  - AGI2Uniprot.20101118 contains the UniProt ID to which each gene is
     * linked
     *
     * Finally it downloads also the excel file provided by the INRA of
     * Versaille containing the Cvi x Col genetic and physical map.
     *
     * @param force boolean, whether to force the download of the files or not.
     * @throws IOException if something goes wrong when trying to download the
     * files.
     */
    public void download(boolean force) throws IOException {
        HashMap<String, String> urls = new HashMap<String, String>();
        urls.put("ftp://ftp.arabidopsis.org/home/tair/Genes/TAIR10_genome_release/TAIR10_gff3/TAIR10_GFF3_genes.gff",
                "At/TAIR10_GFF3_genes.gff");
        urls.put("ftp://ftp.arabidopsis.org/home/tair/Genes/TAIR10_genome_release/TAIR10_functional_descriptions",
                "At/TAIR10_functional_descriptions");
        urls.put("ftp://ftp.arabidopsis.org/home/tair/Ontologies/Gene_Ontology/ATH_GO_GOSLIM.txt",
                "At/ATH_GO_GOSLIM.txt");
        urls.put("ftp://ftp.arabidopsis.org/home/tair/Proteins/Id_conversions/AGI2Uniprot.20101118",
                "At/AGI2Uniprot.20101118");
        urls.put("http://dbsgap.versailles.inra.fr/vnat/Documentation/8/CvixCol_MapCoord.xls",
                "At/CvixCol_MapCoord.xls");

        Set<String> urlset = urls.keySet();
        int cnt = 0;
        for (Iterator<String> it = urlset.iterator(); it.hasNext();) {
            String key = it.next();
            cnt = cnt + 1;
            App.downaloadFile(key, urls.get(key), force);
        }
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
            inputfilename = "At/TAIR10_GFF3_genes.gff";
            At_ParseGeneInfo parser = new At_ParseGeneInfo();
            model = parser.getModelFromTbl(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        try {
            // File containing the gene description
            inputfilename = "At/TAIR10_functional_descriptions";
            At_ParseGeneDescription parser = new At_ParseGeneDescription();
            model = parser.getModelFromTbl(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        try {
            // GFF file containing GO annotation for genes
            // used release from : 05/17/2011     06:33:00 AM
            inputfilename = "At/ATH_GO_GOSLIM.txt";
            At_ParseGoGene parser = new At_ParseGoGene();
            model = parser.getModelFromAthGo(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        try {
            // AGI2 Uniprot adding Gene Protein relation
            inputfilename = "At/AGI2Uniprot.20101118";
            At_ParseGeneProtein parser = new At_ParseGeneProtein();
            model = parser.getModelFromAGI2Uniprot(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        try {
            // Add physical location of the markers
            inputfilename = "At/CvixCol_Physic.csv";
            At_PhysicalMap parser = new At_PhysicalMap();
            model = parser.getModelFromPhysicalMap(inputfilename, model);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        try {
            // Add genomic location of the markers
            inputfilename = "At/CvixCol_Genetic.csv";
            At_GeneticMap parser = new At_GeneticMap();
            model = parser.getModelFromGeneticMap(inputfilename, model);
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
                String outputfilename = "At/genemodel.rdf";
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