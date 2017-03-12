package gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 09/02/15.
 */
public class Star {
    private Vector2 position, velocity;
    private Sprite sprite;

    private GameWorld world;
    private int type;
    private int angle, angleInc;

    public Star(GameWorld world) {
        this.world = world;
        sprite = new Sprite(AssetLoader.star);

        float size = (float) Math.random() * 10 + 10;

        sprite.setSize(size, size);
        sprite.setPosition((float) Math.random() * world.worldWidth,
                (float) Math.random() * world.worldHeight);
        position = new Vector2(sprite.getX(), sprite.getY());
        velocity = new Vector2((float) Math.random() * 100 + 100,
                (float) Math.random() * -100 - 100);

        /*if (Math.random() < 0.5f) {
            velocity.x = velocity.x * -1;
        }
        if (Math.random() < 0.5f) {
            velocity.y = velocity.y * -1;
        }*/
        type = Math.random() < 0.5f ? 1 : 0;


        sprite.setColor(world.parseColor("#FFFFFF", (float) (Math.random() * 0.5f + 0.05f)));


        //angleInc = (int) (Math.random() * 3);
        //angleInc *= Math.random() < 0.5f ? -1 : 1;

    }

    public void update(float delta) {
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
        //angle += angleInc;
        //sprite.setRotation(angle);
        position.add(velocity.cpy().scl(delta));
        sprite.setPosition(position.x, position.y);
        if (sprite.getX() < -20 || sprite.getX() > world.worldWidth + 20) {
            reset();
        }
        if (sprite.getY() < -20 || sprite.getY() > world.worldHeight + 20) {
            reset();
        }
        // }
    }

    private void reset() {
        if (Math.random() < 0.5f) {
            sprite.setPosition((float) Math.random() * world.worldWidth,
                    world.worldHeight + 10);
            position = new Vector2(sprite.getX(), sprite.getY());

        } else {
            sprite.setPosition(-10,
                    (float) (Math.random() * world.worldHeight));
            position = new Vector2(sprite.getX(), sprite.getY());
        }
        int random = (int) (Math.random() * 200 + 200);
        velocity = new Vector2(random,-random);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
        //sprite.setColor(world.parseColor("#FFFFFF", 0.5f));
        sprite.draw(batch);
        // }
    }
}
