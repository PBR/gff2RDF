/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2RDF.Arabidopsis;

import nl.wur.plantbreeding.gff2rdf2.object.Marker;

/**
 * This class represent the Arabidopsis thaliana markers.
 * It can be used for both genetic or physical marker.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class At_Marker extends Marker{
    /**
     * Default constructor setting the genetic boolean to true.
     * (This mean that the marker is considered as being located on a genetic
     * map)
     */
    public At_Marker() {
        this.genetic = true;
    }

    /**
     * Constructor.
     * @param geneticmap a boolean indicated wether the marker is located on a
     * genetic map (true) or a physical map (false).
     */
    public At_Marker(final boolean geneticmap) {
        this.genetic = geneticmap;
    }
}
