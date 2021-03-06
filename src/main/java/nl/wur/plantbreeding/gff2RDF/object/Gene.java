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

package nl.wur.plantbreeding.gff2RDF.object;

import java.util.ArrayList;

/**
 * This class represent Gene information.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Gene {

    /** Locus of the gene. */
    private String locus = "";
    /** Type of the gene. */
    private String type = "";
    /** Description of the gene. */
    private String description = "";
    /** Gene synonym. */
    private String synonym = "";
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
    /** The orientation of the gene on the genome. */
    private String orientation;

    /**
     * Return the type of the gene.
     * @return String describing the type of the gene
     */
    public final String getType() {
        return type;
    }

    /**
     * Set the type of the gene.
     * @param typetmp String describing the type of the gene
     */
    public final void setType(final String typetmp) {
        this.type = typetmp;
    }

    /**
     * Return a String description the function of the gene.
     * @return a String describing the gene.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the gene (its function).
     * @param description a String representing the description of the gene.
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Set the start position of the gene on the genome.
     * @param tmpstart a String of the start position
     */
    public final void setStart(final String tmpstart) {
        this.start = Integer.parseInt(tmpstart);
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
     * Set the stop position of the gene on the genome.
     * @param tmpstop a String of the start position
     */
    public final void setStop(final String tmpstop) {
        this.stop = Integer.parseInt(tmpstop);
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

    /**
     * Get the synonym gene name for this gene.
     * @return the synonym gene name for this gene.
     */
    public final String getSynonym() {
        return synonym;
    }

    /**
     * Set a synonym name for the gene.
     * @param tmpsynonym a String of a synonym name
     */
    public final void setSynonym(final String tmpsynonym) {
        this.synonym = tmpsynonym;
    }

    /**
     * Retrieve the orientation of this gene.
     * @return a String representating the orientation of this gene
     * (should either + or -)
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Set the orientation of this gene.
     * @param orientation a String which gives the information about the
     * orientation of this gene on the genome (should be either + or -).
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
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

    /**
     * Output the content of the object.
     */
    @Override
    public final String toString() {
        String newline = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getName()).append(" Object {").append(newline);
        result.append("Name: ").append(this.name).append(newline);
        result.append("Description: ").append(this.description).append(newline);
        result.append("Synonym: ").append(this.synonym).append(newline);
        result.append("Type: ").append(this.type).append(newline);
        result.append("Locus: ").append(this.locus).append(newline);
        result.append("Chromosome: ").append(this.chromosome).append(newline);
        result.append("Start: ").append(this.start).append(newline);
        result.append("Stop: ").append(this.stop).append(newline);
        result.append("}");
        return result.toString();
    }

}
