/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2000-2013 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A DataFileConfiguration specifies the locations of a set of data files.  The configuration
 * is specified in a configuration file, usually named <CODE>something.datafiles</CODE>.  Each
 * entry in the configuration file is a line of the form:
 * <PRE>    <I>key</I>&lt;tab&gt;<I>endian</I>&lt;tab&gt;<I>path</I></PRE>
 * Each <I>path</I> is a pathname relative to a root directory (usually BT_ROOT) that specifies a data file.  The
 * <I>key</I> is some arbitrary identifier used by clients to retrieve the corresponding {@link
 * File}.  See {@link #lookup(String)}.  (The <I>key</I> is often a substring of the last
 * component of the <I>path</I>, but this is not required.)
 * <P>
 * The <I>endian</I> field is used to select whether an entry applies to the current
 * architecture.  If <I>endian</I> is "*", then the entry applies to all architectures.  If
 * <I>endian</I> is "BE" or "LE", then the entry only applies on Big Endian or Little Endian
 * machines respectively.
 * <P>
 * There is one special <I>key</I>.  An entry with a <I>key</I> of "*" is a wild-card entry.
 * In a wild-card entry, the last component of the <I>path</I> must contain a single "*".  When
 * the configuration is loaded, the file system is checked for pathnames that match
 * <I>path</I>, where the single "*" matches any sequence of characters, and that sequence of
 * characters then becomes the key for that matching path.
 * <P>
 * Entries are added to the table in order.  You can use this to control whether wild-card
 * entries can override regular entries.
 */
public final class DataFileConfiguration {

    private static final String ENDIAN = Endian.getEndianString();
    private static final Map<String, DataFileConfiguration> CONFIGURATIONS =
            new ConcurrentHashMap<String, DataFileConfiguration>();

    // Currently all pathnames must be relative to a root directory.  But I'm not convinced that forcing
    // users to always put their additions inside our tree is such a great idea.  So set this
    // to true to remove the restriction.  We could make this a parameter, or introduce a
    // static method to set it.
    private static final boolean ALLOW_ABSOLUTE_PATHNAMES = false;

    private final String path;
    private final String root;

    // The actual map from keys to files:
    private final Map<String, Entry> dataFileMap;

    // For debugging purposes, track the keys that are only here because they were inserted by
    // a wild card entry:
    private final Set<String> wildKeys;

    private DataFileConfiguration(String root, String path) {
        this.path = path;
        this.root = root;
        dataFileMap = new HashMap<String, Entry>();
        wildKeys = new HashSet<String>();
        File config = new File(path);
        if (config.isAbsolute()) {
            if (!ALLOW_ABSOLUTE_PATHNAMES) {
                throw new ConfigException("Absolute pathname not allowed: " + config);
            }
        } else {
            config = new File(root, path);
        }
        try {
            for (TabReader.Line line : TabReader.iterate(config, 3, "#")) {
                String[] data = line.getData();
                loadEntries(root, data[0], data[1], data[2]);
            }
        } catch (IOException e) {
            throw new ConfigException("Problem opening: " + config, e);
        } catch (TabReader.ReadException e) {
            throw new ConfigException("Problem reading: " + config, e);
        }
    }

    /**
     * Create a DataFileConfiguration directly from a set of keys and some associated data
     * sources.  A source is anything that can be opened to return a {@link Reader}.
     * @param name a name for the resulting configuration
     * @param directory a map from keys to sources
     */
    public DataFileConfiguration(String name, Map<String, Openable> directory) {
        path = name;
        root = "";
        wildKeys = Collections.emptySet();
        dataFileMap = new HashMap<String, Entry>();
        for (Map.Entry<String, Openable> e : directory.entrySet()) {
            dataFileMap.put(e.getKey(), new Entry(e.getKey(), e.getValue()));
        }
    }

    /**
     * Obtain the data file configuration from the specified location.
     * @param root the path to the root directory (usually BT_ROOT)
     * @param path the location of the configuration file relative to the root.
     * @return the loaded configuration.
     * @throws ConfigException if the configuration was not found, or was malformed.
     */
    public static DataFileConfiguration getConfiguration(String root, String path) {
        DataFileConfiguration rv = CONFIGURATIONS.get(path);
        if (rv != null) {
            return rv;
        }
        synchronized (CONFIGURATIONS) {
            //CHECKSTYLE:OFF -- Yes, I'm using double-checked locking, but I'm using it in a
            // safe way, so checkstyle should just shut up.
            rv = CONFIGURATIONS.get(path);
            if (rv != null) {
                return rv;
            }
            rv = new DataFileConfiguration(root, path);
            // The following write to ConcurrentHashMap ensures that the fully constructed
            // instance is now seen by everybody:
            CONFIGURATIONS.put(path, rv);
            //CHECKSTYLE:ON
        }
        return rv;
    }

    private void loadEntries(String root, String key, String endian, String path) {
        if ("*".equals(endian) || endian.equals(ENDIAN)) {
            File file = new File(path);
            if (file.isAbsolute()) {
                if (!ALLOW_ABSOLUTE_PATHNAMES) {
                    throw new ConfigException("Absolute pathname not allowed: " + file);
                }
            } else {
                file = new File(root, path);
            }
            if ("*".equals(key)) {
                File dir = file.getParentFile();
                if (!dir.isDirectory()) {
                    throw new ConfigException("Not a directory: " + dir);
                }
                String name = file.getName();
                int beg = name.indexOf('*');
                if (beg < 0 || beg != name.lastIndexOf('*')) {
                    throw new ConfigException("Wildcard name must contain exactly one '*': " + name);
                }
                String prefix = name.substring(0, beg);
                String suffix = name.substring(beg + 1);
                int minLength = name.length() - 1;    // Allows empty keys
                for (String entry : dir.list()) {
                    if (entry.startsWith(prefix) && entry.endsWith(suffix) && entry.length() >= minLength) {
                        String xkey = entry.substring(beg, entry.length() - suffix.length());
                        dataFileMap.put(xkey, new Entry(xkey, new File(dir, entry)));
                        wildKeys.add(xkey);
                    }
                }
            } else {
                dataFileMap.put(key, new Entry(key, file));
                wildKeys.remove(key);
            }
        }
    }

    /**
     * Find the data that corresponds to a given key.
     *
     * @param key the key for the data you are looking for.
     * @return a {@link Reader} or null.
     */
    public Reader open(String key) throws IOException {
        Entry e = dataFileMap.get(key);
        if (e == null) {
            return null;
        }
        return e.open();
    }

    /**
     * Find the data file that corresponds to a given key.
     *
     * If the returned data file does not exist, the caller <EM>must</EM> treat this as an
     * error, since the whole point of using DataFileConfiguration is to catch errors caused by
     * missing data files!
     *
     * @param key the key for the data file you are looking for.
     * @return a {@link File} or null.
     */
    public File lookup(String key) {
        Entry e = dataFileMap.get(key);
        if (e == null) {
            return null;
        }
        return e.getFile();
    }

    /**
     * Find all the data files whose keys match the given pattern.
     *
     * If one of the returned data files does not exist, the caller <EM>must</EM> treat this as
     * an error, since the whole point of using DataFileConfiguration is to catch errors caused
     * by missing data files!
     *
     * @param keyPattern a pattern that describes the keys you are looking for.
     * @return one {@link Result} for each matching key.
     */
    public Iterable<Result> lookup(Pattern keyPattern) {
        List<Result> rv = new ArrayList<Result>();
        for (Map.Entry<String, Entry> e : dataFileMap.entrySet()) {
            Matcher m = keyPattern.matcher(e.getKey());
            if (m.matches()) {
                rv.add(new Result(m, e.getValue()));
            }
        }
        return rv;
    }

    // Think of this as a union of File and Openable.
    private final class Entry {
        final String key;
        final File file;
        final Openable openable;

        Entry(String key, File file) {
            this.key = key;
            this.file = file;
            this.openable = null;
        }

        Entry(String key, Openable openable) {
            this.key = key;
            this.openable = openable;
            this.file = null;
        }

        File getFile() {
            if (file == null) {
                throw new ConfigException(String.format("in %s the data for %s does not come from a file!",
                        path, key));
            }
            return file;
        }

        Reader open() throws IOException {
            if (file == null) {
                return openable.open();
            } else {
                return new InputStreamReader(new FileInputStream(file), "UTF-8");
            }
        }

        String getName() {
            if (file == null) {
                return String.format("data for %s from %s", key, path);
            } else {
                return String.format("file %s", file);
            }
        }
    }

    /**
     * One result from {@link DataFileConfiguration#lookup(Pattern)}.  Contains a {@link MatchResult}
     * for the matching key, and a {@link File} for the corresponding data file.
     */
    public static final class Result {
        private final MatchResult match;
        private final Entry entry;

        private Result(MatchResult match, Entry entry) {
            this.match = match;
            this.entry = entry;
        }

        public MatchResult getMatch() {
            return match;
        }

        public File getFile() {
            return entry.getFile();
        }

        public Reader open() throws IOException {
            return entry.open();
        }

        public String getName() {
            return entry.getName();
        }
    }

    /**
     * A problem occured while loading a {@link DataFileConfiguration}.
     */
    public static final class ConfigException extends RuntimeException {
        private ConfigException(String message) {
            super(message);
        }

        private ConfigException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Return the path that was used to obtain this data file configuration.
     * @return the location of the configuration file relative to a root directory.
     */
    public String getPath() {
        return path;
    }

    /**
     * Return the path to the configured root directory.
     * @return the location of the configured root directory.
     */
    public String getRoot() {
        return root;
    }

    /**
     * Return the name of the source that would be used to obtain data for this key
     * @param key the key for the data
     * @return a description of a data source
     */
    public String getName(String key) {
        Entry e = dataFileMap.get(key);
        if (e == null) {
            return "missing data for " + key;
        }
        return e.getName();
    }

    /**
     * For debugging purposes only.  Return the set of keys that are present because they were
     * inserted by a wild card entry:
     */
    public Set<String> getWildKeys() {
        return wildKeys;
    }
}
