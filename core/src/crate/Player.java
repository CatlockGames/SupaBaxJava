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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Ryan
 * @author Aaron
 *
 */
public class Player extends Entity{
	private float width = 1;
	private float height = 1;
	
	private Body body;
	
	private Texture sheet;
	//TODO Add animations
	private Animation currentAnimation;
	
	private float stateTime;
	
	private Sprite sprite;
	
	/**
	 * 
	 */
	public Player(World world, Vector2 position) {
		//Setup the animation
		sheet = new Texture(Gdx.files.internal("test.png"));
		TextureRegion[][] temp = TextureRegion.split(sheet, (int) (width * SupaBax.PPM), (int) (height * SupaBax.PPM));
		//TODO Add animations
		
		stateTime = 0;
		
		//Scale the sprite to meters
		sprite = new Sprite();
		sprite.setSize(width, height);
		sprite.setOriginCenter();
		
		//Create box2d body
		PolygonShape box = new PolygonShape();
		box.setAsBox(width / 2f, height / 2f);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		box.dispose();
	}

	@Override
	public void update(float delta) {
		
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
}
