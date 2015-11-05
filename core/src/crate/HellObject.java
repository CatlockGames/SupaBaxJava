/**
 * 
 */
package crate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This is a hell object which is used to cause the reincarnation of enemies and one way to kill the player.
 * @author Aaron
 * @author Ryan
 *
 */
public class HellObject extends Entity {
	private World world;
	
	private Body body;
	private Fixture physicsFixture;

	/**
	 * 
	 * @param gameScreen
	 * @param position
	 * @param width
	 * @param height
	 */
	public HellObject(GameScreen gameScreen, Vector2 position, float width, float height) {
		super(gameScreen);
		this.world = gameScreen.getWorld();
		
		//Body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(position);

		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		//enemy body shape definition
		PolygonShape physicsShape = new PolygonShape();
		physicsShape.setAsBox(width / 2f, height / 2f);
		physicsFixture = body.createFixture(physicsShape, 0f);
		physicsFixture.setUserData("hpf");
		
		physicsShape.dispose();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		//Invisible so no rendering needed
	}

	@Override
	public void dispose() {
		System.out.println("HellObject disposed");
	}

}
