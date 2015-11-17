/**
 * 
 */
package crate;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import enemy.BigEnemy;
import enemy.GroundEnemy;
import enemy.SmallEnemy;
import weapon.Bullet;

/**
 * @author Aaron
 * @author Ryan
 *
 */
public class GameScreen implements Screen, InputProcessor {
	private SupaBax game;
	
	//to switch between debug rendering and normal rendering
	private boolean debug = true;
	
	//box2d necessities
	private Box2DDebugRenderer debugRenderer;
	private World world;
	private BodyBuilder bodyBuilder;
	
	//tmx map stuff
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	//camera and viewport
	private OrthographicCamera camera;
	private Viewport viewport;
	
	//Entities in the game
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Player player;
	private Crate crate;

	/**
	 * 
	 * @param game
	 */
	public GameScreen(SupaBax game) {
		this.game = game;
		
		//float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		
		//create the camera and setup the viewport
		camera = new OrthographicCamera();
		viewport = new FitViewport(24f, 16f, camera);
		viewport.apply();
		
		//set the initial position of the camera
		camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f + 2f, 0f);
		
		//setup box2d world
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0f, -50f), true);
		
		//load the tmx map
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("maps/map1.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1f / SupaBax.PPM);
		
		//build the box2d objects
		bodyBuilder = new BodyBuilder();
		bodyBuilder.createBodies(this);
		
		player = (Player) entities.get(1);
		crate = (Crate) entities.get(2);
	}
	
	/**
	 * Adds a new entity to the game screen.
	 * @param entity
	 */
	public void addEntity(Entity entity){
		/*
		if(entity instanceof Player){
			entities.add(0, entity);
		} else if(entity instanceof Crate){
			entities.add(1, entity);
		} else{
			entities.add(entity);
		}
		*/
		entities.add(entity);
	}
	
	/**
	 * Gets the player
	 * @return
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Gets the box2d world.
	 * @return
	 */
	public World getWorld(){
		return world;
	}
	
	/**
	 * Gets the tiled map.
	 * @return
	 */
	public TiledMap getMap(){
		return map;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		
		//Set the contact listener for the box2d world to callback to.
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				contact.resetFriction();
				
				//Prevent collision between enemies
				if(contact.getFixtureA().getUserData() == "epf" && contact.getFixtureB().getUserData() == "epf"){
					contact.setEnabled(false);
				}
				
				//Prevent collision between enemies and crate
				if((contact.getFixtureA().getUserData() == "epf" && contact.getFixtureB().getUserData() == "cpf") || (contact.getFixtureB().getUserData() == "epf" && contact.getFixtureA().getUserData() == "cpf")){
					contact.setEnabled(false);
				}
				
				//Prevent collision between player and bullets
				if((contact.getFixtureA().getUserData() == "ppf" && contact.getFixtureB().getUserData() == "bpf") || (contact.getFixtureB().getUserData() == "ppf" && contact.getFixtureA().getUserData() == "bpf")){
					contact.setEnabled(false);
				}
				if((contact.getFixtureA().getUserData() == "pgsf" && contact.getFixtureB().getUserData() == "bpf") || (contact.getFixtureB().getUserData() == "pgsf" && contact.getFixtureA().getUserData() == "bpf")){
					contact.setEnabled(false);
				}
				
				//Prevent collision between bullets
				if((contact.getFixtureA().getUserData() == "bpf" && contact.getFixtureB().getUserData() == "bpf") || (contact.getFixtureB().getUserData() == "bpf" && contact.getFixtureA().getUserData() == "bpf")){
					contact.setEnabled(false);
				}
				
				//Prevent collision between bullets and crates
				if((contact.getFixtureA().getUserData() == "bpf" && contact.getFixtureB().getUserData() == "cpf") || (contact.getFixtureB().getUserData() == "bpf" && contact.getFixtureA().getUserData() == "cpf")){
					contact.setEnabled(false);
				}
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
			
			@Override
			public void endContact(Contact contact) {
				//Collision with player and ground
				if(contact.getFixtureA().getUserData() == "pgsf" || contact.getFixtureB().getUserData() == "pgsf"){
					player.removeGroundContact();
				}
			}
			
			@Override
			public void beginContact(Contact contact) {
				//Collision with player and ground
				if(contact.getFixtureA().getUserData() == "pgsf" || contact.getFixtureB().getUserData() == "pgsf"){
					player.addGroundContact();
				}
				//Collision with player and hell object
				if((contact.getFixtureA().getUserData() == "ppf" && contact.getFixtureB().getUserData() == "hpf") || (contact.getFixtureB().getUserData() == "ppf" && contact.getFixtureA().getUserData() == "hpf")){
					//Game over
					System.out.println("Player dead");
				}
				//Collision with player and enemy
				if((contact.getFixtureA().getUserData() == "ppf" && contact.getFixtureB().getUserData() == "epf") || (contact.getFixtureB().getUserData() == "ppf" && contact.getFixtureA().getUserData() == "epf")){
					//Game over
					System.out.println("Player dead");
				}
				//Collision with player and crate
				if((contact.getFixtureA().getUserData() == "ppf" && contact.getFixtureB().getUserData() == "cpf") || (contact.getFixtureB().getUserData() == "ppf" && contact.getFixtureA().getUserData() == "cpf")){
					crate.spawn();
				}
				
				//Delete bullets if they collide with something collidable
				if(contact.getFixtureA().getUserData() == "bpf" && contact.getFixtureB().getUserData() != "cpf" && contact.getFixtureB().getUserData() != "bpf"){
					Bullet bullet = (Bullet) contact.getFixtureA().getBody().getUserData();
					bullet.scheduleDestruction();
				}
				if(contact.getFixtureB().getUserData() == "bpf" && contact.getFixtureA().getUserData() != "cpf" && contact.getFixtureA().getUserData() != "bpf"){
					Bullet bullet = (Bullet) contact.getFixtureB().getBody().getUserData();
					bullet.scheduleDestruction();
				}
				
				//Collision with enemy and wall
				if(contact.getFixtureA().getUserData() == "essf" && contact.getFixtureB().getUserData() == null){
					GroundEnemy enemy = (GroundEnemy) contact.getFixtureA().getBody().getUserData();
					enemy.changeDirection();
				}
				if(contact.getFixtureB().getUserData() == "essf" && contact.getFixtureA().getUserData() == null){
					GroundEnemy enemy = (GroundEnemy) contact.getFixtureB().getBody().getUserData();
					enemy.changeDirection();
				}
				//Collision with enemy and hell object
				if(contact.getFixtureA().getUserData() == "egsf" && contact.getFixtureB().getUserData() == "hpf"){
					GroundEnemy enemy = (GroundEnemy) contact.getFixtureA().getBody().getUserData();
					enemy.reincarnate();
				}
				if(contact.getFixtureB().getUserData() == "egsf" && contact.getFixtureA().getUserData() == "hpf"){
					GroundEnemy enemy = (GroundEnemy) contact.getFixtureB().getBody().getUserData();
					enemy.reincarnate();
				}	
				//Collision with enemy and bullet object
				if (contact.getFixtureA().getUserData() == "epf" && contact.getFixtureB().getUserData() == "bpf") {
					GroundEnemy enemy = (GroundEnemy) contact.getFixtureA().getBody().getUserData();
					Bullet bullet = (Bullet) contact.getFixtureB().getBody().getUserData();
					enemy.damage(bullet.getDamage());
				}
				if (contact.getFixtureB().getUserData() == "epf" && contact.getFixtureA().getUserData() == "bpf") {
					GroundEnemy enemy = (GroundEnemy) contact.getFixtureB().getBody().getUserData();
					Bullet bullet = (Bullet) contact.getFixtureA().getBody().getUserData();
					enemy.damage(bullet.getDamage());
				}
			}
		});
	}
	
	/**
	 * 
	 * @param delta
	 */
	public void update(float delta){
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).update(delta);
		}
		
		//Delete entities if needed
		Iterator<Entity> iterator = entities.iterator();
		while(iterator.hasNext()){
			Entity entity = iterator.next();
			if(entity.destructionScheduled()){
				entity.dispose();
				iterator.remove();
			}
		}
		
		world.step(delta, 6, 2);
		camera.update();
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!debug){
			mapRenderer.setView(camera);
			mapRenderer.render();
			
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			for(int i = 0; i < entities.size(); i++){
				entities.get(i).render(game.batch, delta);
			}
			game.batch.end();
		} else{
			debugRenderer.render(world, camera.combined);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		//dispose of all game entities
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).dispose();
		}
		world.dispose();
		debugRenderer.dispose();
		mapRenderer.dispose();
		map.dispose();
		
		System.out.println("GameScreen disposed");
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ESCAPE){
			Gdx.app.exit();
		}
		if(keycode == Input.Keys.GRAVE){
			if(debug){
				debug = false;
			} else{
				debug = true;
			}
		}
		
		//Handle player movement
		if(keycode == Input.Keys.LEFT){
			player.setMovingLeft(true);
		} else if(keycode == Input.Keys.RIGHT){
			player.setMovingRight(true);
		}
		if(keycode == Input.Keys.UP || keycode == Input.Keys.Z){
			player.setJump(true);
		}
		if(keycode == Input.Keys.X){
			player.setFiring(true);
		}
		
		//Debug enemies
		if(keycode == Input.Keys.E){
			entities.add(new SmallEnemy(this));
		}
		if(keycode == Input.Keys.R){
			entities.add(new BigEnemy(this));
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT){
			player.setMovingLeft(false);
		} else if(keycode == Input.Keys.RIGHT){
			player.setMovingRight(false);
		}
		if(keycode == Input.Keys.UP){
			player.setJump(false);
		}
		if(keycode == Input.Keys.X){
			player.setFiring(false);
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
