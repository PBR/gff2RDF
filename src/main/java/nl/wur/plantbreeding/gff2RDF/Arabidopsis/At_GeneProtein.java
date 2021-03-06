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

/**
 * This class represent the Gene/protein information retrieved from
 * the AGI2Uniprot files.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_GeneProtein {

    /** Locus of the gene. */
    private String locus = "";
    /** Protein associated with the gene. */
    private String protein = "";

    /**
     * Get the protein associated with this gene.
     * @return String representing the protein ID
     */
    public final String getProtein() {
        return protein;
    }

    /**
     * Set the protein (protein ID) associated with this gene.
     * @param tmpprotein String representing the protein ID
     */
    public final void setProtein(final String tmpprotein) {
        this.protein = tmpprotein;
    }

    /**
     * Retrueve the locus of the gene on the Arabidopsis genome.
     * @return a String of the locus of the gene
     */
    public final String getLocus() {
        return locus;
    }

    /**
     * Set the locus of the gene on the Arabidopsis genome.
     * @param tmplocus a String of the locus of the gene
     */
    public final void setLocus(final String tmplocus) {
        this.locus = tmplocus;
    }

}
