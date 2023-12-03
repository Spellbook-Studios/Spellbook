package dk.sebsa.spellbook.data;

import dk.sebsa.spellbook.asset.Identifier;

import java.util.function.Supplier;

/**
 * Interface for retrieving / storing data
 *
 * @author sebs
 * @since 1.0.0
 */
public interface DataStore {
    /**
     * Gets the int stored at the identifier provided
     *
     * @param identifier The identifier for the data
     * @return An int
     */
    int getInt(Identifier identifier);

    /**
     * Gets the boolean stored at the identifier provided
     *
     * @param identifier The identifier for the data
     * @return A boolean
     */
    boolean getBool(Identifier identifier);

    /**
     * Gets the float stored at the identifier provided
     *
     * @param identifier The identifier for the data
     * @return A float
     */
    float getFloat(Identifier identifier);

    /**
     * Gets the double stored at the identifier provided
     *
     * @param identifier The identifier for the data
     * @return A double
     */
    double getDouble(Identifier identifier);

    /**
     * Gets the string stored at the identifier provided
     *
     * @param identifier The identifier for the data
     * @return A string
     */
    String getString(Identifier identifier);

    /**
     * Tried to deserialize the object stored at the identifier using the class as the schema
     *
     * @param identifier The identifier for the data
     * @param schema     The serializable class that of the object stored at the identifier
     * @return An object of the class schema
     */
    void getObject(Identifier identifier, Class<?> schema);


    /**
     * Gets the int stored at the identifier provided, or def if none was found
     * In case that no int was found at the provided location def will be put there instead
     *
     * @param identifier The identifier for the data
     * @param def        Default value for when no other value was stored
     * @return An int
     */
    int getOrDefaultInt(Identifier identifier, int def);

    /**
     * Gets the boolean stored at the identifier provided, or def if none was found
     * In case that no boolean was found at the provided location def will be put there instead
     *
     * @param identifier The identifier for the data
     * @param def        Default value for when no other value was stored
     * @return A boolean
     */
    boolean getOrDefaultBool(Identifier identifier, boolean def);

    /**
     * Gets the float stored at the identifier provided, or def if none was found
     * In case that no float was found at the provided location def will be put there instead
     *
     * @param identifier The identifier for the data
     * @param def        Default value for when no other value was stored
     * @return A float
     */
    float getOrDefaultFloat(Identifier identifier, float def);

    /**
     * Gets the double stored at the identifier provided, or def if none was found
     * In case that no double was found at the provided location def will be put there instead
     *
     * @param identifier The identifier for the data
     * @param def        Default value for when no other value was stored
     * @return A boolean
     */
    double getOrDefaultDouble(Identifier identifier, double def);

    /**
     * Gets the string stored at the identifier provided, or def if none was found
     * In case that no string was found at the provided location def will be put there instead
     *
     * @param identifier The identifier for the data
     * @param def        Default value for when no other value was stored
     * @return A string
     */
    String getOrDefaultString(Identifier identifier, String def);

    /**
     * Gets the object stored at the identifier provided, or def if none was found
     * In case that no object was found at the provided location the object the supplier returns will be put there instead
     * In case the object was found it will be deserialized using the schema provided
     *
     * @param identifier The identifier for the data
     * @param def        Default value for when no other value was stored
     * @param schema     The object will be deserialized with this schema
     * @return A boolean
     */
    void getOrDefaultObject(Identifier identifier, Supplier<Object> def, Class<?> schema);

    /**
     * Store an int at the provided identifier
     *
     * @param identifier The identifier for the data
     * @param i          An int
     */
    void storeInt(Identifier identifier, int i);

    /**
     * Store an boolean at the provided identifier
     *
     * @param identifier The identifier for the data
     * @param b          A boolean
     */
    void storeBool(Identifier identifier, boolean b);

    /**
     * Store a float at the provided identifier
     *
     * @param identifier The identifier for the data
     * @param f          A float
     */
    void storeFloat(Identifier identifier, float f);

    /**
     * Store a double at the provided identifier
     *
     * @param identifier The identifier for the data
     * @param d          A double
     */
    void storeDouble(Identifier identifier, double d);

    /**
     * Store a string at the provided identifier
     *
     * @param identifier The identifier for the data
     * @param s          A string
     */
    void storeString(Identifier identifier, String s);

    /**
     * Store an object at the provided identifier
     *
     * @param identifier The identifier for the data
     * @param o          A serializable object
     */
    void storeObject(Identifier identifier, Object o);
}
