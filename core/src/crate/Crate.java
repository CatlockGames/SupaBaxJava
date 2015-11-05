/**
 * 
 */
package crate;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * This class is the definition of a crate, the main collecting objective of the game.
 * @author Aaron
 * @author Ryan
 *
 */
public class Crate extends Entity {
	private World world;
	
	private Body body;
	private Fixture physicsFixture;
	
	private Sprite sprite;
	
	private Random random = new Random();
	
	private float width = 1f;
	private float height = 1f;
	
	private int crushCount = -1; // -1 to account for initial crate spawn
	private Vector2[] spawnLocations;
	
	private boolean spawn = false;

	/**
	 * 
	 * @param gameScreen
	 * @param spawnLocations
	 */
	public Crate(GameScreen gameScreen, Vector2[] spawnLocations) {
		super(gameScreen);
		this.world = gameScreen.getWorld();
		
		this.spawnLocations = spawnLocations;
		
		//Scale the sprite to meters
		sprite = new Sprite(new Texture(Gdx.files.internal("crate.png")));
		sprite.setSize(width, height);
		sprite.setOriginCenter();
		
		//Body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//player body shape definition
		PolygonShape physicsShape = new PolygonShape();
		physicsShape.setAsBox(width / 2f, height / 2f);
		physicsFixture = body.createFixture(physicsShape, 1f);
		physicsFixture.setUserData("cpf");
		
		physicsShape.dispose();
		
		spawn();
	}

	@Override
	public void update(float delta) {
		if(spawn){
			spawn = false;
			crushCount++;
			body.setLinearVelocity(new Vector2(0f, 0f));
			body.setTransform(spawnLocations[random.nextInt(spawnLocations.length)], 0f);
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		sprite.draw(batch);
	}

	@Override
	public void dispose() {
		sprite.getTexture().dispose();
		System.out.println("Crate disposed");
	}
	
	/**
	 * Spawns the crate at a random new location
	 */
	public void spawn(){
		spawn = true;
	}
	
	/**
	 * Gets the number of times the crate has been crushed
	 * @return
	 */
	public int getCrushCount(){
		return crushCount;
	}

}
