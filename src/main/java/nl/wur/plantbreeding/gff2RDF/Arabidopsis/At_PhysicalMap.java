/*
 * Copyright (c) 2012, Pierre-Yves Chibon
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the Wageningen University nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ''AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
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
