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

package nl.wur.plantbreeding.gff2RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This gives IO support for Jena Model (semantic graphes).
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class ModelIO {

    /**
     *
     * RDF Reading
     *
     */

    /**
     * Reads the RDF from a given file and returns the associated model.
     * @param filename the name of the file to read
     * @return a Jena model
     */
    public final Model readRdf(final String filename) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(filename);
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + filename + " not found");
        }

        // read the RDF/XML file
        model.read(in, null);

        return model;

    }


    /**
     *
     * Write Model (to stdout or file)
     *
     */

    /**
     * Print in standard output the RDF representation of the model.
     * @param model the Jena model to output
     */
    public final void displayModel(final Model model) {
        // now write the model in XML form to a file
        model.write(System.out);
    }

    /**
     * Write to a file with the given filename the RDF representation of the
     * model.
     * @param model the Jena model to output
     * @param filename the name of the file to print
     * @throws IOException when something goes wrong while outputing the model
     */
    public final void printModelToFile(final Model model, final String filename)
            throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        // now write the model in XML form to a file
        model.write(out);
        out.close();
        System.out.println("Write model in RDF in: " + filename);
    }

    /**
     *Print the model in N3 format.
     * @param filename the path to the file in which to write the model
     * @param model the Jena model to output
     * @throws IOException when something goes wrong while outputing the model
     */
    public final void printModelNtripleToFile(final Model model,
            final String filename)
            throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        // now write the model in N3 form to a file
        model.write(out, "N-TRIPLE");
        out.close();
        System.out.println("Write model in N# in: " + filename);
    }

    /**
     * Print in standard output the N3 representation of the model.
     * @param model the Jena model to output
     * @throws IOException when something goes wrong while outputing the model
     */
    public final void displayModelN3(final Model model) throws IOException {
        model.write(System.out);
    }
}
