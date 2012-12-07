/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF.Potato;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.ObjectToModel;
import nl.wur.plantbreeding.gff2RDF.object.Gene;

/**
 * This class associates GO terms to PGSC genes. The GO information comes from
 * the file "Solanum_phureja.txt" which associates GO to peptide, these peptide
 * identifier are transformed to gene identifiers using the conversion table
 * provided in the file "PGSC_DM_v3.4_g2t2c2p2func.txt".
 *
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Po_ParseGo {

    /**
     * Logger used for outputing log information.
     */
    private static final Logger LOG = Logger.getLogger(
            Po_ParseGeneDescription.class.getName());

    /**
     * Default constructor.
     */
    public Po_ParseGo() {
    }

    /**
     * This method parses the given input file which should be
     * "Solanum_phureja.txt" and extract for each peptide identifier the
     * associated GO terms. These peptide identifier are then converted to gene
     * identifier using the conversion table generated in the
     * Po_ParseGeneToProtein class and the resulting gene, GO term association
     * is added to the model.
     *
     * @param inputfilename a String giving the name of the input file to read.
     * @param convertion_table a HashMap of String:String having PGSC peptide
     * identifier as key and PGSC gene identifier as value.
     * @param model a Jena Model in which the gene, GO association should be
     * added
     * @return a Jena Model in which the gene, GO association has been added.
     * @throws IOException throws when something goes wrong while trying to read
     * the input file.
     */
    public Model addGeneGoToModel(String inputfilename,
            HashMap<String, String> convertion_table, Model model)
            throws IOException {
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
        String peptideid = null;
        String goterms = null;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.trim();
            String[] content = strline.split("\t");
            if (content.length > 1 && cnt > 1) {
                peptideid = content[0];
                goterms = content[1];
                String geneid = convertion_table.get(peptideid);
                if (geneid == null) {
                    continue;
                }
                Gene gene = new Gene();
                gene.setLocus(geneid);
                for (String go : goterms.split(",")) {
                    if (!go.trim().isEmpty()) {
                        gene.addGoTerm(go);
                    }
                }
                model = obj2m.addToModel(gene, model);
                genecnt = genecnt + 1;
            }
            cnt = cnt + 1;
        }
        in.close();

        if (peptideid != null && goterms != null) {
            String geneid = convertion_table.get(peptideid);
            Gene gene = new Gene();
            gene.setLocus(geneid);
            for (String go : goterms.split(",")) {
                if (!go.trim().isEmpty()) {
                    gene.addGoTerm(go);
                }
            }
            model = obj2m.addToModel(gene, model);
            genecnt = genecnt + 1;
        }

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, genecnt + " genes found");
        LOG.log(Level.FINE, "Model has size: " + model.size());

        return model;
    }
}
