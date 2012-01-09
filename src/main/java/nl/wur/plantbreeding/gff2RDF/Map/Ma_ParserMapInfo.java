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

package nl.wur.plantbreeding.gff2RDF.Map;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.ObjectToModel;
import nl.wur.plantbreeding.gff2RDF.object.Marker;

/**
 * This class parses the provided map file and convert it to RDF model.
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Ma_ParserMapInfo {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            Ma_ParserMapInfo.class.getName());

    public Model addMarkersToModel(String inputfilename, Model model)
        throws IOException {
        ObjectToModel obj2m = new ObjectToModel();

        System.out.println("Parsing: " + inputfilename
                + " and adding information to a model of size " + model.size());

        final FileInputStream fstream = new FileInputStream(inputfilename);
        // Get the object of DataInputStream
        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(in));
        String strline;
        //Read File Line By Line
        int cnt = 0;
        while ((strline = br.readLine()) != null) {
            final String[] content = strline.split("\t");
            // Deals with gene
            if (content.length > 1) {
                Marker marker = new Marker();
                marker.setId(content[0].trim());
                marker.setName(content[1].trim());
                marker.setChromosome(content[2].trim());
                marker.setPosition(content[3].trim());
                marker.setGenetic(true);
                model = obj2m.addToModel(marker, model);
                cnt = cnt + 1;
            }
        }
        //Close the input stream
        in.close();
        return model;
    }

}
