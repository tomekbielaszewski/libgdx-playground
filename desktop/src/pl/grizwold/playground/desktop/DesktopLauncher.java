package pl.grizwold.playground.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.grizwold.playground.DropGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 800;
        config.height = 480;
        config.title = "Droplet";

        new LwjglApplication(new DropGame(config.width, config.height), config);
    }
}
