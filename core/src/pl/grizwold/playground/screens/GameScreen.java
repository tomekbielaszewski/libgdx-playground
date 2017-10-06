package pl.grizwold.playground.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import pl.grizwold.playground.DropGame;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    private DropGame game;
    private Sound dropSound;
    private Music rainMusic;

    private Texture dropImage;
    private Texture bucketImage;

    private Vector3 touchPos = new Vector3();
    private OrthographicCamera camera;

    private Rectangle bucket;
    private Array<Rectangle> raindrops;

    private long lastDropTime;
    private int dropsGathered;

    public GameScreen(DropGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        bucket = new Rectangle();
        bucket.width = bucketImage.getWidth();
        bucket.height = bucketImage.getHeight();
        bucket.x = game.WIDTH / 2 - bucket.width / 2;
        bucket.y = 20;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 10, game.HEIGHT - 10);
        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for(Rectangle raindrop: raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - bucket.width / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucket.x -= 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucket.x += 400 * Gdx.graphics.getDeltaTime();

        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > game.WIDTH - bucket.width)
            bucket.x = game.WIDTH - bucket.width;

        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + raindrop.height < 0) iter.remove();
            if(raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();

        raindrop.width = dropImage.getWidth();
        raindrop.height = dropImage.getHeight();

        raindrop.x = MathUtils.random(0, game.WIDTH - raindrop.width);
        raindrop.y = game.HEIGHT;

        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        rainMusic.play();
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
