/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2rdf2.Arabidopsis;

import java.util.ArrayList;

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
