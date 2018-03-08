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

	// Camera
	private static Camera camera = new Camera(0, 0);

	// Tile
	private static int[] tileSize = new int[2];

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
	private static Animation idleAnimation;
	private static Animation fireAnimation;
	private static Animation boarAnimation;

	// Fire
	// private static int fireTex;
	private static int[] firePos = new int[] { 0, 480 };
	private static int[] fireSize = new int[2];
	// Boar
	private static int[] boarSize = new int[2];
	private static int[] boarPos = new int[] { 300, 200 };
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
		window.setTitle("HW3");
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
				glTexImageTGAFile(gl, "data/lava.tga", tileSize), glTexImageTGAFile(gl, "data/tile.tga", tileSize) };

		backgroundGrass = new Background(backgroundTextures, 30, 30);

		// make a method after in BackGround
		worldWidth = 64 * 30;
		worldHeight = 64 * 30;
		
		// Enemy and projectile texture
		int slimeYellow = glTexImageTGAFile(gl, "data/slimeYellow.tga", tileSize);
		int projectile = glTexImageTGAFile(gl, "data/projectile.tga", projectileSize);
		int slimeGreen = glTexImageTGAFile(gl, "data/slimeGreen.tga", tileSize);

		// Animation Texture
		AnimationFrame[] idle = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/charIdle1.tga", spriteSizeIdle), (float) 500),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charIdle2.tga", spriteSizeIdle), (float) 500), };
		AnimationFrame[] movingLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftOne.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftTwo.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftThree.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftFour.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftFive.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftSix.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftTwo.tga", spriteSizeIdle), (float) 100), };
		AnimationFrame[] movingRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightOne.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightTwo.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightThree.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightFour.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightFive.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightSix.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightTwo.tga", spriteSizeMoving), (float) 100), };
		AnimationFrame[] fire = { new AnimationFrame(glTexImageTGAFile(gl, "data/fireOne.tga", fireSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireTwo.tga", fireSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireThree.tga", fireSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/fireFour.tga", fireSize), (float) 200), };
		AnimationFrame[] boar = { new AnimationFrame(glTexImageTGAFile(gl, "data/boarOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/boarTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/boarThree.tga", boarSize), (float) 200), };

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
		fireAnimation = new Animation(fire);
		boarAnimation = new Animation(boar);

		shinjou = new Shinjou(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle[0], idle[0].getImage(),
				idleAnimation, moveLeftAnimation, moveRightAnimation);

		// Create a bounding box camera for both the monkey and camera
		AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle[1]);
		AABBCamera fireAABB = new AABBCamera(firePos[0], firePos[1], fireSize[0], fireSize[1]);
		AABBCamera boarAABB = new AABBCamera(boarPos[0], boarPos[1], boarSize[0], boarSize[1]);
		AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), screenX, screenY);
		// The game loop
		long lastFrameNS;
		long curFrameNS = System.nanoTime();

		//// Physics runs at 100fps, or 10ms / physics frame
		// int physicsDeltaMs = 10;
		// int lastPhysicsFrameMs = 0;

		while (!shouldExit) {
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
			lastFrameNS = curFrameNS;

			// Actually, this runs the entire OS message pump.
			window.display();
			if (!window.isVisible()) {
				shouldExit = true;
				break;
			}

			// Physics Update
			// do {
			// // 1. Physics movement
			// // 2. Physics collision detection
			// // 3. Physics collision resolution
			//
			// lastPhysicsFrameMs += physicsDeltaMs;
			// } while (lastPhysicsFrameMs + physicsDeltaMs < curFrameNS );

			for (int i = 0; i < shinjou.getProjectiles().size(); i++) {
				for (int j = 0; j < enemies.size(); j++) {
					if (AABBIntersect(shinjou.getProjectiles().get(i).getCollisionBox(),
							enemies.get(j).getCollisionBox())) {
						shinjou.getProjectiles().remove(i);
						enemies.get(j).decHealth();
						enemies.get(j).setCurrentTexture(slimeYellow);
						if (enemies.get(j).getHealth() < 1) {
							// enemies.get(j).setAlive(false);
							enemies.remove(j);
						}

						if (shinjou.getProjectiles().size() == 0) {
							break;
						}
						if (i > 0)
							i--;

					}
				}
			}
			// Game logic goes here.
			curFrameNS = System.nanoTime();
			long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;

			if (kbState[KeyEvent.VK_ESCAPE]) {
				shouldExit = true;
			}

			// System.out.println("Idle: " + spriteSizeIdle[0] + " , " + spriteSizeIdle[1]);
			// System.out.println("Moving: " + spriteSizeMoving[0] + " , " +
			// spriteSizeMoving[1]);

			float velocity = 250 * ((float) deltaTimeMS / 1000);
			//
			// Shinjou Movement
			//
			characterMove(velocity, deltaTimeMS);
			//
			// Camera Movement
			//
			cameraMove(velocity);

			fireAnimation.updateSprite(deltaTimeMS);
			boarAnimation.updateSprite(deltaTimeMS);

			hunt1(deltaTimeMS, firePos);
			hunt2(deltaTimeMS, boarPos);

			// Bullet Release
			if (kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE]) {
				Projectile proj = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 2),
						spritePos[1] + (spriteSizeMoving[1] / 2), projectileSize[0], projectileSize[1]);
				shinjou.addProjectile(proj);
			}

			ArrayList<Projectile> projList = shinjou.getProjectiles();

			if (projList.size() > 0) {
				for (int i = 0; i < projList.size(); i++) {
					// System.out.println("proj: " + projList.get(i).getX());

					if (projList.get(i).getX() > worldHeight) {
						projList.remove(i);
					} else
						projList.get(i).update(velocity * 2);
				}
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
			// System.out.println("startX : " + startX + " || endX: " + endX);
			// System.out.println("startY : " + startY + " || endY: " + endY);

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
			if (AABBIntersect(cameraAABB, fireAABB)) {
				glDrawSprite(gl, fireAnimation.getCurrentFrame(), firePos[0] - camera.getX(),
						firePos[1] - camera.getY(), fireSize[0], fireSize[1]);
			}
			if (AABBIntersect(cameraAABB, boarAABB)) {
				glDrawSprite(gl, boarAnimation.getCurrentFrame(), boarPos[0] - camera.getX(),
						boarPos[1] - camera.getY(), boarSize[0], boarSize[1]);
			}
			if (AABBIntersect(cameraAABB, spriteAABB)) {
				glDrawSprite(gl, shinjou.getCurrentTexture(), spritePos[0] - camera.getX(),
						spritePos[1] - camera.getY(), spriteSizeMoving[0], spriteSizeMoving[1]);
			}
			// projectilePos[0] = spritePos[0];
			// projectilePos[1] = spritePos[1];
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
		}
		// Present to the player.
		// window.swapBuffers();
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

	public static void hunt2(long deltaTimeMS, int[] pos) {
		float v = 100 * ((float) deltaTimeMS / 1000);
		if (pos[0] > spritePos[0]) {
			pos[0] -= v;
		} else {
			pos[0] += v;
		}

		if (pos[1] > spritePos[1]) {
			pos[1] -= v;
		} else {
			pos[1] += v;
		}
	}

	public static void characterMove(float velocity, long deltaTimeMS) {
		// move up
		if (kbState[KeyEvent.VK_W]) {
			// Tile tile = backgroundGrass.getTile(spritePos[0] / 64, spritePos[1] / 64);
			spritePos[1] -= velocity;
			if (spritePos[1] < 0)
				spritePos[1] = 0;
			moveRightAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(moveRightAnimation.getCurrentFrame());

			if (spritePos[1] < worldHeight - 365)
				camera.setY(spritePos[1] - 235);
			if (camera.getY() < 0)
				camera.setY(0);
		}

		// move down
		else if (kbState[KeyEvent.VK_S]) {
			spritePos[1] += velocity;
			if (spritePos[1] > (worldHeight - spriteSizeMoving[1]))
				spritePos[1] = worldHeight - spriteSizeMoving[1];
			moveLeftAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());

			if (spritePos[1] > 235)
				camera.setY(spritePos[1] - 235);
			if (camera.getY() + 1 > tileSize[1] * backgroundGrass.getHeight() - screenY)
				camera.setY(tileSize[1] * backgroundGrass.getHeight() - screenY);
		}
		// move left
		else if (kbState[KeyEvent.VK_A]) {
			spritePos[0] -= velocity;
			if (spritePos[0] < 0)
				spritePos[0] = 0;

			moveLeftAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());

			if (spritePos[0] < worldWidth - 480)
				camera.setX(spritePos[0] - 320);

			if (camera.getX() - 1 < 0)
				camera.setX(0);
		}
		// move right
		else if (kbState[KeyEvent.VK_D]) {
			spritePos[0] += velocity;
			if (spritePos[0] > (worldWidth - spriteSizeMoving[0]))
				spritePos[0] = worldWidth - spriteSizeMoving[0];
			moveRightAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(moveRightAnimation.getCurrentFrame());

			if (spritePos[0] > 320)
				camera.setX(spritePos[0] - 320);
			if (camera.getX() > tileSize[0] * backgroundGrass.getWidth() - screenX)
				camera.setX(tileSize[0] * backgroundGrass.getWidth() - screenX);
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
