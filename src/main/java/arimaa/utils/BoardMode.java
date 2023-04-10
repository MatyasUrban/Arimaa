package arimaa.utils;

import java.awt.Color;

public enum BoardMode {
    NONE(Color.LIGHT_GRAY),
    SWITCH(Color.GREEN),
    STEP(Color.ORANGE),
    PULL(Color.MAGENTA),
    PUSH(Color.RED);
    private final Color color;

    BoardMode(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

}
