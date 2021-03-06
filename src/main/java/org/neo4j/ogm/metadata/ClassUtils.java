/*
 * Copyright (c)  [2011-2015] "Neo Technology" / "Graph Aware Ltd."
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.neo4j.ogm.metadata;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author Vince Bickers
 * @author Luanne Misquitta
 */
public abstract class ClassUtils {

    @SuppressWarnings("serial")
    private static final Map<String, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap<String, Class<?>>() {{
        put("Z", Boolean.TYPE);
        put("B", Byte.TYPE);
        put("C", Character.TYPE);
        put("D", Double.TYPE);
        put("F", Float.TYPE);
        put("I", Integer.TYPE);
        put("J", Long.TYPE);
        put("S", Short.TYPE);
    }};

    /**
     * Return the reified class for the parameter of a parameterised setter or field from the parameter signature
     *
     * @param descriptor parameter descriptor
     * @return reified class for the parameter
     */
    public static Class<?> getType(String descriptor) {

        int p = descriptor.indexOf("(");
        int q = descriptor.indexOf(")");

        if (!descriptor.contains("[")) {
            if (descriptor.endsWith(";)V")) {
                q--;
            }
            if (descriptor.startsWith("(L")) {
                p++;
            }
            if(descriptor.startsWith("L")) { //handles descriptors of the format Ljava/lang/Byte;
                p++;
                q = descriptor.length()-1;
            }
        }
        if(descriptor.startsWith("[")) { //handles descriptors of the format [F
            p = 0;
            q = 2;
        }
        if(descriptor.startsWith("[L")) { //handles descriptors of the format [Ljava/lang/Float;
            p = 1;
            q = descriptor.length()-1;
        }
        if(descriptor.length()==1) { //handles descriptors of the format I
                q=1;
        }
        String typeName = descriptor.substring(p + 1, q).replace("/", ".");
        if (typeName.length() == 1) {
            return PRIMITIVE_TYPE_MAP.get(typeName);
        }

        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a list of unique elements on the classpath as File objects, preserving order.
     * Classpath elements that do not exist are not returned.
     * @param classPaths classpaths to be included
     * @return {@link List} of unique {@link File} objects on the classpath
     */
    public static ArrayList<File> getUniqueClasspathElements(List<String> classPaths) {
        ArrayList<File> pathFiles = new ArrayList<>();
        for(String classPath : classPaths) {
            try {
                Enumeration<URL> resources = ClassUtils.class.getClassLoader().getResources(classPath.replace(".","/"));
                while(resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    if(resource.getProtocol().equals("file")) {
                        pathFiles.add(new File(resource.toURI()));
                    }
                    else if(resource.getProtocol().equals("jar")) {
                        String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));  //Strip out the jar protocol
                        pathFiles.add(new File(jarPath));

                    }
                }
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return pathFiles;
    }

}
