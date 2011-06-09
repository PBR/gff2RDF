/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2rdf2.Arabidopsis;

import java.util.ArrayList;

/**
 * This class represent the Gene information retrieved from the TAIR files.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_Gene {

    /** Locus of the gene. */
    private String locus = "";
    /** Function of the gene. */
    private String function = "";
    /** Name of the gene. */
    private String name = "";
    /** List of GO terms associated with the gene. */
    private ArrayList<String> goterms = new ArrayList<String>();
    /** Start position of the gene on the genome. */
    private int start;
    /** Stop position of the gene on the genome. */
    private int stop;
    /** The Chromosome of the gene on the genome. */
    private String chromosome;

    /**
     * Return the function of the gene.
     * @return String describing the function of the gene
     */
    public final String getFunction() {
        return function;
    }

    /**
     * Set the function of the gene.
     * @param tmpfunction String describing the function of the gene
     */
    public final void setFunction(final String tmpfunction) {
        this.function = tmpfunction;
    }

    /**
     * Get the list of GO terms associated with this gene.
     * @return ArrayList of String representing the GO ID
     */
    public final ArrayList<String> getGoterms() {
        return goterms;
    }

    /**
     * Set the list of GO terms (GO ID) associated with this gene.
     * @param tmpgoterms ArrayList of String representing the Go ID
     */
    public final void setGoterms(final ArrayList<String> tmpgoterms) {
        this.goterms = tmpgoterms;
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

    /**
     * Retrieve the name of the gene.
     * @return a String of the name of the gene
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the name of the gene.
     * @param tmpname a String of the name of the gene
     */
    public final void setName(final String tmpname) {
        this.name = tmpname;
    }

    /**
     * Retrieve the start position of the gene on the genome.
     * @return a int of the start position
     */
    public final int getStart() {
        return start;
    }

    /**
     * Set the start position of the gene on the genome.
     * @param tmpstart a int of the start position
     */
    public final void setStart(final int tmpstart) {
        this.start = tmpstart;
    }

    /**
     * Retrieve the stop position of the gene on the genome.
     * @return a int of the stop position
     */
    public final int getStop() {
        return stop;
    }

    /**
     * Set the stop position of the gene on the genome.
     * @param tmpstop a int of the start position
     */
    public final void setStop(final int tmpstop) {
        this.stop = tmpstop;
    }

    /**
     * Return the chromosome of the gene.
     * @return a String representing the chromosome of the gene
     */
    public final String getChromosome() {
        return chromosome;
    }

    /**
     * Set the chromosome of the gene.
     * @param tmpchromosome a String of the chromosome of the gene
     */
    public final void setChromosome(final String tmpchromosome) {
        this.chromosome = tmpchromosome;
    }



    /*
     * Non-basic functions
     */



    /**
     * Add a GO ID to the list of Go terms associated with the gene
     * if the GO ID is not in the list yet.
     * @param tmpgo a String of the GO ID.
     */
    public final void addGoTerm(final String tmpgo) {
        if (!goterms.contains(tmpgo)) {
            goterms.add(tmpgo);
        }
    }

    /**
     * Add the start and stop position of the gene on the genome.
     * Invert start and stop if stop is higher than start.
     * @param tmpstart a int of the start position
     * @param tmpstop a int of the stop position
     */
    public final void addPosition(final int tmpstart, final int tmpstop) {
        if (tmpstart > tmpstop) {
            start = tmpstop;
            stop = tmpstart;
        } else {
            start = tmpstart;
            stop = tmpstop;
        }
    }
    /**
     * Add the start and stop position of the gene on the genome.
     * Invert start and stop if stop is higher than start.
     * @param tmpstart a string of the start position
     * @param tmpstop a string of the stop position
     */
    public final void addPosition(final String tmpstart, final String tmpstop) {
        final int intstart = Integer.parseInt(tmpstart);
        final int intstop = Integer.parseInt(tmpstop);
        this.addPosition(intstart, intstop);
    }
}
