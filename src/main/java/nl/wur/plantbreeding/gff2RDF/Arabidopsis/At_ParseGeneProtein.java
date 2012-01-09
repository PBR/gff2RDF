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

/**
 * This class parses the files retrieved on the TAIR FTP and which gives the
 * conversion/link between TAIR ID and UniProt ID
 * The file name is of the type:
 *   AGI2Uniprot.20101118
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_ParseGeneProtein {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            At_ParseGeneProtein.class.getName());

    /**
     * This method parses the AGI2Uniprot file and add to the model the mapping
     * between TAIR ID and UniProt ID as it find them.
     *
     * @param inputfilename the path to the input file containing the
     * gene/protein information
     * @param model a Jena model in which the gene/protein information will be
     * stored
     * @return a Jena model containing with its previous information the
     * gene/protein information retrieved by this method.
     * @throws IOException When something goes wrong with a file.
     */
    public final Model getModelFromAGI2Uniprot(final String inputfilename,
            Model model) throws IOException {

        System.out.println("Parsing: " + inputfilename
                + " and adding information to a model of size " + model.size());

        ObjectToModel obj2m = new ObjectToModel();

        int cnt = 0;
        final FileInputStream fstream = new FileInputStream(inputfilename);
        // Get the object of DataInputStream
        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(in));
        String strline;
        //Read File Line By Line
        while ((strline = br.readLine()) != null) {
            strline = strline.trim();
            String[] content = strline.split("\t");
            At_GeneProtein agp = new At_GeneProtein();
            // We keep only the content before the "."
            String locus = content[0].trim().split("\\.")[0];
            agp.setLocus(locus);
            agp.setProtein(content[1].trim());
            model = obj2m.addToModel(agp, model);
            cnt = cnt + 1;
        }
        in.close();

        LOG.log(Level.FINE, cnt + " lines read");
        LOG.log(Level.FINE, "Final model has size: " + model.size());

        return model;
    }
}
