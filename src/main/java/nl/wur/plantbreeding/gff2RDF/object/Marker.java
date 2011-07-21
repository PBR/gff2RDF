/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * @param tmpstop a int of the start position
     */
    public final void setStop(final String tmpstop) {
        this.stop = Integer.parseInt(tmpstop);
    }
}
