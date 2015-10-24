/**
 * 
 */
package crate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Aaron
 *
 */
public class SmallEnemy extends Entity {
	private Body body;
	private Fixture physicsFixture;
	private Fixture sideSensorFixture;
	
	private Texture sheet;
	private Animation currentAnimation;
	
	private float stateTime;
	
	private Sprite sprite;
	
	private float width = 1;
	private float height = 1;
	
	private float speed = 4f;
	private float direction = 1f;

	/**
	 * 
	 */
	public SmallEnemy(World world) {
		//Setup the animation
		sheet = new Texture(Gdx.files.internal("spritesheets/enemies/smallenemy.png"));
		TextureRegion[][] temp = TextureRegion.split(sheet, 32, 32);

		TextureRegion[] frames = {
				temp[1][1]
		};
		currentAnimation = new Animation(0.5f, frames);
		//TODO Add animations
		stateTime = 0;
		
		//Scale the sprite to meters
		sprite = new Sprite();
		sprite.setSize(width, height);
		sprite.setOriginCenter();
		
		//Body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(12, 16));
		bodyDef.fixedRotation = true;
		//bodyDef.bullet = true;
		
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//enemy body shape definition
		PolygonShape physicsShape = new PolygonShape();
		physicsShape.setAsBox(width / 2f, height / 2f);
		physicsFixture = body.createFixture(physicsShape, 1f);
		physicsFixture.setUserData("epf");
		
		PolygonShape sideSensorShape = new PolygonShape();
		sideSensorShape.setAsBox(width / 2f + width / 12f, height / 4f);
		sideSensorFixture = body.createFixture(sideSensorShape, 0f);
		sideSensorFixture.setSensor(true);
		sideSensorFixture.setUserData("essf");
		
		physicsShape.dispose();
		sideSensorShape.dispose();
		
		physicsFixture.setFriction(0f);
	}

	@Override
	public void update(float delta) {
		//Vector2 pos = body.getPosition();
		Vector2 vel = body.getLinearVelocity();
		
		body.setLinearVelocity(direction * speed, vel.y);
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		sprite.setRegion(currentAnimation.getKeyFrame(stateTime, true));
		sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		
		sprite.draw(batch);
	}

	@Override
	public void dispose() {
		sheet.dispose();
	}
	
	/**
	 * Toggles the direction of the enemy
	 */
	public void changeDirection(){
		direction *= -1;
	}

}
