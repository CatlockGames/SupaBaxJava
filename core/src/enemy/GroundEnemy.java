/**
 * 
 */
package enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import crate.Entity;
import crate.GameScreen;

/**
 * The abstract definition of an enemy.
 * @author Aaron
 * @author Ryan
 *
 */
public abstract class GroundEnemy extends Entity {
	protected World world;
	
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
	protected float currentHealth;
	protected float health;
	
	protected boolean reincarnate = false;

	/**
	 * 
	 * @param gameScreen
	 * @param width
	 * @param height
	 */
	public GroundEnemy(GameScreen gameScreen, float width, float height, float health) {
		super(gameScreen);
		this.world = gameScreen.getWorld();
		this.width = width;
		this.height = height;
		this.health = health;
		this.currentHealth = health;
	}
	
	public abstract void destroyBodies();
	
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
		currentHealth = health;
	}

	/**
	 * 
	 * @param damage
	 */
	public void damage(float damage) {
		currentHealth -= damage;
		if (currentHealth <= 0) {
			scheduleDestruction();
		}
	}
}
