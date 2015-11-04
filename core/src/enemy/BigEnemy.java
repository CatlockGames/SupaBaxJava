/**
 * 
 */
package enemy;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import crate.GameScreen;

/**
 * Definition of a big enemy, the lumbering foe that strikes fear in the player with its sheer size and health.
 * @author Aaron
 * @author Ryan
 *
 */
public class BigEnemy extends GroundEnemy {

	/**
	 * 
	 * @param gameScreen
	 */
	public BigEnemy(GameScreen gameScreen) {
		super(gameScreen, 1.5f, 1.5f);
		
		//Setup the animations
		sheet = new Texture(Gdx.files.internal("spritesheets/enemies/smallenemy.png"));
		TextureRegion[][] splitSheet = TextureRegion.split(sheet, 32, 32);

		TextureRegion[] walkFrames = new TextureRegion[11];
		for(int i = 0; i < 11; i++){
			walkFrames[i] = splitSheet[0][i];
		}
		walk = new Animation(0.1f, walkFrames);

		TextureRegion[] rageWalkFrames = new TextureRegion[11];
		for(int i = 0; i < 11; i++){
			rageWalkFrames[i] = splitSheet[1][i];
		}
		rageWalk = new Animation(0.05f, rageWalkFrames);

		currentAnimation = walk;
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
			//Must be transformed during an update step
			body.setTransform(new Vector2(12f, 18f), 0f);
			currentAnimation = rageWalk;
			reincarnate = false;
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		sprite.setRegion(currentAnimation.getKeyFrame(stateTime, true));
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
