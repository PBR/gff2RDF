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

package nl.wur.plantbreeding.gff2RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.Map.Ma_ParserMapInfo;

/**
 * This is the action class which parses the given map file and transform it
 * to a RDF model.
 *
 * The main function is 'main' function.
 * Which reads the input file and convert its content in a Jena Model which is
 * then writen as RDF if not empty.
 *
 * @author Pierre-Yves Chibon --py@chibon.fr
 */
class MapAction {
     /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            MapAction.class.getName());
    /** Folder in which the files are/will be stored. */
    private String folder = "Map/";

    /**
     * Default constructor.
     *
     * Creates the folder 'Map' if necessary.
     */
    public MapAction() {
        App.makeFolder(this.folder);
    }

    /**
     * Constructor.
     *
     * Creates a specific folder if asked.
     * @param newfolder String of the new folder to use.
     */
    public MapAction(String newfolder) {
        App.makeFolder(newfolder);
        this.folder = newfolder;
    }

    /**
     * Set the folder in which the files will/are stored.
     * @param tmpfolder a String of the name of the folder.
     */
    public void setFolder(String tmpfolder) {
        this.folder = tmpfolder;
    }

    /**
     * This function reads the downloaded files and conver their content into
     * an RDF model which is stored in a Jena Model object.
     *
     * @param debug boolean to print the stack trace of the exceptions catched.
     */
    public void main(String inputfilename, boolean physical,
            boolean debug) {
        Model model = ModelFactory.createDefaultModel();

        try {
            // GFF file containing the gene information;
            Ma_ParserMapInfo parser = new Ma_ParserMapInfo();
            model = parser.addMarkersToModel(inputfilename, model, physical);
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "IO Error in " + inputfilename
                    + ": \"{0}\"", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }

        System.out.println("Model final: " + model.size());
        try {
            if (!model.isEmpty()) {
                ModelIO mio = new ModelIO();
                String outputfilename = this.folder + "mapmodel.rdf";
                mio.printModelToFile(model, outputfilename);
            }
        } catch (IOException ex) {
            System.err.println();
            LOG.log(Level.SEVERE, "Error while writting: {0}", ex.getMessage());
            if (debug) {
                ex.printStackTrace(System.err);
            }
        }
    }

}
