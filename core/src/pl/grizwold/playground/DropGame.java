package pl.grizwold.playground;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.grizwold.playground.screens.MainMenuScreen;

public class DropGame extends Game {
    public final float WIDTH;
    public final float HEIGHT;
    public SpriteBatch batch;
    public BitmapFont font;

    public DropGame(float WIDTH, float HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}
