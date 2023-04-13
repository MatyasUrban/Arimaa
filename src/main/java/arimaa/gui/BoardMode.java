package arimaa.gui;

import java.awt.Color;

/**
 * The BoardMode enum represents 5 possible modes of board interactivity.
 */
public enum BoardMode {
    NONE(Color.LIGHT_GRAY, "None"),
    SWITCH(Color.GREEN, "Switch"),
    STEP(Color.ORANGE, "Step"),
    PULL(Color.MAGENTA, "Pull"),
    PUSH(Color.RED, "Push");
    private final Color color;
    private final String modeName;

    /**
     * Constructs board mode object.
     *
     * @param color Color of the selected pieces in the given mode.
     * @param modeName String name of the mode.
     */
    BoardMode(Color color, String modeName){
        this.color = color;
        this.modeName = modeName;

    }

    /**
     * Gets the color of the mode.
     *
     * @return The color of the mode.
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Gets the string name of the mode.
     *
     * @return The string name of the mode.
     */
    public String getModeName(){
        return this.modeName;
    }

}
