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

package nl.wur.plantbreeding.gff2RDF;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Main class of the project.
 *
 * Receive command line arguments and deal with them.
 * This program has no command line argument and is used and run from its IDE.
 * The potential arguments are for the moment set as variable in the main class.
 * The input and function are managed from this class.
 */
public class App {

    /**
     * This URI is the based used to create the RDF of non-standard URI.
     * For example, gene will have a type: URI/GENE#
     * they will have a position: URI/POSITION# which will have a starting
     * position: URI/POSITION#Start.
     *
     * You may change it to fit your needs.
     */
    private String uri = "http://pbr.wur.nl/";
    /** Logger used for outputing log information. */
    private static final Logger LOG = Logger.getLogger(
            ObjectToModel.class.getName());

    /**
     * Start-up function.
     * @param args command line argument given
     */
    public static void main(final String[] args) {

        ClOptions options = new ClOptions();
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(90);

        try {
            parser.parseArgument(args);

            if (options.isHelp() || args.length == 0) {
                parser.printUsage(System.err);
                return;
            }

            if (options.isArabidopsis()) {
                ArabidopsisAction aa;
                if (options.getFolder() != null
                        && !options.getFolder().isEmpty()) {
                    aa = new ArabidopsisAction(options.getFolder());
                } else {
                    aa = new ArabidopsisAction();
                }
                aa.download(options.isForceDl());
                if (!options.isDlOnly()) {
                    aa.main(options.isDebug());
                }
            }

            if (options.isPotato()) {
                PotatoAction pa;
                if (options.getFolder() != null
                        && !options.getFolder().isEmpty()) {
                    pa = new PotatoAction(options.getFolder());
                } else {
                    pa = new PotatoAction();
                }
                pa.download(options.isForceDl());
                pa.unzipFiles(options.isForceUnzip());
                if (!options.isDlOnly()) {
                    pa.main(options.isDebug());
                }
            }

            if (options.isTomato()) {
                TomatoAction ta;
                if (options.getFolder() != null
                        && !options.getFolder().isEmpty()) {
                    ta = new TomatoAction(options.getFolder());
                } else {
                    ta = new TomatoAction();
                }
                ta.download(options.isForceDl());
                if (!options.isDlOnly()) {
                    ta.main(options.isDebug());
                }
            }

            if (options.getMap() != null
                    && !options.getMap().isEmpty()) {
                MapAction ma;
                if (options.getFolder() != null
                        && !options.getFolder().isEmpty()) {
                    ma = new MapAction(options.getFolder());
                } else {
                    ma = new MapAction();
                }
                if (!options.isDlOnly()) {
                    ma.main(options.getMap(), options.isDebug());
                }
            }

            if (options.isPpi()) {
                PPIAction ppia = new PPIAction();
                ppia.download(options.isForceDl());
                if (!options.isDlOnly()) {
                    ppia.main(options.isDebug());
                }
            }

        } catch (CmdLineException e) {
            LOG.log(Level.SEVERE, "Error in the command line: {0}",
                    e.getMessage());
            System.err.println("java -jar gff2rdf2.jar [options...] arguments...");
            parser.printUsage(System.err);
            System.exit(1);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "An Error occured: {0}", e.getMessage());
            e.printStackTrace(System.err);
            System.exit(100);
            return;
        }
    }

    /**
     * Retrieve the URI set.
     * @return a String of a based URI to use in the models
     */
    public final String getUri() {
        return this.uri;
    }

    /**
     * This function download from the internet the specified file.
     * @param urlstring String of the url of the file
     * @param outputfile String of the name of the file that will be stored
     * locally
     * @param force boolean whether to download the file if it is already
     * present locally
     * @throws MalformedURLException if there is a problem in the URL.
     * @throws IOException if something goes wrong while writing the file
     */
    public static void downaloadFile(String urlstring, String outputfile, boolean force)
            throws MalformedURLException, IOException {
        System.out.println("Trying to download file: '" + outputfile
                + "' from :\n " + urlstring);
        File f = new File(outputfile);
        if (f.exists() && !force) {
            System.out.println("  -- no need");
            LOG.log(Level.FINE, "The file {0} already exists, no need to re-download it",
                    outputfile);
            return;
        }
        URL url = new URL(urlstring);
        int ByteRead, ByteWritten = 0;
        OutputStream outStream = new BufferedOutputStream(
                new FileOutputStream(outputfile));

        URLConnection uCon = url.openConnection();
        InputStream is = uCon.getInputStream();
        byte[] buf = new byte[1024];
        while ((ByteRead = is.read(buf)) != -1) {
            outStream.write(buf, 0, ByteRead);
            ByteWritten += ByteRead;
        }
        is.close();
        outStream.close();
//        URL url = new URL(urlstring);
//        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//        FileOutputStream fos = new FileOutputStream(outputfile);
//        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
    }

    /**
     * This function creates the folder of the given name if it does not already
     * exists.
     * @param foldername String of the name of the folder.
     */
    public static void makeFolder(String foldername) {
        System.out.println(
                "The files downloaded and generated will be stored in the folder "
                + foldername + " created in the current directory.");
        File f = new File(foldername);
        if (!f.exists()) {
            f.mkdir();
        } else {
            LOG.log(Level.FINE,
                    "The folder '{0}' already exists, no need to create it.",
                    foldername);
        }
    }

    /**
     * This method remove all illegal characters from a file.
     *
     * This is needed for the gene description file from Arabidopsis which
     * contains character making the generation of the RDF impossible.
     * @param filename the string of the name of the file to read.
     * @throws IOException if something goes wrong while reading/writing.
     */
    public static void cleanFile(String filename) throws IOException {
        System.out.println("Cleanning file: " + filename);
        final FileInputStream fstream = new FileInputStream(filename);
        // Get the object of DataInputStream
        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        FileWriter fstreamout = new FileWriter(filename + "-cleaned");
        BufferedWriter out = new BufferedWriter(fstreamout);

        String strline = "";
        while ((strline = br.readLine()) != null) {
            // We need to split the line as otherwise we loos all the tabs
            String[] words = strline.split("\t");
            String newline = "";
            for (String word : words) {
                word = word.replaceAll("[^\\x20-\\x7e]", "");
                if (newline.equals("")) {
                    newline = word;
                } else {
                    newline = newline + "\t" + word;
                }
            }
            out.write(newline + "\n");
        }
        out.close();
        br.close();
    }

    /**
     * This function extract a specified zip archive.
     * @param inputfile String of the url of the file
     * @param folder String of the name of the folder in which the files should
     * be extracted
     * @param force boolean whether to extract the file if it is already
     * present locally
     * @throws IOException if something goes wrong while writing the file
     */
    public static void extractZipFile(String inputfile, String folder,
            boolean force) throws IOException {
        System.out.println("Trying to extract files from: '" + inputfile);
        ZipFile zf = new ZipFile(inputfile);
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            File f = new File(folder + "/" + ze.getName());
            if (f.exists() && !force) {
                System.out.println("  -- no need");
                LOG.log(Level.FINE, "The file {0} already exists, no need to re-download it",
                        folder + "/" + ze.getName());
                return;
            }
            System.out.println("  Unzipping: " + folder + "/" + ze.getName());
            FileOutputStream fout = new FileOutputStream(folder + "/" + ze.getName());
            InputStream in = zf.getInputStream(ze);
            for (int c = in.read(); c != -1; c = in.read()) {
                fout.write(c);
            }
            in.close();
            fout.close();
        }
    }
}
