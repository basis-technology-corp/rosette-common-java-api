/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/
package com.basistech.util;

import java.io.File;
import java.io.FileFilter;
import java.text.MessageFormat;

/**
 * This class retrieves pathnames commonly used in Basis products. It is non-static to allow for some control
 * of initialization order.
 */
public class Pathnames {

    private static String btRootDirectory;

    /**
     * Default constructor.
     */
    public Pathnames() {
    }

    /**
     * Set the Basis root directory (the root of an SDK or runtime installation). This is the parent of the
     * 'rlp' directory. Calling this overrides any setting of the 'bt.root' system property. This operation is
     * typically performed once per application. The value set the first time this is called persists for the
     * duration of the process.
     * 
     * @param path to the BT root directory
     */
    public static void setBTRootDirectory(String path) {
        btRootDirectory = path;
    }
    
    static void reset() {
        btRootDirectory = null; // tests need to put this in a known state.
    }

    static String binOrLib() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1 ? "bin" : "lib";
    }

    static String debugSuffix() {
        return System.getProperty("debug.level", "").equals("2") ? "_g" : "";
    }

    String getLibraryParentDir() {
        return getBTRootDirectory() + "/rlp/" + binOrLib() + debugSuffix();
    }

    /**
     * Returns the platform string in use by the application. If the bt.arch system property is set, use that.
     * Otherwise, if there is only one directory under btroot/rlp/{bin|lib}, use that. Otherwise, multiple
     * directories exist and IllegalStateException is thrown.
     * 
     * @return the platform string in use
     * @throws IllegalStateException when the platform is undefined
     */
    String getPlatform() {
        String s = System.getProperty("bt.arch");
        if (s == null) {
            String dir = getLibraryParentDir();
            File[] platformDirs = new File(dir).listFiles(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() & !f.isHidden();
                }
            });
            if (platformDirs == null || platformDirs.length == 0) {
                throw new IllegalStateException(MessageFormat
                    .format("\"{0}\" has no platform subdirectories.  "
                            + "This may be the result of an incorrect setting of bt.root.", dir));
            } else if (platformDirs.length == 1) {
                s = platformDirs[0].getName();
            } else {
                throw new IllegalStateException(MessageFormat
                    .format("Multiple platform subdirectories found under \"{0}\".  "
                            + "bt.arch system property is required.", dir));
            }
        } else if (s.trim().length() == 0) {
            throw new IllegalStateException("bt.arch system property cannot be blank.");
        }

        File dir = new File(getLibraryParentDir() + File.separator + s);
        if (!dir.isDirectory()) {
            throw new IllegalStateException(MessageFormat
                .format("\"{0}\" is not a directory.  "
                        + "This may be the result of an incorrect setting of bt.root or bt.arch.", dir));
        }
        return s;
    }

    /**
     * Retrieve the root of an SDK or runtime installation. This is the parent directory of the 'rlp'
     * directory. If not previously set by calling setBTRootDirectory, this will default to the value of the
     * 'bt.root' system property.
     * 
     * @return the BT root directory
     * @throws NoRootDirectoryException if no root directory has been set.
     */
    public String getBTRootDirectory() {
        if (btRootDirectory != null) {
            return btRootDirectory;
        }
        String r = System.getProperty("bt.root", null);
        if (r == null || r.length() == 0) {
            throw new NoRootDirectoryException();
        }
        return r;
    }

    /**
     * Retrieve the directory within the Basis Technology installation directory where native code libraries
     * are located.
     * 
     * @return the directory for native code libraries
     */
    public String getJNIModuleDirectory() {
        return getLibraryParentDir() + File.separator + getPlatform();
    }

}
