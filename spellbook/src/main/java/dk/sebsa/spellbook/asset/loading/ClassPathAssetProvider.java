package dk.sebsa.spellbook.asset.loading;

import dk.sebsa.spellbook.asset.Identifier;
import lombok.CustomLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Loads assets from within the jar located under spellbook/
 *
 * @author sebs
 * @since 1.0.0
 */
@CustomLog
public class ClassPathAssetProvider extends AssetProvider {
    private final Class<ClassPathAssetProvider> clazz = ClassPathAssetProvider.class;
    private final ClassLoader cl = clazz.getClassLoader();
    private List<AssetLocation> assets;
    private final String namespace;

    @Override
    public String toString() {
        return "ClassPathAssetProvider{" +
                "namespace='" + namespace + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    private final String path;

    /**
     * @param namespace The namespace of the assets that are loaded
     * @param path      Path to load assets from e.g. "spellbook/"
     */
    public ClassPathAssetProvider(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    @Override
    public List<AssetLocation> getAssets() {
        assets = new ArrayList<>();

        URL dirUrl = cl.getResource("dk/sebsa/spellbook/asset");
        assert dirUrl != null;
        String protocol = dirUrl.getProtocol();

        try { // Depending on the enviroment the assets has to be loaded from an "external folder" (Often when running from IDE (ECLIPSE))
            if (protocol.equals("file")) {
                logger.log("IDE Support Jar Load");
                importFromSketchyJar();
            } else {
                logger.log("Classic Jar Load");
                importFromJar();
            }
        } catch (IOException e) {
            logger.err("Error loading assets: ");
            logger.stackTrace(e);
        }

        return assets;
    }

    private void importFromJar() throws IOException {
        // Loads the engine resources from a jar
        String me = clazz.getName().replace(".", "/") + ".class";
        URL dirUrl = cl.getResource(me);

        assert dirUrl != null; // Cant find itself???
        String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();

            if (name.startsWith(path) && !name.endsWith("/")) {
                Identifier id = new Identifier(namespace, name.split(path)[1]);
                assets.add(new AssetLocation(id, name, AssetLocation.LocationTypes.Jar));
            }
        }

        jar.close();
    }

    private void importFromSketchyJar() throws IOException {
        List<String> streams = new ArrayList<>();
        String pathNoSlash = path.replace("/", "");
        streams.add(pathNoSlash);

        while (!streams.isEmpty()) {
            InputStream in = cl.getResourceAsStream(streams.get(0));
            if (in == null) {
                logger.err("When importing assets from jar folder was not found: " + streams.get(0));
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = br.readLine()) != null) {
                logger.log(line);
                if (line.contains(".")) { // TODO: INSTALL ECLIPSE AND TEST / REIMPLEMENT AGAAAAIN
                    assets.add(new AssetLocation(null, streams.get(0) + "/" + line, AssetLocation.LocationTypes.Jar));
                } else streams.add(streams.get(0) + "/" + line);
            }

            in.close();
            br.close();
            streams.remove(0);
        }
    }
}
