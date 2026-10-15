package com.BryanBecerra;

import com.badlogic.gdx.Game;

/** Application entry point shared by all platforms. */
public class Main extends Game {
    
    private Space outerSpace;
    
    @Override
    public void create() {
        outerSpace = new Space();
        //Greenfoot has the world-actor model so, space "world" holds everything
        setScreen(outerSpace);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (outerSpace != null) {
            outerSpace.dispose();
        }
    }
}