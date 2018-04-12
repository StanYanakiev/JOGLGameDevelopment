package hw1;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DemoGame {
	// Set this to true to make the game loop exit.
	private static boolean shouldExit;

	// The previous frame's keyboard state.
	private static boolean kbPrevState[] = new boolean[256];

	// The current frame's keyboard state.
	private static boolean kbState[] = new boolean[256];

	// Useful variables
	private static int worldWidth;
	private static int worldHeight;
	private static int screenX = 800;
	private static int screenY = 600;
	
	private static int projDir = 1;

	// Camera
	private static Camera camera = new Camera(0, 0);

	// Tile
	private static int[] tileSize = new int[2];
	private static int[] enemySize = new int[2];

	// Declare backgrounds
	private static Background backgroundGrass;

	// Declare Sprites
	// Shinjou
	private static Shinjou shinjou;
	private static float[] spritePos = new float[] { 320, 235 };
	private static int[] spriteSizeIdle = new int[2];
	private static int[] spriteSizeMoving = new int[2];

	// Slime
	private static float[] enemyPos = new float[] { 0, 0 };
	

	// Animation Initiated
	private static Animation moveLeftAnimation;
	private static Animation moveRightAnimation;
	private static Animation attackLeftAnimation;
	private static Animation attackRightAnimation;
	private static Animation idleAnimation;
	private static Animation fireAnimation;
	private static Animation fireBoarLeftAnimation;
	private static Animation fireBoarRightAnimation;
	private static Animation wildBoarLeftAnimation;
	private static Animation wildBoarRightAnimation;
	private static Animation ironBoarLeftAnimation;
	private static Animation ironBoarRightAnimation;
	// Fire
	// private static int fireTex;
	private static int[] firePos = new int[] { 200, 380 };
	private static int[] fireSize = new int[2];
	// Boar
	private static int[] boarSize = new int[2];
	private static int[] boarPos = new int[] { 0, 0 };
	// Extra
	private static boolean fireDir = true;
	// Projectile
	private static int[] projectileSize = new int[2];

	public static void main(String[] args) {
		GLProfile gl2Profile;

		try {
			// Make sure we have a recent version of OpenGL
			gl2Profile = GLProfile.get(GLProfile.GL2);
		} catch (GLException ex) {
			System.out.println("OpenGL max supported version is too low.");
			System.exit(1);
			return;
		}

		// Create the window and OpenGL context.
		GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
		window.setSize(screenX / 2, screenY / 2);
		window.setTitle("HW6");
		window.setVisible(true);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbState[keyEvent.getKeyCode()] = true;
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbState[keyEvent.getKeyCode()] = false;
			}
		});

		// Setup OpenGL state.
		window.getContext().makeCurrent();
		GL2 gl = window.getGL().getGL2();
		gl.glViewport(0, 0, screenX, screenY);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(0, screenX, screenY, 0, 0, 100);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// Game initialization goes here.
		// Initialization of backgroundTextures

		int[] backgroundTextures = { glTexImageTGAFile(gl, "data/grass.tga", tileSize),
				glTexImageTGAFile(gl, "data/wall.tga", tileSize), glTexImageTGAFile(gl, "data/water.tga", tileSize),
				glTexImageTGAFile(gl, "data/sand1.tga", tileSize), glTexImageTGAFile(gl, "data/sand2.tga", tileSize),
				glTexImageTGAFile(gl, "data/lava.tga", tileSize), glTexImageTGAFile(gl, "data/tile.tga", tileSize),
				glTexImageTGAFile(gl, "data/house1.tga", tileSize), glTexImageTGAFile(gl, "data/house2.tga", tileSize),
				glTexImageTGAFile(gl, "data/house3.tga", tileSize), glTexImageTGAFile(gl, "data/house4.tga", tileSize)};

		backgroundGrass = new Background(backgroundTextures, 30, 30);

		// make a method after in BackGround
		worldWidth = 64 * 30;
		worldHeight = 64 * 30;
		
		// Enemy and projectile texture
		int slimeGreen = glTexImageTGAFile(gl, "data/slimeGreen.tga", enemySize);
		int slimeYellow = glTexImageTGAFile(gl, "data/slimeYellow.tga", enemySize);
		int projectile = glTexImageTGAFile(gl, "data/orb.tga", projectileSize);
	

		// Animation Texture
		AnimationFrame[] idle = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleOne.tga", spriteSizeIdle), (float) 300),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleTwo.tga", spriteSizeIdle), (float) 300),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleThree.tga", spriteSizeIdle), (float) 300),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleFour.tga", spriteSizeIdle), (float) 300)};
		AnimationFrame[] movingLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftOne.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftTwo.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftThree.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftFour.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftFive.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftSix.tga", spriteSizeMoving), (float) 200) };
		AnimationFrame[] movingRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightOne.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightTwo.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightThree.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightFour.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightFive.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightSix.tga", spriteSizeMoving), (float) 200),};
		AnimationFrame[] attackLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackLeftOne.tga", spriteSizeMoving), (float) 550),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackLeftTwo.tga", spriteSizeMoving), (float) 550),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackLeftThree.tga", spriteSizeMoving), (float) 550) };
		AnimationFrame[] attackRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackRightOne.tga", spriteSizeMoving), (float) 20),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackRightTwo.tga", spriteSizeMoving), (float) 20),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackRightThree.tga", spriteSizeMoving), (float) 20) };
		AnimationFrame[] fire = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireOne.tga", fireSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireTwo.tga", fireSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireThree.tga", fireSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireFour.tga", fireSize), (float) 200), };
		AnimationFrame[] fireBoarLeft = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireBoarLeftOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireBoarLeftTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireBoarLeftThree.tga", boarSize), (float) 200), };
		AnimationFrame[] fireBoarRight = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireBoarRightOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireBoarRightTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireBoarRightThree.tga", boarSize), (float) 200), };
		AnimationFrame[] wildBoarLeft = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/wildBoarLeftOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/wildBoarLeftTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/wildBoarLeftThree.tga", boarSize), (float) 200), };
		AnimationFrame[] wildBoarRight = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/wildBoarRightOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/wildBoarRightTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/wildBoarRightThree.tga", boarSize), (float) 200), };
		AnimationFrame[] ironBoarLeft = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/ironBoarLeftOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/ironBoarLeftTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/ironBoarLeftThree.tga", boarSize), (float) 200), };
		AnimationFrame[] ironBoarRight = { 
				new AnimationFrame(glTexImageTGAFile(gl, "data/ironBoarRightOne.tga", boarSize), (float) 400),
				new AnimationFrame(glTexImageTGAFile(gl, "data/ironBoarRightTwo.tga", boarSize), (float) 400),
				new AnimationFrame(glTexImageTGAFile(gl, "data/ironBoarRightThree.tga", boarSize), (float) 400), };

		// Slimes
		ArrayList<Slime> enemies = new ArrayList<Slime>();
		enemies.add(new Slime(enemyPos[0] + 200, enemyPos[1] + 200, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 600, enemyPos[1] + 900, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 1800, enemyPos[1] + 500, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 896, enemyPos[1] + 256, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 832, enemyPos[1] + 256, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 832, enemyPos[1] + 192, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 896, enemyPos[1] + 192, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 1536, enemyPos[1] + 1728, tileSize[0], tileSize[1], slimeGreen));

		
	
		
		// Animation
		idleAnimation = new Animation(idle);
		moveLeftAnimation = new Animation(movingLeft);
		moveRightAnimation = new Animation(movingRight);
		attackLeftAnimation = new Animation(attackLeft);
		attackRightAnimation = new Animation(attackRight);
		fireAnimation = new Animation(fire);
		fireBoarLeftAnimation = new Animation(fireBoarLeft); 
		fireBoarRightAnimation = new Animation(fireBoarRight); 
		wildBoarLeftAnimation = new Animation(wildBoarLeft); 
		wildBoarRightAnimation = new Animation(wildBoarRight); 
		ironBoarLeftAnimation = new Animation(ironBoarLeft); 
		ironBoarRightAnimation = new Animation(ironBoarRight); 
		
		ArrayList<Boar> boars = new ArrayList<Boar>();
		boars.add(new Boar(enemyPos[0] + 64, enemyPos[1]  + 64, boarSize[0], boarSize[1], wildBoarRight[0].getImage(), wildBoarLeftAnimation, wildBoarRightAnimation));
		boars.add(new Boar(enemyPos[0] + 240, enemyPos[1]  + 240, boarSize[0], boarSize[1], wildBoarRight[0].getImage(), wildBoarLeftAnimation, wildBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 400, enemyPos[1]  + 400, boarSize[0], boarSize[1], ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 240, enemyPos[1]  + 800, boarSize[0], boarSize[1], ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 1000, enemyPos[1]  + 65, boarSize[0], boarSize[1], ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 129, enemyPos[1]  + 1000, boarSize[0], boarSize[1], ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new FireBoar(enemyPos[0] + 240, enemyPos[1]  + 240, boarSize[0], boarSize[1], fireBoarRight[0].getImage(), fireBoarLeftAnimation, fireBoarRightAnimation));
		boars.add(new FireBoar(enemyPos[0] + 640, enemyPos[1]  + 840, boarSize[0], boarSize[1], fireBoarRight[0].getImage(), fireBoarLeftAnimation, fireBoarRightAnimation));
		shinjou = new Shinjou(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle[0], idle[0].getImage(),
				idleAnimation, moveLeftAnimation, moveRightAnimation, attackLeftAnimation, attackRightAnimation);

		// Create a bounding box camera for both the monkey and camera
		AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle[1]);
		AABBCamera fireAABB = new AABBCamera(firePos[0], firePos[1], fireSize[0], fireSize[1]);
		AABBCamera boarAABB = new AABBCamera(boarPos[0], boarPos[1], boarSize[0], boarSize[1]);
		AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), screenX, screenY);
		// The game loop
		long lastFrameNS;
		long curFrameNS = System.nanoTime();

		// Physics runs at 100fps, or 10ms / physics frame
		 int physicsDeltaMs = 10;
		 int lastPhysicsFrameMs = 0;

		while (!shouldExit) {
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
			lastFrameNS = curFrameNS;

			// Actually, this runs the entire OS message pump.
			window.display();
			if (!window.isVisible()) {
				shouldExit = true;
				break;
			}

			// Game logic goes here.
			curFrameNS = System.nanoTime();
			long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
			float velocity = 150 * ((float) deltaTimeMS / 1000);

			ArrayList<Projectile> projList = shinjou.getProjectiles();
			// Physics Update
			do {
				// 1. Physics movement
				// 2. Physics collision detection
				// 3. Physics collision resolution

				// Shinjou Movement
				characterMove(velocity, deltaTimeMS);
				
				

				// Projectile Collision with Background
				if (projList.size() > 0) {
					for (int i = 0; i < projList.size(); i++) {
						// System.out.println("proj: " + projList.get(i).getX());
						Projectile proj = projList.get(i);
						if (backgroundGrass.getTile((float) Math.floor((proj.getX() + proj.getWidth() / 2) / 64),
								(float) Math.floor((proj.getY() + proj.getHeight() / 2) / 64)).getCollision())
							projList.remove(i);

						if (proj.getX() > worldWidth || proj.getX() < 0 || proj.getY() > worldHeight || proj.getY() < 0) {
							projList.remove(i);
						} else
							proj.update(velocity * 2);
					}
				}
				for (int i = 0; i < shinjou.getProjectiles().size(); i++) {
					for (int j = 0; j < boars.size(); j++) 
					{
						if (AABBIntersect(shinjou.getProjectiles().get(i).getCollisionBox(),
								boars.get(j).getCollisionBox())) {
							shinjou.getProjectiles().remove(i);
							boars.get(j).decHealth();
					
							if (boars.get(j).getHealth() < 1) {
								// enemies.get(j).setAlive(false);
								boars.remove(j);
							}

							if (shinjou.getProjectiles().size() == 0) 
								break;
							if (i > 0)
								i--;

						}
					}
				}
				// Projectile Collision with Enemies
				for (int i = 0; i < shinjou.getProjectiles().size(); i++) {
					for (int j = 0; j < enemies.size(); j++) 
					{
						if (AABBIntersect(shinjou.getProjectiles().get(i).getCollisionBox(),
								enemies.get(j).getCollisionBox())) {
							shinjou.getProjectiles().remove(i);
							enemies.get(j).decHealth();
							enemies.get(j).setCurrentTexture(slimeYellow);
							if (enemies.get(j).getHealth() < 1) {
								// enemies.get(j).setAlive(false);
								enemies.remove(j);
							}

							if (shinjou.getProjectiles().size() == 0) 
								break;
							if (i > 0)
								i--;

						}
					}
				}
		 
				lastPhysicsFrameMs += physicsDeltaMs;
			} while (lastPhysicsFrameMs + physicsDeltaMs < (curFrameNS /1000000) );

			if (kbState[KeyEvent.VK_ESCAPE]) {
				shouldExit = true;
			}

			
			//
			// Camera Movement
			//
			cameraMove(velocity);
			


		
			fireAnimation.updateSprite(deltaTimeMS);
			fireBoarLeftAnimation.updateSprite(deltaTimeMS);

			hunt1(deltaTimeMS, firePos);
			//hunt2(deltaTimeMS, boarPos);

			// Bullet Release
			if (kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE]) {
				Projectile proj = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
						spritePos[1] + (spriteSizeMoving[1] / 4), projectileSize[0], projectileSize[1], projDir);
				shinjou.addProjectile(proj);
				shinjou.setBusy(true);
				shinjou.setShooting(true);

			}
			if(shinjou.getShooting())
			{
				for(int i = 0; i < attackRight.length;i++) {
					shinjou.setCurrentTexture(attackRight[i].getImage());
				}
//				attackRightAnimation.updateSprite(deltaTimeMS);
//				shinjou.setCurrentTexture(attackRightAnimation.getCurrentFrame());

				shinjou.setShooting(false);
			}
			for (Boar b : boars)
			{
					
					b.update(deltaTimeMS, spritePos, backgroundGrass);
					
				}
		

			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

			// Update The AABB
			spriteAABB.setX(spritePos[0]);
			spriteAABB.setY(spritePos[1]);

			fireAABB.setX(firePos[0]);
			fireAABB.setY(firePos[1]);

			boarAABB.setX(boarPos[0]);
			boarAABB.setY(boarPos[1]);

			cameraAABB.setX(camera.getX());
			cameraAABB.setY(camera.getY());

			// Find Tiles In Camera
			int startX = (int) (camera.getX() / 64);
			int endX = (int) (camera.getX() + (screenX - 1)) / 64;
			int startY = (int) (camera.getY() / 64);
			int endY = (int) (camera.getY() + (screenY - 1)) / 64;

			// Draw background(s)
			for (int i = startX; i <= endX; i++) {
				for (int j = startY; j <= endY; j++) {
					int tileX = i * tileSize[0];
					int tileY = j * tileSize[1];
					glDrawSprite(gl, backgroundGrass.getTile(i, j).getImage(), tileX - camera.getX(),
							tileY - camera.getY(), tileSize[0], tileSize[1]);
				}
			}

			// Draw sprites
			//if (AABBIntersect(cameraAABB, fireAABB)) {
				for (Boar b : boars)
				{
					glDrawSprite(gl, b.getCurrAnimation().getCurrentFrame(), b.getX() - camera.getX(),
							b.getY() - camera.getY(), boarSize[0], boarSize[1]);
				}
			//}
				if (AABBIntersect(cameraAABB, fireAABB)) {
					glDrawSprite(gl, fireAnimation.getCurrentFrame(), firePos[0] - camera.getX(),
							firePos[1] - camera.getY(), fireSize[0], fireSize[1]);
				}
		
			for (int i = 0; i < projList.size(); i++) {
				//if (AABBIntersect(cameraAABB, projList.get(i).getCollisionBox()))
				glDrawSprite(gl, projectile, projList.get(i).getX() - camera.getX(),
						projList.get(i).getY() - camera.getY(), projectileSize[0], projectileSize[1]);
			}

			for (int i = 0; i < enemies.size(); i++) {
				//if (AABBIntersect(cameraAABB, enemies.get(i).getCollisionBox()))
					glDrawSprite(gl, enemies.get(i).getCurrentTexture(), enemies.get(i).getX() - camera.getX(),
						enemies.get(i).getY() - camera.getY(), tileSize[0], tileSize[1]);
			}
			if (AABBIntersect(spriteAABB, cameraAABB)) {
				glDrawSprite(gl, shinjou.getCurrentTexture(), spritePos[0] - camera.getX(),
						spritePos[1] - camera.getY(), spriteSizeMoving[0], spriteSizeMoving[1]);
			}
		}
		// Present to the player.
		 window.swapBuffers();
	}

	public static boolean AABBIntersect(AABBCamera box1, AABBCamera box2) {
		// box1 to the right
		if (box1.getX() > box2.getX() + box2.getWidth()) {
			return false;
		}
		// box1 to the left
		if (box1.getX() + box1.getWidth() < box2.getX()) {
			return false;
		}
		// box1 below
		if (box1.getY() > box2.getY() + box2.getHeight()) {
			return false;
		}
		// box1 above
		if (box1.getY() + box1.getHeight() < box2.getY()) {
			return false;
		}
		return true;
	}

	// Load a file into an OpenGL texture and return that texture.
	public static int glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {
		final int BPP = 4;

		DataInputStream file = null;
		try {
			// Open the file.
			file = new DataInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException ex) {
			System.err.format("File: %s -- Could not open for reading.", filename);
			return 0;
		}

		try {
			// Skip first two bytes of data we don't need.
			file.skipBytes(2);

			// Read in the image type. For our purposes the image type
			// should be either a 2 or a 3.
			int imageTypeCode = file.readByte();
			if (imageTypeCode != 2 && imageTypeCode != 3) {
				file.close();
				System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
				return 0;
			}

			// Skip 9 bytes of data we don't need.
			file.skipBytes(9);

			int imageWidth = Short.reverseBytes(file.readShort());
			int imageHeight = Short.reverseBytes(file.readShort());
			int bitCount = file.readByte();
			file.skipBytes(1);

			// Allocate space for the image data and read it in.
			byte[] bytes = new byte[imageWidth * imageHeight * BPP];

			// Read in data.
			if (bitCount == 32) {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = file.readByte();
				}
			} else {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = -1;
				}
			}

			file.close();

			// Load into OpenGL
			int[] texArray = new int[1];
			gl.glGenTextures(1, texArray, 0);
			int tex = texArray[0];
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0, GL2.GL_BGRA,
					GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

			out_size[0] = imageWidth;
			out_size[1] = imageHeight;
			return tex;
		} catch (IOException ex) {
			System.err.format("File: %s -- Unexpected end of file.", filename);
			return 0;
		}
	}

	public static void glDrawSprite(GL2 gl, int tex, float x, float y, int w, int h) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
		gl.glBegin(GL2.GL_QUADS);
		{
			gl.glColor3ub((byte) -1, (byte) -1, (byte) -1);
			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(x, y);
			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(x + w, y);
			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(x + w, y + h);
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(x, y + h);
		}
		gl.glEnd();
	}

	public static void hunt1(long deltaTimeMS, int[] pos) {
		// float v = 200 * ((float) deltaTimeMS / 1000);
		float v = (deltaTimeMS / 16) * 3;
		if (backgroundGrass.getTile((float) Math.floor((pos[0]) / 64), (float) Math.floor(pos[1] / 64))
				.getCollision()) {
			if (fireDir)
				pos[0] -= v;
			else
				pos[0] += v;
			fireDir = !fireDir;
		} else {
			if (fireDir) {
				if (pos[0] < worldWidth - fireSize[0]) {
					pos[0] += v;

				} else
					fireDir = !fireDir;
			} else {
				if (pos[0] > 0) {
					pos[0] += -v;

				} else
					fireDir = !fireDir;
			}
		}
	}

	public static void hunt2(long deltaTimeMS, int[] pos) {
		float v = 100 * ((float) deltaTimeMS / 1000);
		float deltaX;
		float deltaY;
		
		 if (pos[0] == spritePos[0] && pos[1] == spritePos[1])
		 {
			 //make a new decision
			 }
		if (pos[0] < spritePos[0]) {
			deltaX = v;
		} else {
			deltaX = -v;
		}

		if (pos[1] < spritePos[1]) {
			deltaY = v;
		} else {
			deltaY= -v;
		}
		
		// Actually move!
		 pos[0] += deltaX;
		 pos[1] += deltaY;
	}

	public static void characterMove(float velocity, long deltaTimeMS) {
		// move up
		if (kbState[KeyEvent.VK_W]) {
			if (!backgroundGrass.getTile((float) Math.floor((spritePos[0] + 15) / 64), (float) Math.floor(spritePos[1] / 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]/2) / 64), (float) Math.floor(spritePos[1]/ 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]- 15) / 64), (float) Math.floor(spritePos[1]/ 64)).getCollision() )
			{
				spritePos[1] -= velocity;
				if (spritePos[1] < 0)
					spritePos[1] = 0;
			} else
				spritePos[1] = (float) (Math.ceil(spritePos[1]/64) * 64);

				moveRightAnimation.updateSprite(deltaTimeMS);
				shinjou.setCurrentTexture(moveRightAnimation.getCurrentFrame());

			if (spritePos[1] < worldHeight - 365)
				camera.setY(spritePos[1] - 235);
			if (camera.getY() < 0)
				camera.setY(0);

			projDir = 0;
		}

		// move down
		else if (kbState[KeyEvent.VK_S]) {
			
			if (!backgroundGrass.getTile((float) Math.floor((spritePos[0] + 15)/ 64), (float) Math.floor((spritePos[1] + spriteSizeMoving[1]) / 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]/2) / 64), (float) Math.floor((spritePos[1] + spriteSizeMoving[1])/ 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]- 15) / 64), (float) Math.floor((spritePos[1] + spriteSizeMoving[1])/ 64)).getCollision() )
			{
				spritePos[1] += velocity;
				if (spritePos[1] > (worldHeight - spriteSizeMoving[1]))
					spritePos[1] = worldHeight - spriteSizeMoving[1];
			} else
				spritePos[1] = (float) (Math.floor((spritePos[1] + spriteSizeMoving[1])/64) * 64) - spriteSizeMoving[1];

				moveLeftAnimation.updateSprite(deltaTimeMS);
				shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());

			if (spritePos[1] > 235)
				camera.setY(spritePos[1] - 235);
			if (camera.getY() + 1 > tileSize[1] * backgroundGrass.getHeight() - screenY)
				camera.setY(tileSize[1] * backgroundGrass.getHeight() - screenY);
			
			projDir = 2;
		}
		// move left
		else if (kbState[KeyEvent.VK_A]) {
			if (!backgroundGrass.getTile((float) Math.floor((spritePos[0]) / 64), (float) Math.floor((spritePos[1] + 15) / 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] ) / 64), (float) Math.floor((spritePos[1] + spriteSizeMoving[1]/2 ) / 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] ) / 64), (float) Math.floor((spritePos[1] - 15 + spriteSizeMoving[1]) / 64)).getCollision() )
			{
				spritePos[0] -= velocity;
				if (spritePos[0] < 0)
					spritePos[0] = 0;
			} else
				spritePos[0] = (float) (Math.ceil(spritePos[0] / 64) * 64);
			
				moveLeftAnimation.updateSprite(deltaTimeMS);
				shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());

			if (spritePos[0] < worldWidth - 480)
				camera.setX(spritePos[0] - 320);
			if (camera.getX() - 1 < 0)
				camera.setX(0);
			
			projDir = 3;
		}
		// move right
		else if (kbState[KeyEvent.VK_D]) {
			
			if (!backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64), (float) Math.floor((spritePos[1] + 15) / 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64), (float) Math.floor((spritePos[1] + spriteSizeMoving[1]/2 ) / 64)).getCollision() 
					&& !backgroundGrass.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64), (float) Math.floor((spritePos[1] - 15 + spriteSizeMoving[1]) / 64)).getCollision() )
			{
				spritePos[0] += velocity;
				if (spritePos[0] > (worldWidth - spriteSizeMoving[0]))
					spritePos[0] = worldWidth - spriteSizeMoving[0];
			} else
				spritePos[0] = (float) (Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64) * 64
						- spriteSizeMoving[0]);
		
				moveRightAnimation.updateSprite(deltaTimeMS);
				shinjou.setCurrentTexture(moveRightAnimation.getCurrentFrame());

			if (spritePos[0] > 320)
				camera.setX(spritePos[0] - 320);
			if (camera.getX() > tileSize[0] * backgroundGrass.getWidth() - screenX)
				camera.setX(tileSize[0] * backgroundGrass.getWidth() - screenX);
			projDir = 1;
		} else {
			idleAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(idleAnimation.getCurrentFrame());
		}
	}

	public static void cameraMove(float v) {
		//
		// Camera Movement
		//

		v = v * 4;
		// move up
		if (kbState[KeyEvent.VK_UP]) {
			camera.setY(camera.getY() - v);
			if (camera.getY() < 0)
				camera.setY(0);
		}
		// move down
		else if (kbState[KeyEvent.VK_DOWN]) {
			camera.setY(camera.getY() + v);
			if (camera.getY() + 1 > tileSize[1] * backgroundGrass.getHeight() - screenY)
				camera.setY(tileSize[1] * backgroundGrass.getHeight() - screenY);
		}
		// move left
		else if (kbState[KeyEvent.VK_LEFT]) {
			camera.setX(camera.getX() - v);
			if (camera.getX() - 1 < 0)
				camera.setX(0);
		}
		// move right
		else if (kbState[KeyEvent.VK_RIGHT]) {
			camera.setX(camera.getX() + v);
			if (camera.getX() > tileSize[0] * backgroundGrass.getWidth() - screenX)
				camera.setX(tileSize[0] * backgroundGrass.getWidth() - screenX);
		}

	}
}
