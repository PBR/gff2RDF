package nl.wur.plantbreeding.gff2RDF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_GeneticMap;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGeneDescription;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGeneInfo;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGeneProtein;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_ParseGoGene;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_PhysicalMap;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

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

        ClOptions options = new ClOptions();
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(90);

        try {
            parser.parseArgument(args);

            if (options.isHelp() || args.length == 0) {
                parser.printUsage(System.err);
                return;
            }

            if (options.isArabidopsis()) {
                ArabidopsisAction aa = new ArabidopsisAction();
                aa.download(options.isForceDl());
                aa.main(options.isDebug());
            }
        } catch (CmdLineException e) {
            LOG.log(Level.SEVERE, "Error in the command line: {0}",
                    e.getMessage());
            System.err.println("java -jar gff2rdf2.jar [options...] arguments...");
            parser.printUsage(System.err);
            System.exit(1);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "An Error occured: {0}", e.getMessage());
            e.printStackTrace(System.err);
            System.exit(100);
            return;
        }
    }

    /**
     * Retrieve the URI set.
     * @return a String of a based URI to use in the models
     */
    public final String getUri() {
        return this.uri;
    }

    public static void downaloadFile(String url, String outputfile, boolean force)
            throws MalformedURLException, IOException {
        System.out.println("Trying to download file: '" + outputfile
                + "' from :\n " + url);
        File f = new File(outputfile);
        if (f.exists() && !force) {
            System.out.println("  -- no need \n");
            LOG.log(Level.FINE, "The file {0} already exists, no need to re-download it",
                    outputfile);
            return;
        }
        System.out.println();
        URL google = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(google.openStream());
        FileOutputStream fos = new FileOutputStream(outputfile);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
    }

    public static void makeFolder(String foldername) {
        System.out.println(
                "The files downloaded and generated will be stored in the folder "
                + foldername + " created in the current directory.");
        File f = new File(foldername);
        if (!f.exists()) {
            f.mkdir();
        } else {
            LOG.log(Level.FINE,
                    "The folder '{0}' already exists, no need to create it.",
                    foldername);
        }
    }
}
