/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2rdf2.Arabidopsis;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2rdf2.ObjectToModel;

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
    private final Logger log = Logger.getLogger(
            At_PhysicalMap.class.getName());

    /**
     * This method parses the genetic map to extract the physical position of
     * the marker in the map and add them to the model returned.
     *
     * @param inputfilename the path to the input file the genetic map
     * @param model a Jena model in which the gene information will be stored
     * @return a Jena model containing with its previous information the
     * information about the marker in the genetic map
     */
    public final Model getModelFromPhysicalMap(final String inputfilename,
            Model model) {

        log.log(Level.INFO, "Parsing: {0} and adding information to a model "
                + "of size " + model.size(), inputfilename);

        ObjectToModel obj2m = new ObjectToModel();

        int cnt = 0;
        int genecnt = 0;
        String strline = "";
        try {
            final FileInputStream fstream = new FileInputStream(inputfilename);
            // Get the object of DataInputStream
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            At_Marker marker = null;
            //Read File Line By Line
            while ((strline = br.readLine()) != null) {
                strline = strline.replace("\"", "");
                strline = strline.trim();
                String[] content = strline.split(",");
                if (content.length > 1 && cnt > 1) {
                    marker = new At_Marker(false);
                    marker.setName(content[0].trim());
                    marker.setChromosome(content[1].trim());
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

            log.log(Level.INFO, cnt + " lines read");
            log.log(Level.INFO, genecnt + " markers found");
            log.log(Level.INFO, "Model has size: " + model.size());
        } catch (Exception e) { //Catch exception if any
            log.log(Level.SEVERE, "Line: {0}", strline);
            log.log(Level.SEVERE, "Caught an exception: ", e);
        }


        return model;
    }
}
