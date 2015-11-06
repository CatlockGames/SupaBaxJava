/**
 * 
 */
package weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import crate.Entity;
import crate.GameScreen;

/**
 * Abstract definition of a bullet.
 * @author Aaron
 * @author Ryan
 *
 */
public abstract class Bullet extends Entity {
	protected Body body;
	protected Fixture physicsFixture;
	
	protected Texture sheet;
	protected Animation animation;
	protected float stateTime;
	
	protected Sprite sprite;
	
	protected float direction;
	protected float width;
	protected float height;
	
	protected float damage;
	protected float speed;
	
	private boolean destructionScheduled = false;

	/**
	 * 
	 * @param gameScreen
	 * @param width
	 * @param height
	 * @param damage
	 * @param speed
	 */
	public Bullet(GameScreen gameScreen, float width, float height, float direction, float damage, float speed) {
		super(gameScreen);
		
		this.width = width;
		this.height = height;
		this.direction = direction;
		this.damage = damage;
		this.speed = speed;
	}
	
	/**
	 * Schedules the bullet to be destroyed
	 */
	public void scheduleDestruction(){
		destructionScheduled = true;
	}
	
	/**
	 * Gets if the bullet is scheduled to be destroyed
	 * @return
	 */
	public boolean destructionScheduled(){
		return destructionScheduled;
	}
	
	public abstract void destroyBodies();

}
