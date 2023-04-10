package arimaa.utils;

import java.awt.Color;

public enum BoardMode {
    NONE(Color.LIGHT_GRAY, "None"),
    SWITCH(Color.GREEN, "Switch"),
    STEP(Color.ORANGE, "Step"),
    PULL(Color.MAGENTA, "Pull"),
    PUSH(Color.RED, "Push");
    private final Color color;
    private final String modeName;

    BoardMode(Color color, String modeName){
        this.color = color;
        this.modeName = modeName;

    }

    public Color getColor(){
        return this.color;
    }
    public String getModeName(){
        return this.modeName;
    }



}
