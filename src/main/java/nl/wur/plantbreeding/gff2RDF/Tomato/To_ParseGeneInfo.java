/*
 * Copyright (c) 2012-2013, Pierre-Yves Chibon
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

package nl.wur.plantbreeding.gff2RDF.Tomato;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.ObjectToModel;
import nl.wur.plantbreeding.gff2RDF.object.Gene;
import nl.wur.plantbreeding.gff2RDF.object.Marker;

/**
 * This class parses the GFF file containing the genes and proteins information
 * from ITAG and populate an RDF model from these information.
 *
 * As format may change, the parser may need changes for futur release.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class To_ParseGeneInfo {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            To_ParseGeneInfo.class.getName());

    /**
     * This method parse the file ITAG2_genomic_all.gff3 from ITAG
     * and extract the genes and their annontation which are then added to the
     * Jena Model.
     *
     * @param inputfilename the path to the input file containing the gene
     * functional description
     * @param model a Jena model in which the gene description will be stored
     * @return a Jena model containing with its previous information the gene
     * description retrieved by this method.
     * @throws IOException When something goes wrong with a file.
     */
    public Model addGenesToModel(String inputfilename, Model model)
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
            if (content.length < 2) {
                continue;
            }

            if (content[1].equalsIgnoreCase("itag_sgn_markers")
                    && content[2].equalsIgnoreCase("match")) {
                Marker marker = new Marker();
                // Add the SGN marker to the model
                final String sgnid = content[content.length - 1].split(
                        "Alias=")[1].split(";")[0];
                marker.setId(sgnid);
                marker.setSgnid(sgnid);

                final String name = content[content.length - 1].split(
                        "Name=")[1].split(";")[0];
                marker.setName(name);
                

                marker.setChromosome(content[0]);

                final int start = Integer.parseInt(content[3]);
                final int stop = Integer.parseInt(content[4]);
                if (start < stop) {
                    marker.setStart(start);
                    marker.setStop(stop);
                } else {
                    marker.setStart(stop);
                    marker.setStop(start);
                }

                model = obj2m.addToModel(marker, model);

            } else if (content[1].equalsIgnoreCase("itag_renaming")
                    && content[2].equalsIgnoreCase("mrna")) {
                Gene gene = new Gene();
                // Add gene information (description, GO...) to the model
                final String desc = content[content.length - 1];
                String name = null;
                String description = null;
                final ArrayList<String> go = new ArrayList<String>();
                for (String val : desc.split(";")) {
                    final String key = val.split("=")[0];
                    String value = val.split("=")[1];
                    if (key.equalsIgnoreCase("ID")) {
                        value = value.split("mRNA:")[1];
                        name = value;
                    } else if (key.equalsIgnoreCase("note")) {
                        description = value;
                    } else if (key.equalsIgnoreCase("ontology_term")) {
                        gene.addGoTerm(value);
                    }
                }
                gene.setLocus(name);
                gene.setDescription(description);
                gene.setType("gene:ITAG_renaming");
                gene.setOrientation(content[6]);

                final int start = Integer.parseInt(content[3]);
                final int stop = Integer.parseInt(content[4]);

                gene.setChromosome(content[0]);
                if (start < stop) {
                    gene.setStart(start);
                    gene.setStop(stop);
                } else {
                    gene.setStart(stop);
                    gene.setStop(start);
                }

                model = obj2m.addToModel(gene, model);
            }

            cnt = cnt + 1;
        }
        in.close();
        return model;
    }

    public Model addProteinsToModel(String inputfilename, Model model)
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
                if (content[1].equalsIgnoreCase("ITAG_blastp_swissprot")
                        && content[2].equalsIgnoreCase("match")) {
                    // Add Swissprot (verified) proteins to the model
                    final String uniprotid =
                            content[content.length - 1].split(
                            "subject_id=")[1].split(
                            ";")[0];
                    final String geneid = content[0];//.split("\\.")[0];

                    model = obj2m.addProteinToModel(model,
                            geneid, uniprotid);

                } else if (content[1].equalsIgnoreCase("ITAG_blastp_trembl")
                        && content[2].equalsIgnoreCase("match")) {
                    // Add Trembl (unverified) proteins to the model
                    final String uniprotid =
                            content[content.length - 1].split(
                            "subject_id=")[1].split(
                            ";")[0];
                    final String geneid = content[0];//.split("\\.")[0];

                    model = obj2m.addProteinToModel(model,
                            geneid, uniprotid);
                }
                cnt = cnt + 1;
            }
        }
        //Close the input stream
        in.close();
        return model;
    }
}
