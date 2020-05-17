package com.afj.solution.test.mobile.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import static java.util.Objects.requireNonNull;
import static org.apache.velocity.texen.util.FileUtil.file;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class AppElementsSearcher {
    @Getter(AccessLevel.PUBLIC) private final String screenPackage;

    public AppElementsSearcher(final String screenPackage) {
        requireNonNull(screenPackage, "App Element package can not be null");

        this.screenPackage = screenPackage;
    }

    public AppElement getAppElement(final String name) {
        return getAppElement(name, null);
    }

    public AppElement getAppElement(final String elementName, final String screenName) {
        log.info(String.format("Starting search for the '%s' element on the '%s' screen", elementName, screenName));
        final Class[] classesArray = screenName == null
            ? getClasses()
            : new Class[]{getClassByName(screenName)};
        AppElement element = null;
        try {
            breakLabel:
            for (final Class classes : classesArray) {
                if (!Modifier.isAbstract(classes.getModifiers())) {
                    for (final Field field : classes.getDeclaredFields()) {
                        if (field.getType().toString().contains("com.free2move.qa.auto.utils.AppElement")
                            && field.getName().equals(elementName.toUpperCase())) {
                            field.setAccessible(true);
                            element = (AppElement) field.get(AppElement.class);
                            field.setAccessible(false);
                            break breakLabel;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.info(String.format("Expection log %s", e.getMessage()));
        }
        requireNonNull(element, String.format("Element with name %s not found", elementName));

        log.info(String.format("Element '%s' was found", elementName));
        return element;
    }

    private Class getClassByName(final String className) {
        return Arrays.stream(requireNonNull(getClasses()))
            .filter(aClass -> aClass.getSimpleName().equalsIgnoreCase(className + "Screen"))
            .findAny().orElse(null);
    }

    private Class[] getClasses() {
        try {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            final String path = screenPackage.replace('.', '/');
            Enumeration<URL> resources = null;

            resources = classLoader.getResources(path);

            final List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                final URL resource = resources.nextElement();
                dirs.add(file(resource.getFile()));
            }
            final ArrayList<Class> classes = new ArrayList<>();
            for (final File directory : dirs) {
                classes.addAll(findClasses(directory, screenPackage));
            }
            return classes.toArray(new Class[0]);
        } catch (ClassNotFoundException | IOException e) {
            log.info(String.format("Expection log %s", e.getMessage()));
            return null;
        }
    }

    private List<Class> findClasses(final File directory, final String packageName) throws ClassNotFoundException {
        final List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        final File[] files = directory.listFiles();
        for (final File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
