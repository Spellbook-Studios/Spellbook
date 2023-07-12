package dk.sebsa.spellbook.asset.loading;

import dk.sebsa.mana.Logger;
import dk.sebsa.spellbook.asset.AssetReference;

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
 * @author sebs
 * @since 1.0.0
 */
public class ClassPathAssetProvider extends AssetProvider {
    private final Logger logger;

    public ClassPathAssetProvider(Logger logger) {
        this.logger = logger;
    }

    private final Class<ClassPathAssetProvider> clazz = ClassPathAssetProvider.class;
    private final ClassLoader cl = clazz.getClassLoader();
    private List<AssetReference> assets;

    @Override
    public List<AssetReference> getAssets() {
        assets = new ArrayList<>();

        URL dirUrl = cl.getResource("dk/sebsa/spellbook/asset");
        String protocol = dirUrl.getProtocol();

        try { // Depending on the enviroment the assets has to be loaded from an "external folder" (Often when running from IDE)
            if(protocol.equals("file")) { logger.log("IDE Support Jar Load"); importFromSketchyJar(); }
            else { logger.log("Classic Jar Load"); importFromJar();}
        } catch (IOException e) { logger.err("Error loading assets: "); e.printStackTrace(); }

        return assets;
    }

    private void importFromJar() throws IOException {
        // Loads the engine resources from a jar
        String me = clazz.getName().replace(".", "/") + ".class";
        URL dirUrl = cl.getResource(me);

        String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
        Enumeration<JarEntry> entries = jar.entries();

        while(entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if(name.startsWith("spellbook/") && !name.endsWith("/")) assets.add(new AssetReference("/" + name, AssetReference.LocationTypes.Jar));
        }

        jar.close();
    }

    private void importFromSketchyJar() throws IOException {
        List<String> streams = new ArrayList<>();
        streams.add("spellbook");

        while(!streams.isEmpty()) {
            InputStream in = cl.getResourceAsStream(streams.get(0));
            if(in == null) { logger.err("When importing assets from jar folder was not found: " + streams.get(0)); return; }

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = br.readLine()) != null) {
                logger.log(line);
                if(line.contains(".")) assets.add(new AssetReference("/" + streams.get(0) + "/" + line, AssetReference.LocationTypes.Jar));
                else streams.add(streams.get(0) + "/" + line);
            }

            in.close();
            br.close();
            streams.remove(0);
        }
    }

    @Override
    public String toString() {
        return "ClassPathAssetProvider{}";
    }
}
