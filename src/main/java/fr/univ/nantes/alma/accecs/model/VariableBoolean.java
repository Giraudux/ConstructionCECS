package fr.univ.nantes.alma.accecs.model;

/**
 * Class which represent the boolean variable
 * 
 * @author Alexis Giraudet
 */
public class VariableBoolean extends Variable<Boolean> {
    public VariableBoolean(String name, Category category) {
        super(name, category, "BOOL", null, null, false);
    }

    public VariableBoolean(String name, Category category, Boolean defaultValue) {
        super(name, category, "BOOL", null, null, defaultValue);
    }
}
