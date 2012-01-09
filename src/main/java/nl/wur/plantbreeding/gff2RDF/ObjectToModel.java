/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wur.plantbreeding.gff2RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.logging.Logger;
import nl.wur.plantbreeding.gff2RDF.Arabidopsis.At_GeneProtein;
import nl.wur.plantbreeding.gff2RDF.object.Gene;
import nl.wur.plantbreeding.gff2RDF.object.Marker;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * This class handles the conversion from the object used in this programm
 * to the RDF model.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class ObjectToModel {

    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            ObjectToModel.class.getName());

    /**
     * This is the based URI which will be used in the construction of the
     * model.
     */
    private final String uri = new App().getUri();
    /** The uri used to specify gene type and gene attributes. */
    private final String geneuri = uri + "GENE#";
    /** The uri used to specify scaffold type and scaffold attributes. */
    private final String scaffolduri = uri + "SCAFFOLD#";
    /** The uri used to specify marker type and marker attributes. */
    private final String markeruri = uri + "MARKER#";
    /** The uri used to specify position type and position attributes. */
    private final String positionuri = uri + "POSITION#";
    /** The uri used to specify map position type and map position attributes. */
    private final String mappositionuri = uri + "MAPPOSITION#";
    /** The uri used to specify GO type and GO attribute.
     * This is the URI used by geneontology.org
     */
    private final String gouri = "http://purl.org/obo/owl/GO#";
    /** The uri used to specify protein type and protein attribute.
     * This is the URI used by uniprot.
     */
    private final String proteinuri = "http://purl.uniprot.org/uniprot/";
    

    /**
     * This method add the given Arabidopsis thaliana gene information to the
     * provided Jena model and return this model.
     * @param geneobj an Arabidopsis thaliana gene with information
     * @param model a Jena Model
     * @return a Jena Model with the gene information
     */
    public final Model addToModel(final Gene geneobj, final Model model) {
        // Set the different URI that will be used

        // Create the scaffold node, add type and name
        Resource scaffold = model.createResource(scaffolduri
                + geneobj.getChromosome());
        scaffold.addProperty(RDF.type, scaffolduri);
        if (geneobj.getChromosome() != null
                && !geneobj.getChromosome().isEmpty()) {
            scaffold.addProperty(model.createProperty(scaffolduri
                    + "ScaffoldName"), geneobj.getChromosome());
        }

        // Create the gene node and add the type and basic information
        Resource gene = model.createResource(geneuri + geneobj.getLocus());
        gene.addProperty(RDF.type, geneuri);
        gene.addProperty(model.createProperty(geneuri + "FeatureName"),
                geneobj.getLocus());
        if (geneobj.getFunction() != null && !geneobj.getFunction().isEmpty()) {
            gene.addProperty(model.createProperty(geneuri + "FeatureType"),
                geneobj.getFunction());
        }

        // Create the position node, add type and start, stop and chr
        // information
        if (geneobj.getChromosome() != null
                && !geneobj.getChromosome().isEmpty()) {
            Resource position = model.createResource();
            position.addProperty(RDF.type, positionuri);
            position.addProperty(model.createProperty(positionuri + "Start"),
                    Integer.toString(geneobj.getStart()));
            position.addProperty(model.createProperty(positionuri + "Stop"),
                    Integer.toString(geneobj.getStop()));
            position.addProperty(model.createProperty(positionuri + "Scaffold"),
                    scaffold);
            gene.addProperty(model.createProperty(geneuri + "Position"),
                    position);
        }

        // Iterate over the GO term list and add them to the model
        for (String go : geneobj.getGoterms()) {
            String goi = go.replace(":", "_");
            Resource goterm = model.createResource(gouri + goi);
            goterm.addProperty(RDF.type, gouri);
            goterm.addProperty(model.createProperty(gouri + "GoID"), go);
            gene.addProperty(model.createProperty(geneuri + "Go"), goterm);
        }

        return model;
    }

    /**
     * This function add the given Arabidopsis thaliana gene/protein relation
     * information to the model.
     * @param agp an At_GeneProtein containing the relation between one gene
     * and one protein.
     * @param model a Jena Model to add the information into.
     * @return the Jena Model with the information added
     */
    public final Model addToModel(final At_GeneProtein agp, final Model model) {
        // Set the different URI that will be used

        // Create the gene node and add the type
        Resource gene = model.createResource(geneuri + agp.getLocus());
        gene.addProperty(RDF.type, geneuri);

        // Create the protein node
        final Resource protein = model.createResource(proteinuri
                + agp.getProtein());

        // Link the gene node to the protein node
        gene.addProperty(model.createProperty(geneuri + "Protein"), protein);

        return model;
    }

    /**
     * Add a Arabidopsis thaliana marker to the model.
     * This marker can be either located on a genetic map (it has a Position)
     * or on a physical map (it has no Position but a Start and Stop).
     * @param marker a At_Marker to add to the model.
     * @param model the Jena model to which the At_Marker will be added.
     * @return the given Jena Model containing the original information and the
     * information about the marker.
     */
    public final Model addToModel(final Marker marker, final Model model) {
        // Create the gene node and add the type
        Resource markerres = model.createResource(markeruri
                + marker.getName());
        markerres.addProperty(model.createProperty(markeruri
                        + "MarkerName"), marker.getName());
        markerres.addProperty(RDF.type, markeruri);

        // Create the scaffold node, add type and name
        Resource scaffold = model.createResource(scaffolduri
                + marker.getChromosome());
        scaffold.addProperty(RDF.type, scaffolduri);

        if (marker.getChromosome() != null
                && !marker.getChromosome().isEmpty()) {

            // Add the scaffold name
            scaffold.addProperty(model.createProperty(scaffolduri
                    + "ScaffoldName"), marker.getChromosome());

            // Create the position node to the physical and genetic map
            if (marker.isGenetic()) {
                // Genetic location of the marker
                markerres.addProperty(model.createProperty(markeruri
                        + "mapPosition"), marker.getPosition());
                markerres.addProperty(model.createProperty(markeruri
                        + "Chromosome"), marker.getChromosome());
            } else {
                // Physical location of the marker
                Resource position = model.createResource();
                position.addProperty(RDF.type, positionuri);
                position.addProperty(model.createProperty(positionuri
                        + "Start"), Integer.toString(marker.getStart()));
                position.addProperty(model.createProperty(positionuri + "Stop"),
                        Integer.toString(marker.getStop()));
                position.addProperty(model.createProperty(positionuri
                        + "Scaffold"), scaffold);
                markerres.addProperty(model.createProperty(markeruri
                        + "Position"), position);
            }
        }

        return model;
    }

    /**
     * Add the given description to a given gene in the given Jena Model.
     * @param geneid the geneid of the gene (used in the URI).
     * @param description the description to be added to the gene.
     * @param model the model in which this gene and its description go
     * @return the Jena Model with the added information
     */
    public final Model addGeneDescriptionToModel(final String geneid,
            String description, final Model model) {
        description = description.replaceAll("&#1", "");
        description = StringEscapeUtils.unescapeHtml(description);
        description = description.replaceAll("&", "");
        Resource gene = model.createResource(geneuri + geneid);
        gene.addProperty(RDF.type, geneuri);
        gene.addProperty(model.createProperty(geneuri + "Description"),
                description);
        return model;
    }

    /**
     * Link the given protein (ID) to the given gene (ID) in the provided Model.
     * @param model a Jena Model in which the gene will be linked to the protein.
     * @param geneid a String representing the gene identifier.
     * @param protid a String representing the protein identifier
     * @return the Jena Model linking the gene to the protein.
     */
    public Model addProteinToModel(Model model, String geneid, String protid) {
        final Resource gene = model.createResource(geneuri + geneid);
        gene.addProperty(RDF.type, geneuri);

        final Resource protein = model.createResource(proteinuri + protid);
        gene.addProperty(model.createProperty(geneuri + "Protein"), protein);

        return model;
    }
}
