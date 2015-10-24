/**
 * 
 */
package crate;

import java.util.Random;

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
	private Fixture groundSensorFixture;
	
	private Texture sheet;
	private Animation currentAnimation;
	
	private float stateTime;
	
	private Sprite sprite;
	
	private float width = 1;
	private float height = 1;
	
	private float speed = 4f;
	private float direction = 1f;
	private float rage = 1f;
	
	private boolean reincarnate = false;

	/**
	 * 
	 */
	public SmallEnemy(World world) {
		//Setup the animation
		sheet = new Texture(Gdx.files.internal("spritesheets/enemies/smallenemy.png"));
		TextureRegion[][] temp = TextureRegion.split(sheet, 32, 32);

		TextureRegion[] frames = {
				temp[0][0], temp[0][1], temp[0][2], temp[0][3], temp[0][4], temp[0][5], 
				temp[0][6], temp[0][7], temp[0][8], temp[0][9], temp[0][10]
		};
		currentAnimation = new Animation(0.1f, frames);
		//TODO Add animations
		stateTime = 0;
		
		//Scale the sprite to meters
		sprite = new Sprite();
		sprite.setSize(width, height);
		sprite.setOriginCenter();
		
		//Body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(12f, 18f));
		bodyDef.fixedRotation = true;
		//bodyDef.bullet = true;
		
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//enemy body shape definition
		PolygonShape physicsShape = new PolygonShape();
		physicsShape.setAsBox(width / 2f, height / 2f);
		physicsFixture = body.createFixture(physicsShape, 1f);
		physicsFixture.setUserData("epf");
		physicsFixture.setFriction(0f);
		
		//side sensor definition
		PolygonShape sideSensorShape = new PolygonShape();
		sideSensorShape.setAsBox(width / 2f + width / 12f, height / 4f);
		sideSensorFixture = body.createFixture(sideSensorShape, 0f);
		sideSensorFixture.setSensor(true);
		sideSensorFixture.setUserData("essf");
		
		//ground sensor definition
		PolygonShape groundSensorShape = new PolygonShape();
		groundSensorShape.setAsBox(width / 2.5f, height / 6f, new Vector2(0f, -height / 2f), 0f);
		groundSensorFixture = body.createFixture(groundSensorShape, 0f);
		groundSensorFixture.setSensor(true);
		groundSensorFixture.setUserData("egsf");
		
		physicsShape.dispose();
		sideSensorShape.dispose();
		groundSensorShape.dispose();
		
		//Sets a random direction
		Random random = new Random();
		switch(random.nextInt(2)){
		case 0: direction = -1f; break;
		case 1: direction = 1f; break;
		}
	}

	@Override
	public void update(float delta) {
		Vector2 vel = body.getLinearVelocity();
		
		body.setLinearVelocity(speed * direction * rage, vel.y);
		
		if(reincarnate){
			rage = 2.5f;
			body.setTransform(new Vector2(12f, 18f), 0f);
			reincarnate = false;
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		sprite.setRegion(currentAnimation.getKeyFrame(stateTime, true));
		sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		if(direction == -1){
			sprite.setFlip(true, false);
		}
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
	
	/**
	 * Reincarnates the enemy as a raged enemy at the top
	 */
	public void reincarnate(){
		reincarnate = true;
	}

}
