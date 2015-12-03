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


/**
 * This class is provided to initialize the Basis product root directory in the Spring IoC container.
 * An application should create a bean of this type with scope='singleton' and use it to configure other Basis
 * classes. In some cases, the application will need to cite this bean in a dependsOn attribute. In all cases,
 * this class must be configured with:
 * 
 * <pre>
 * init-method='initialize'
 * </pre>
 * 
 * For example, a bean might be:
 * 
 * <pre>
 * &lt;bean id='bt-root' class='com.basistech.spring.BasisRootBean' init-method='initialize'&gt;
 * &lt;property name='rootDirectory' value='/pathname/to/my/basis-product-root'&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * Note that the rootDirectory property is optional. If you don't set it, the Pathnames class will read the
 * bt.root System property.
 * 
 * @see com.basistech.util.Pathnames
 */
public class BasisRootBean {
    private String rootDirectory;
    private Pathnames pathnames;

    /**
     * Create a placeholder; root directory is null.
     */
    public BasisRootBean() {
        rootDirectory = null;
    }

    /**
     * Initialize with the root directory.
     */
    public void initialize() {
        if (rootDirectory != null) {
            Pathnames.setBTRootDirectory(rootDirectory);
        }
        pathnames = new Pathnames();

    }

    /**
     * Get the Basis root directory.
     * 
     * @return the root directory.
     */
    public String getRootDirectory() {
        return rootDirectory;
    }

    /**
     * Set the Basis root directory.
     * 
     * @param rootDirectory the root directory.
     */
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    /**
     * Get the Pathnames object, which can be used to get the bt.root System property.
     * 
     * @return Pathnames.
     */
    public Pathnames getPathnames() {
        return pathnames;
    }

}
