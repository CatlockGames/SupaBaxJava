/**
 * 
 */
package weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import crate.GameScreen;

/**
 * Definition of a pistol bullet.
 * @author Aaron
 * @author Ryan
 *
 */
public class PistolBullet extends Bullet {
	private World world;

	/**
	 * 
	 * @param gameScreen
	 * @param direction
	 */
	public PistolBullet(GameScreen gameScreen, Vector2 position, float direction) {
		super(gameScreen, direction, 0.25f, 0.25f, 1f, 1f);
		this.world = gameScreen.getWorld();
		
		//Setup the animations
		//sheet = new Texture(Gdx.files.internal("spritesheets/weapons/pistol/pistolBullet.png"));
		sheet = new Texture(Gdx.files.internal("spritesheets/player/player.png"));
		TextureRegion[][] splitSheet = TextureRegion.split(sheet, 32, 32);
		
		TextureRegion[] animationFrames = new TextureRegion[5];
		for(int i = 0; i < 5; i++){
			animationFrames[i] = splitSheet[0][i];
		}
		animation = new Animation(0.1f, animationFrames);
		
		stateTime = 0;

		//Scale the sprite to meters
		sprite = new Sprite();
		sprite.setSize(width, height);
		sprite.setOriginCenter();
		
		//Body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);

		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//bullet body shape definition
		PolygonShape physicsShape = new PolygonShape();
		physicsShape.setAsBox(width / 2f, height / 2f);
		physicsFixture = body.createFixture(physicsShape, 1f);
		physicsFixture.setUserData("bpf");
		physicsFixture.setFriction(0f);
		
		physicsShape.dispose();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		sprite.setRegion(animation.getKeyFrame(stateTime, true));
		sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		if(direction == -1f){
			sprite.setFlip(true, false);
		}
		sprite.draw(batch);
	}

	@Override
	public void dispose() {
		sheet.dispose();
	}

}
