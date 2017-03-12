package gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 24/02/15.
 */
public class Background {

    private Vector2 position,position1, velocity;
    private GameWorld world;
    private Sprite sprite1, sprite2;

    public Background(GameWorld world) {
        this.world = world;

        sprite1 = new Sprite(AssetLoader.background);
        sprite2 = new Sprite(AssetLoader.background);
        sprite1.setAlpha(0.08f);
        sprite2.setAlpha(0.08f);
        position = new Vector2(0,0);
        position1 = new Vector2(0,AssetLoader.backgroundT.getHeight());
        velocity = new Vector2(0,-100);
    }

    public void update(float delta) {


        position.add(velocity.cpy().scl(delta));
        position1.add(velocity.cpy().scl(delta));

        sprite1.setPosition(position.x,position.y);
        sprite2.setPosition(position1.x,position1.y);

        if (sprite1.getY() <= -world.gameHeight){
            position.y = (sprite2.getY()+sprite2.getHeight());

        }
        if (sprite2.getY() <= -world.gameHeight){
            position1.y = (sprite1.getY()+sprite1.getHeight());
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        sprite1.draw(batch);
        sprite2.draw(batch);

    }
}
