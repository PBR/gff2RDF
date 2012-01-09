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
import nl.wur.plantbreeding.gff2RDF.object.Marker;

/**
 * This class parses the Arabidopsis thaliana Physical Map
 *
 * File was obtain converted in tab delimiter format using OOo Calc based on
 * this file:
 * http://dbsgap.versailles.inra.fr/vnat/Documentation/8/CvixCol_MapCoord.xls
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_PhysicalMap {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            At_PhysicalMap.class.getName());

    /**
     * This method parses the genetic map to extract the physical position of
     * the marker in the map and add them to the model returned.
     *
     * @param inputfilename the path to the input file the genetic map
     * @param model a Jena model in which the gene information will be stored
     * @return a Jena model containing with its previous information the
     * information about the marker in the genetic map
     * @throws IOException When something goes wrong with a file.
     */
    public final Model getModelFromPhysicalMap(final String inputfilename,
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
        Marker marker = null;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.replace("\"", "");
            strline = strline.trim();
            String[] content = strline.split(",");
            if (content.length > 1 && cnt > 1) {
                marker = new Marker(false);
                marker.setGenetic(false);
                marker.setName(content[0].trim());
                String chr = "Chr" + content[1].trim();
                marker.setChromosome(chr);
                marker.setStart(content[2].trim());
                marker.setStop(content[2].trim());

                // Add marker to model
                model = obj2m.addToModel(marker, model);
                genecnt = genecnt + 1;
            }
            cnt = cnt + 1;
        }
        in.close();

        if (marker != null) {
            // add marker to model here
            model = obj2m.addToModel(marker, model);
        }

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, genecnt + " markers found");
        LOG.log(Level.FINE, "Model has size: " + model.size());

        return model;
    }
}
