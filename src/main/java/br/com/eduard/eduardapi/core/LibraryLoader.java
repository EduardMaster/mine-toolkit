package br.com.eduard.eduardapi.core;

import kotlin.KotlinVersion;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * Loader and Downloader of Libraries used on Plugins
 */
public class LibraryLoader extends URLClassLoader {

    private final File libFile;
    /**
     * @return heck if kotlin is loaded
     */
    public boolean needLoadKotlin() {
        try {
            KotlinVersion version = KotlinVersion.CURRENT;
            return false;
        } catch (Error err) {
            return true;
        }

    }
    public LibraryLoader(ClassLoader loader, File file) {
        super(new URL[]{}, loader);
        this.libFile = file;
    }

    /**
     * Tries load Jars from Folder
     */
    public void loadLibraries() {
        File pastaLibs = libFile;
        pastaLibs.mkdirs();
        log("Starting loading libraries");
        for (File file : Objects.requireNonNull(pastaLibs.listFiles())) {
            if (file.getName().endsWith(".jar")) {
                if (!needLoadKotlin() && file.getName().toLowerCase().contains("kotlin"))
                    continue;
                try {
                    log("Loading jar: " + file.getName());
                    addURL(file.toURI().toURL());
                } catch (Exception e) {
                    log("Fail on Loading Jar: " + file.getName());
                    e.printStackTrace();
                }
            }
        }

    }

    public static void log(String msg) {
        String name = "MineToolkit JarLoader";
        System.out.println("[" + name + "] " + msg);
    }

    /**
     * Injects a JAR file into the System ClassPath.
     * <p>
     * <b>Warning:</b> This method relies on reflection and URLClassLoader,
     * which is <b>deprecated</b> and <b>blocked</b> by default since Java 16.
     * For modern Java versions (16, 17, 21, 25+), please use the 'libraries'
     * field in plugin.yml (Spigot) or bundle dependencies via ShadowJar.
     * </p>
     * * @param file The JAR file to be loaded.
     * @throws Exception If the injection fails due to security restrictions or incompatible JRE.
     * @author Eduard
     */
    public static void addCustomClassPath(final File file) throws Exception {
        String versionString = System.getProperty("java.version");
        // Handles both legacy (1.8) and modern (17, 25) version strings
        int javaVersion = Integer.parseInt(versionString.split("\\.")[0].replace("-ea", ""));

        if (javaVersion >= 16) {
            log("---------------------------------------------------------");
            log("INCOMPATIBILITY WARNING:");
            log("Java " + javaVersion + " strictly enforces module encapsulation.");
            log("Dynamic JAR injection via reflection is no longer supported.");
            log("Please use Spigot's 'libraries' field or ShadowJar relocation.");
            log("---------------------------------------------------------");
            return;
        }

        try {
            URL url = new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
            final Object systemClassLoader = ClassLoader.getSystemClassLoader();

            if (!(systemClassLoader instanceof URLClassLoader)) {
                log("System ClassLoader is not an instance of URLClassLoader. Aborting.");
                return;
            }

            final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(systemClassLoader, url);

            log("Successfully loaded library: " + file.getName());
        } catch (Exception e) {
            log("Critical failure while injecting JAR: " + e.getMessage());
        }
    }
}
