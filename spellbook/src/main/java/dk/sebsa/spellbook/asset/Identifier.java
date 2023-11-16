package dk.sebsa.spellbook.asset;

import lombok.Getter;

@Getter
public class Identifier {
    public static final char NAMESPACE_SEPARATOR = ':';
    public static final String DEFAULT_NAMESPACE = "spellbook";

    private final String namespace;
    private final String path;

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public Identifier(String[] namespaceAndPath) {
        this.namespace = namespaceAndPath[0];
        this.path = namespaceAndPath[1];
    }

    public Identifier(String identifier) {
        this(decompose(identifier, NAMESPACE_SEPARATOR));
    }

    public static Identifier of(String identifier, char separator) {
        return new Identifier(decompose(identifier, separator));
    }

    protected static String[] decompose(String var0, char var1) {
        String[] var2 = new String[]{DEFAULT_NAMESPACE, var0};
        int var3 = var0.indexOf(var1);

        if (var3 >= 0) {
            var2[1] = var0.substring(var3 + 1);

            if (var3 >= 1) {
                var2[0] = var0.substring(0, var3);
            }
        }
        return var2;
    }

    @Override
    public String toString() {
        return namespace + NAMESPACE_SEPARATOR + path;
    }
}
