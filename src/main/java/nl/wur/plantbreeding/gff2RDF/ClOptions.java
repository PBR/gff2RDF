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

package nl.wur.plantbreeding.gff2RDF;

import org.kohsuke.args4j.Option;

/**
 * Class object defining the different options available through the command
 * line.
 * @author Pierre-Yves Chibon -- py@chibon.fr
 */
public class ClOptions {

    @Option(name = "--tomato",
    usage = "Download the Tomato genome annotation (v2.31 from ITAG) and convert it to RDF")
    private boolean tomato;
    @Option(name = "--arabidopsis",
    usage = "Download the Arabidopsis genome annotation (v10 from TAIR) and convert it to RDF")
    private boolean arabidopsis;
    @Option(name = "--potato",
    usage = "Download the Potato genome annotation (v3.4 from PGSC) and convert it to RDF")
    private boolean potato;
    @Option(name = "--ppi",
    usage = "Download protein-protein information (from EBI) and convert it to RDF")
    private boolean ppi;
    @Option(name = "-h", aliases = {"--help"},
    usage = "Print the usage and exit")
    private boolean help;
    @Option(name = "--debug",
    usage = "Increase the verbosity of the output to help debugging")
    private boolean debug;
    @Option(name = "--force-download",
    usage = "Force the download of the files even if there are already found on their folder.")
    private boolean forcedl;
    @Option(name = "--force-unzip",
    usage = "Force the extraction of the files even if there are already found on their folder.")
    private boolean forceunzip;
    @Option(name = "--download-only",
    usage = "Performs only the download of the files")
    private boolean dlonly;

    @Option(name = "--folder",
    usage = "Specifies in which folder download/read the files of the annotation (by default it goes create a folder per specie).")
    private String folder;

    @Option(name = "--map",
    usage = "Specifies a genetic map to add to the model. "
            + "The file should be present in the file system, tab delimited and have four columns:"
            + " ID (used in the URI), name, chromosome, map position")
    private String map;
    
    @Option(name = "--physical-map",
    usage = "Specifies a physical map to add to the model. "
            + "The file should be present in the file system, tab delimited and have four columns:"
            + " ID (used in the URI), name, chromosome, physical position")
    private String physical_map;

    /**
     * Returns whether the help argument was set.
     * @return the boolean help.
     */
    public boolean isHelp() {
        return this.help;
    }

    /**
     * Retrieve the name of the folder
     * @return
     */
    public String getFolder(){
        if (this.folder != null && !this.folder.endsWith("/")){
            this.folder = this.folder + "/";
        }
        return this.folder;
    }
    /**
     * Returns whether the download only argument was set.
     * @return the boolean dlonly.
     */
    public boolean isDlOnly() {
        return this.dlonly;
    }

    /**
     * Returns whether the force extract only argument was set.
     * @return the boolean forceunzip.
     */
    public boolean isForceUnzip() {
        return this.forceunzip;
    }

    /**
     * Returns whether the forcedl argument was set.
     * @return the boolean forcedl.
     */
    public boolean isForceDl() {
        return this.forcedl;
    }

    /**
     * Returns whether the debug argument was set.
     * @return the boolean debug.
     */
    public boolean isDebug() {
        return this.debug;
    }

    /**
     * Returns whether the tomato argument was set.
     * @return the boolean tomato.
     */
    public boolean isTomato() {
        return this.tomato;
    }

    /**
     * Returns whether the potato argument was set.
     * @return the boolean potato.
     */
    public boolean isPotato() {
        return this.potato;
    }

    /**
     * Returns whether the arabidopsis argument was set.
     * @return the boolean arabidopsis.
     */
    public boolean isArabidopsis() {
        return this.arabidopsis;
    }

    /**
     * Returns whether the protein-protein interaction argument was set.
     * @return the boolean ppi.
     */
    public boolean isPpi() {
        return this.ppi;
    }

    /**
     * Returns the map set in the command line.
     * @return a String of the path to the map file.
     */
    public String getMap() {
        return map;
    }

    /**
     * Returns the physical map set in the command line.
     * @return a String of the path to the map file.
     */
    public String getPhysicalMap() {
        return physical_map;
    }

}
