package dk.sebsa.spellbook.asset;

import lombok.Getter;

/**
 * An identifier consists of 2 strings. A namespace and a path.
 * The namespace should be used to identify different groups of identifiers, belonging to 2 different systems.
 * e.g. "spellbook" is used by the engine for its internal identifiers, and the sandbox example project uses "sandbox"
 * The path should be unique within a namespace and refer to a specific object like a texture. e.g. path = "textures/ufo.png"
 * As a string, the identifier is, formatted as "IDENTIFIER:PATH", e.g. "spellbook:shaders/ui/defaultui.glsl"
 */
@Getter
public class Identifier {
    /**
     * The default character used to separate namespaces and paths
     */
    public static final char NAMESPACE_SEPARATOR = ':';
    /**
     * The default namespace of identifiers, this is best left unused
     */
    public static final String DEFAULT_NAMESPACE = "spellbook";

    private final String namespace;
    private final String path;

    /**
     * Creates an identifier within the namespace with the specified path
     *
     * @param namespace The namespace of the identifier
     * @param path      The path
     */
    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Creates an identifier from array. The first value being the namespace and the second the path
     *
     * @param namespaceAndPath An array at least 2 elements in size
     */
    public Identifier(String[] namespaceAndPath) {
        this.namespace = namespaceAndPath[0];
        this.path = namespaceAndPath[1];
    }

    /**
     * Creates an identifier based on the string provided seperated at the default namespace seperator (":")
     *
     * @param identifier Identifier formatted "NAMESPCE:PATH"
     */
    public Identifier(String identifier) {
        this(decompose(identifier, NAMESPACE_SEPARATOR));
    }

    /**
     * Creates an identifier with the string as the path and namespace split at the provided separator
     *
     * @param identifier Identifier formatted "NAMESPCESEPERATORPATH"
     * @param separator  The seperator used in the strings format
     * @return A new identifier matching the identifier provided
     */
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

    /**
     * Returns true if this and other, is the same object or have equal values, thereby identifying the same object
     * Returns false otherwise
     *
     * @param other Object to compare to
     * @return True if the 2 identifiers match
     */
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (!(other instanceof Identifier otherIdentifier)) return false;
        else return this.namespace.equals(otherIdentifier.namespace) && this.path.equals(otherIdentifier.path);
    }

    @Override
    public String toString() {
        return namespace + NAMESPACE_SEPARATOR + path;
    }
}
