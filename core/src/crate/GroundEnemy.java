/**
 * 
 */
package crate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * @author Aaron
 *
 */
public abstract class GroundEnemy extends Entity {
	protected Body body;
	protected Fixture physicsFixture;
	protected Fixture sideSensorFixture;
	protected Fixture groundSensorFixture;
	
	protected Texture sheet;
	protected Animation currentAnimation;
	protected Animation walk;
	protected Animation rageWalk;
	
	protected float stateTime;
	
	protected Sprite sprite;
	
	protected float width;
	protected float height;
	
	protected float speed = 4f;
	protected float direction = 1f;
	protected float rage = 1f;
	
	protected boolean reincarnate = false;

	/**
	 * 
	 */
	public GroundEnemy(float width, float height) {
		this.width = width;
		this.height = height;
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
