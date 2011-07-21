/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.wur.plantbreeding.gff2RDF;

import org.kohsuke.args4j.Option;

/**
 * Class object defining the different options available through the command
 * line.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class ClOptions {

    @Option(name="--tomato",
        usage="Download the Tomato genome annotation (v2.31 from ITAG) and convert it to RDF")
    private boolean tomato;

    @Option(name="--arabidopsis",
        usage="Download the Arabidopsis genome annotation (v10 from TAIR) and convert it to RDF")
    private boolean arabidopsis;

    @Option(name="--potato",
        usage="Download the Potato genome annotation (v3.4 from PGSC) and convert it to RDF")
    private boolean potato;

    @Option(name="-h", aliases={"--help"},
        usage="Print the usage and exit")
    private boolean help;

    @Option(name="--debug",
        usage="Increase the verbosity of the output to help debugging")
    private boolean debug;

    @Option(name="--force_download",
        usage="Force the download of the files even if there are already found on their folder.")
    private boolean forcedl;

    /**
     * Returns whether the help argument was set.
     * @return the boolean help.
     */
    public boolean isHelp(){
        return this.help;
    }

    /**
     * Returns whether the forcedl argument was set.
     * @return the boolean forcedl.
     */
    public boolean isForceDl(){
        return this.forcedl;
    }

    /**
     * Returns whether the debug argument was set.
     * @return the boolean debug.
     */
    public boolean isDebug(){
        return this.debug;
    }

    /**
     * Returns whether the tomato argument was set.
     * @return the boolean tomato.
     */
    public boolean isTomato(){
        return this.tomato;
    }

    /**
     * Returns whether the potato argument was set.
     * @return the boolean potato.
     */
    public boolean isPotato(){
        return this.potato;
    }

    /**
     * Returns whether the arabidopsis argument was set.
     * @return the boolean arabidopsis.
     */
    public boolean isArabidopsis(){
        return this.arabidopsis;
    }
}
