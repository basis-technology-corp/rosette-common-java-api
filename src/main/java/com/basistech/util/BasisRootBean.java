/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
