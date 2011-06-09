package nl.wur.plantbreeding.gff2rdf2;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2rdf2.Arabidopsis.At_GeneticMap;
import nl.wur.plantbreeding.gff2rdf2.Arabidopsis.At_ParseGeneDescription;
import nl.wur.plantbreeding.gff2rdf2.Arabidopsis.At_ParseGeneInfo;
import nl.wur.plantbreeding.gff2rdf2.Arabidopsis.At_ParseGeneProtein;
import nl.wur.plantbreeding.gff2rdf2.Arabidopsis.At_ParseGoGene;
import nl.wur.plantbreeding.gff2rdf2.Arabidopsis.At_PhysicalMap;

/**
 * Main class of the project.
 *
 * Receive command line arguments and deal with them.
 * This program has no command line argument and is used and run from its IDE.
 * The potential arguments are for the moment set as variable in the main class.
 * The input and function are managed from this class.
 */
public class App {

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

        String homefolder = System.getProperty("user.home").replace('\\', '/');
        Model model = ModelFactory.createDefaultModel();


        // GFF file containing the gene information
        String inputfilename = homefolder + "/Desktop/At/TAIR10_GFF3_genes.gff";
        model = App.AtGenes(model, inputfilename);

        inputfilename = homefolder
                + "/Desktop/At/TAIR10_functional_descriptions";
        model = App.AtGeneDescription(model, inputfilename);

        // GFF file containing GO annotation for genes
        // use release from : 05/17/2011     06:33:00 AM
        inputfilename = homefolder + "/Desktop/At/ATH_GO_GOSLIM.txt";
        model = App.AtGoGene(model, inputfilename);

        // AGI2 Uniprot adding Gene Protein relation
        inputfilename = homefolder + "/Desktop/At/AGI2Uniprot.20101118";
        model = App.AtGeneProtein(model, inputfilename);

        // Add physical location of the markers
        inputfilename = homefolder + "/Desktop/At/CvixCol_Physic.csv";
        model = App.AtPhysicalMap(model, inputfilename);

        // Add genomic location of the markers
        inputfilename = homefolder + "/Desktop/At/CvixCol_Genetic.csv";
        model = App.AtGeneticMap(model, inputfilename);


        LOG.log(Level.INFO, "Model final: " + model.size());
        try {
            ModelIO mio = new ModelIO();
            String outputfilename = homefolder + "/Desktop/At/genemodel.n3";
            mio.printModelNtripleToFile(model, outputfilename);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error while writting: ", ex);
        }
    }

    /**
     * Retrieve the URI set.
     * @return a String of a based URI to use in the models
     */
    public final String getUri() {
        return this.uri;
    }

    private static Model AtGenes(Model model, final String inputfilename) {
        // Arabidopsis thaliana's GENE parser
        At_ParseGeneInfo parser = new At_ParseGeneInfo();
        model = parser.getModelFromTbl(inputfilename, model);
        return model;
    }

    private static Model AtGeneDescription(Model model, String inputfilename) {
        At_ParseGeneDescription parser = new At_ParseGeneDescription();
        model = parser.getModelFromTbl(inputfilename, model);
        return model;
    }

    private static Model AtGoGene(Model model, String inputfilename) {
        At_ParseGoGene parser = new At_ParseGoGene();
        model = parser.getModelFromAthGo(inputfilename, model);
        return model;
    }

    private static Model AtGeneProtein(Model model, final String inputfilename) {
        // Arabidopsis thalian's Gene/Protein Parser
        At_ParseGeneProtein parser = new At_ParseGeneProtein();
        model = parser.getModelFromAGI2Uniprot(inputfilename, model);
        return model;
    }

    private static Model AtGeneticMap(Model model, final String inputfilename) {
        // Arabidopsis thalian's Gene/Protein Parser
        At_GeneticMap parser = new At_GeneticMap();
        model = parser.getModelFromGeneticMap(inputfilename, model);
        return model;
    }

    private static Model AtPhysicalMap(Model model, final String inputfilename) {
        // Arabidopsis thalian's Gene/Protein Parser
        At_PhysicalMap parser = new At_PhysicalMap();
        model = parser.getModelFromPhysicalMap(inputfilename, model);
        return model;
    }
}
