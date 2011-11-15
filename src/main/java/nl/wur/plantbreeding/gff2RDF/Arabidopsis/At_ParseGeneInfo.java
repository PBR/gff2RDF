/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF.Arabidopsis;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.ObjectToModel;
import nl.wur.plantbreeding.gff2RDF.object.Gene;

/**
 * This class parses the files submitted by TAIR to the NCBI in the release
 * TAIR10.
 * Files have been downloaded on May 17th 2011 on the TAIR ftp and the parser
 * is adapted to this format.
 *
 * The file name is of the type:
 *      TAIR10_GFF3_genes.gff
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_ParseGeneInfo {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            At_ParseGeneInfo.class.getName());

    /**
     * This method parse the GFF files from TAIR and add the information
     * retrieved about the genes into the model given as parameter.
     * The gene information retrieved by this method are:
     *   - Gene ID
     *   - Gene position on the genome (physical position):
     *       - Chromosome
     *       - Start position
     *       - Stop position
     *
     * @param inputfilename the path to the input file containing the gene
     * information
     * @param model a Jena model in which the gene information will be stored
     * @return a Jena model containing with its previous information the gene
     * information retrieved by this method.
     * @throws IOException When something goes wrong with a file.
     */
    public final Model getModelFromGff(final String inputfilename,
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
        Gene gene = null;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.trim();
            String[] content = strline.split("\t");
            if (content.length > 3 && content[2].equalsIgnoreCase("gene")) {
                gene = new Gene();
                gene.setChromosome(content[0].trim());
                gene.addPosition(content[3], content[4]);
                String locus = content[content.length - 1].split(
                        "ID=")[1].split(";")[0];
                gene.setLocus(locus);
                String function = content[content.length - 1].split(
                        "Note=")[1].split(";")[0];
                gene.setDescription(function);

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

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, genecnt + " genes found");
        LOG.log(Level.FINE, "Model has size: " + model.size());

        return model;
    }
}
