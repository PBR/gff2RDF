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

package nl.wur.plantbreeding.gff2RDF.object;

/**
 * This class represent Markers information.
 * It can be used for both genetic or physical marker.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class Marker {
    /** Name of the marker. */
    private String name = "";
    /**
     * ID of the marker, if not empty it will be used for the URI, otherwise
     * the name will be.
     */
    private String id = "";
    /** SGN markers have their own identifier which we use in one query.
     */
    private String sgnid = "";

    /**
     * Position of the marker either on the genetic map or on the physical map.
     */
    private String position;
    /** The Chromosome of the gene on the genome. */
    private String chromosome;
    /** Start position of the gene on the genome. */
    private int start;
    /** Stop position of the gene on the genome. */
    private int stop;
    /**
     * Boolean reflecting wether the marker is located on a genetic map (true)
     * or a physical map (false).
     * Default is genetic == true
     */
    protected boolean genetic;

    /**
     * Default constructor
     */
    public Marker() { }

    /**
     * Constructor allowing to specify if the marker is genetic or physical.
     * @param b a boolean specifying if the marker is genetic (true, default) or
     * on a physical map (false).
     */
    public Marker(boolean b) {
        this.genetic = b;
    }

    /**
     * Ask wether the marker is located on a genetic map (true) or on a physical
     * map (false).
     * @return a boolean
     */
    public final boolean isGenetic() {
        return genetic;
    }

    /**
     * Set the boolean wether the marker is located on a genetic map (true) or
     * on a physical map (false).
     * @param tmpgenetic a boolean
     */
    public final void setGenetic(final boolean tmpgenetic) {
        this.genetic = tmpgenetic;
    }

    /**
     * Get the name of the marker.
     * @return the name of the marker
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the name of the marker.
     * @param tmpname name of the marker
     */
    public final void setName(final String tmpname) {
        this.name = tmpname;
    }

    /**
     * Set the chromosome of the marker.
     * @param tmpchr name of the chromosome
     */
    public final void setChromosome(final String tmpchr) {
        this.chromosome = tmpchr;
    }

    /**
     * Get the chromosome on which the marker is located.
     * @return the chromosome of the marker
     */
    public final String getChromosome() {
        return this.chromosome;
    }

    /**
     * Set the position of the marker, either on the genetic map or on the
     * physical map.
     * @param tmppos position of the marker
     */
    public final void setPosition(final String tmppos) {
        this.position = tmppos;
    }

    /**
     * Set the position of the marker, either on the genetic map or on the
     * physical map.
     * @param tmppos position of the marker
     */
    public final void setPosition(final int tmppos) {
        this.position = Integer.toString(tmppos);
    }

    /**
     * Return the position of the marker either on the genetic map or on the
     * physical map.
     * @return position of the marker
     */
    public final String getPosition() {
        return this.position;
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
     * @param tmpstart a int of the start position
     */
    public final void setStart(final String tmpstart) {
        this.start = (int) Double.parseDouble(tmpstart);
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
     * @param tmpstop a int of the start position
     */
    public final void setStop(final String tmpstop) {
        this.stop = (int) Double.parseDouble(tmpstop);
    }

    /**
     * Return the id of the marker if this one is not empty, otherwise returns
     * the name.
     * @return a String which is either the id if it is set or the name.
     */
    public String getId() {
        if (this.id == null || this.id.isEmpty()){
            return this.name;
        } else {
            return this.id;
        }
    }

    /**
     * Set the id of the marker to be used in the URI.
     * @param idtmp a String of the marker identifier.
     */
    public void setId(String idtmp) {
        this.id = idtmp;
    }

    /**
     * Retrieve the SGN Identifier used for this marker.
     * @return a String of the SGN identifier.
     */
    public String getSgnid() {
        return sgnid;
    }

    /**
     * Set the SGN identifier of this marker.
     * @param sgnidtmp a String of the SGN identifier.
     */
    public void setSgnid(String sgnidtmp) {
        this.sgnid = sgnidtmp;
    }

}
