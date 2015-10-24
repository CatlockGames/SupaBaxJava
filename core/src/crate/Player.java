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
 * @author Ryan
 * @author Aaron
 *
 */
public class Player extends Entity{
	private Body body;
	private Fixture bodyFixture;
	private Fixture groundSensorFixture;
	
	private Texture sheet;
	private Animation currentAnimation;
	
	private float stateTime;
	
	private Sprite sprite;
	
	//Action booleans
	private boolean movingLeft;
	private boolean movingRight;
	private boolean jump = false;
	
	private boolean grounded = false;
	
	private float width = 1;
	private float height = 1;
	
	private float maxSpeed = 9f;
	
	//private int previousDirection;
	
	/**
	 * 
	 * @param world
	 * @param position
	 */
	public Player(World world, Vector2 position) {
		//Setup the animation
		sheet = new Texture(Gdx.files.internal("tileset.png"));
		TextureRegion[][] temp = TextureRegion.split(sheet, (int) (width * SupaBax.PPM), (int) (height * SupaBax.PPM));
		
		TextureRegion[] frames = {
				temp[0][0]
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
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		bodyDef.bullet = true;
		
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//player body shape definition
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(width / 2f, height / 2f);
		bodyFixture = body.createFixture(bodyShape, 1f);
		bodyFixture.setUserData("physics fixture");
		
		//Ground sensor shape definition
		PolygonShape groundSensorShape = new PolygonShape();
		groundSensorShape.setAsBox(width / 2.5f, height / 6f, new Vector2(0f, -height / 2f), 0f);
		groundSensorFixture = body.createFixture(groundSensorShape, 0f);
		groundSensorFixture.setSensor(true);
		groundSensorFixture.setUserData("ground sensor");

		bodyShape.dispose();
		groundSensorShape.dispose();
	}

	@Override
	public void update(float delta) {
		//Get the current position and velocity for calculations
		Vector2 vel = body.getLinearVelocity();
		Vector2 pos = body.getPosition();
		
		//Cap movement
		if(Math.abs(vel.x) > maxSpeed){
			vel.x = Math.signum(vel.x) * maxSpeed;
			body.setLinearVelocity(vel);
		}
		
		//Disable friction while jumping
		if(!grounded){
			bodyFixture.setFriction(0f);
		} else{
			if(!movingLeft && !movingRight){
				bodyFixture.setFriction(100f);
			} else{
				bodyFixture.setFriction(0.2f);
			}
			//for moving platforms
			//body.applyLinearImpulse(new Vector2(0f, -20f), pos, true);
		}
		
		//Move left if below the max speed
		if(movingLeft && vel.x > -maxSpeed){
			body.applyLinearImpulse(new Vector2(-2f, 0f), pos, true);
		}
		//Move right if below the max speed
		if(movingRight && vel.x < maxSpeed){
			body.applyLinearImpulse(new Vector2(2f, 0f), pos, true);
		}
		//Jump if grounded
		if(jump){
			jump = false;
			if(grounded){
				body.applyLinearImpulse(new Vector2(0f, 19f), pos, true);
			}
		}
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
	 * 
	 * @return
	 */
	public Vector2 getPosition(){
		return body.getPosition();
	}

	/**
	 * 
	 * @param jump
	 */
	public void setJump(boolean jump){
		this.jump = jump;
	}
	
	/**
	 * 
	 * @param grounded
	 */
	public void setGrounded(boolean grounded){
		this.grounded = grounded;
	}

	/**
	 * 
	 * @param movingLeft
	 */
	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	/**
	 * 
	 * @param movingRight
	 */
	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}
}
