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
	private static int currentFrameTex;
	private static int worldWidth;
	private static int worldHeight;
	
	// Camera
	private static Camera camera = new Camera(0, 0);
	// Tile
	private static int[] tileSize = new int[2];

	// Declare backgrounds
	private static Background backgroundGrass;
	
	// Declare Sprites
	// Shinjou
	private static float[] spritePos = new float[] { 320, 235 };
	private static int[] spriteSizeIdle = new int[2];
	private static int[] spriteSizeMoving = new int[2];
	
	// Animation Initiated
	private static Animation moveLeftAnimation;
	private static Animation moveRightAnimation;
	private static Animation idleAnimation;
	private static Animation fireAnimation;
	private static Animation boarAnimation;
	
	
	// Fire
	//private static int fireTex;
	private static int[] firePos = new int[] { 0, 480 };
	private static int[] fireSize = new int[2];
	// Boar
	private static int[] boarSize = new int[2];
	private static int[] boarPos = new int[] { 300, 200};
	
	// Extra
	private static boolean fireDir = true;

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
		window.setSize(800/2, 600/2);
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
		gl.glViewport(0, 0, 800, 600);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(0, 800, 600, 0, 0, 100);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// Game initialization goes here.
		
		
		// Initialization of backgroundTextures
		
		
		int[] backgroundTextures = {
				glTexImageTGAFile(gl, "data/grass.tga", tileSize),
				glTexImageTGAFile(gl, "data/wall.tga", tileSize),
				glTexImageTGAFile(gl, "data/water.tga", tileSize),
				glTexImageTGAFile(gl, "data/sand1.tga", tileSize),
				glTexImageTGAFile(gl, "data/sand2.tga", tileSize),
				glTexImageTGAFile(gl, "data/lava.tga", tileSize)
				};
		
//		backgroundGrass = new Background(backgroundGrassTex ,13, 11);
//		backgroundGrass.setImage(backgroundTreeTex, 68, 76);	
//		backgroundGrass.setImage(backgroundWaterTex, 78, 131);
		backgroundGrass = new Background(backgroundTextures ,30, 30);
//		backgroundGrass.setImage(backgroundTreeTex, 68, 76);
//		backgroundGrass.setImage(backgroundWaterTex, 78, 131);
		
		// make a method after in BackGround
		worldWidth = 64 * 30;
		worldHeight = 64 * 30;
				
		
		
		
		// Animation Texture
		AnimationFrame[] idle = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/charIdle1.tga", spriteSizeIdle), (float) 500),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charIdle2.tga", spriteSizeIdle), (float) 500),
		};
		AnimationFrame[] movingLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftOne.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftTwo.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftThree.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftFour.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftFive.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftSix.tga", spriteSizeIdle), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charLeftTwo.tga", spriteSizeIdle), (float) 100),
		};
		AnimationFrame[] movingRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightOne.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightTwo.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightThree.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightFour.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightFive.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightSix.tga", spriteSizeMoving), (float) 100),
				new AnimationFrame(glTexImageTGAFile(gl, "data/charRightTwo.tga", spriteSizeMoving), (float) 100),
		};
		AnimationFrame[] fire = {
					new AnimationFrame(glTexImageTGAFile(gl, "data/fireOne.tga", fireSize), (float) 200),
					new AnimationFrame(glTexImageTGAFile(gl, "data/fireTwo.tga", fireSize), (float) 200),
					new AnimationFrame(glTexImageTGAFile(gl, "data/fireThree.tga", fireSize), (float) 200),
					new AnimationFrame(glTexImageTGAFile(gl, "data/fireFour.tga", fireSize), (float) 200),
		};
		AnimationFrame[] boar = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/boarOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/boarTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/boarThree.tga", boarSize), (float) 200),
		};
		
		// Animation
		idleAnimation = new Animation(idle);
		moveLeftAnimation = new Animation(movingLeft);
		moveRightAnimation = new Animation(movingRight);
		fireAnimation = new Animation(fire);
		boarAnimation = new Animation(boar);

		// Create a bounding box camera for both the monkey and camera
		AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle	[1]);
		AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), 600, 800);
		// The game loop
		long lastFrameNS;
		long curFrameNS = System.nanoTime();
		
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
			// Check keyboard input for player
			// Update positions and animations of all sprites
			curFrameNS = System.nanoTime();
			long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
			
			fireAnimation.updateSprite(deltaTimeMS);
			boarAnimation.updateSprite(deltaTimeMS);
			
			hunt1(deltaTimeMS, firePos);
			hunt2(deltaTimeMS, boarPos);
			
			if (kbState[KeyEvent.VK_ESCAPE]) {
				shouldExit = true;
			}
			
			float velocity =  250 * ((float) deltaTimeMS / 1000);
//			System.out.println("Idle: " + spriteSizeIdle[0] + " , " + spriteSizeIdle[1]);
//			System.out.println("Moving: " + spriteSizeMoving[0] + " , " + spriteSizeMoving[1]);
			
		
			// move up
			if (kbState[KeyEvent.VK_W]) {
				spritePos[1] -= velocity;
				if (spritePos[1] < 0) 
					spritePos[1] = 0;
				moveRightAnimation.updateSprite(deltaTimeMS);
				currentFrameTex =  moveRightAnimation.getCurrentFrame();
			
				if (spritePos[1] < worldHeight - 365)
					camera.setY(spritePos[1] - 235);
				if (camera.getY() < 0)
					camera.setY(0);
			}

			// move down
			else if (kbState[KeyEvent.VK_S] ) {
				spritePos[1] += velocity;
				if(spritePos[1] > (worldHeight - spriteSizeMoving[1]))
					spritePos[1] = worldHeight - spriteSizeMoving[1];
				moveLeftAnimation.updateSprite(deltaTimeMS);
				currentFrameTex =  moveLeftAnimation.getCurrentFrame();
				
				if(spritePos[1] > 235)
					camera.setY(spritePos[1] - 235);
				if (camera.getY() + 1 > tileSize[1] * backgroundGrass.getHeight() - 600) 
					camera.setY(tileSize[1] * backgroundGrass.getHeight() - 600);
			}
			// move left
			else if (kbState[KeyEvent.VK_A]) {
				spritePos[0] -= velocity;
				if(spritePos[0] < 0)
					spritePos[0] = 0;
				moveLeftAnimation.updateSprite(deltaTimeMS);
				currentFrameTex =  moveLeftAnimation.getCurrentFrame();	
				
				if (spritePos[0] < worldWidth - 480)
					camera.setX(spritePos[0] - 320);

				if (camera.getX() - 1 < 0)
					camera.setX(0);
			}
			// move right
			else if (kbState[KeyEvent.VK_D]) {
				spritePos[0] += velocity;
				if(spritePos[0] > (worldWidth - spriteSizeMoving[0]))
					spritePos[0] = worldWidth - spriteSizeMoving[0];
				moveRightAnimation.updateSprite(deltaTimeMS);
				currentFrameTex =  moveRightAnimation.getCurrentFrame();
				
				if(spritePos[0] > 320)
					camera.setX(spritePos[0] - 320);
				if (camera.getX() > tileSize[0] * backgroundGrass.getWidth() - 800) 
					camera.setX(tileSize[0] * backgroundGrass.getWidth() - 800);
			}
			else 
			{
				idleAnimation.updateSprite(deltaTimeMS);
				currentFrameTex= idleAnimation.getCurrentFrame();
			}
			
			// move camera
			velocity = velocity * 4;
			// move up
			if (kbState[KeyEvent.VK_UP]) {
				camera.setY(camera.getY() - velocity);
				if (camera.getY() < 0)
					camera.setY(0);
			}
			// move down
			else if (kbState[KeyEvent.VK_DOWN]) 
			{
				camera.setY(camera.getY() + velocity);
				if (camera.getY() + 1 > tileSize[1] * backgroundGrass.getHeight() - 600) 
					camera.setY(tileSize[1] * backgroundGrass.getHeight() - 600);
			}
			// move left
			else if (kbState[KeyEvent.VK_LEFT]) 
			{
				camera.setX(camera.getX() - velocity);
				if (camera.getX() - 1 < 0)
					camera.setX(0);
			}
			// move right
			else if (kbState[KeyEvent.VK_RIGHT]) 
			{
				camera.setX(camera.getX() + velocity);
				if (camera.getX() > tileSize[0] * backgroundGrass.getWidth() - 800) 
					camera.setX(tileSize[0] * backgroundGrass.getWidth() - 800);
			}
			

			

			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

			//int check = backgroundInBounds(camera.getX(), camera.getY());
			
			// Draw background(s)
			for (int i = 0; i < backgroundGrass.getWidth(); i++) {
				for (int j = 0; j < backgroundGrass.getHeight(); j++) {

					int tileX = i * tileSize[0];
					int tileY = j * tileSize[1];
					glDrawSprite(gl, backgroundGrass.getTile(i, j), tileX - camera.getX(), tileY - camera.getY(),
							tileSize[0], tileSize[1]);
				}
			}

			//
			// Draw sprites
			glDrawSprite(gl, fireAnimation.getCurrentFrame(), firePos[0] - camera.getX(), firePos[1] - camera.getY(),
					fireSize[0], fireSize[1]);
			glDrawSprite(gl, boarAnimation.getCurrentFrame(), boarPos[0] - camera.getX(), boarPos[1] - camera.getY(),
					boarSize[0], boarSize[1]);
			glDrawSprite(gl, currentFrameTex, spritePos[0] - camera.getX(), spritePos[1] - camera.getY(),
					spriteSizeMoving[0], spriteSizeMoving[1]);
		}
		// Present to the player.
		// window.swapBuffers();
	}

//	
//	public static int backgroundInBounds(float f, float g) {
//
//		return (int) ((g / tileSize[1]) * 11 + (f / tileSize[0]));
//	}


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

	public static void hunt1(long deltaTimeMS , int[] pos) 
	{
//		float v = 200 * ((float) deltaTimeMS / 1000);
		float v =  (deltaTimeMS/ 16) * 3;
		
		if(fireDir)
		{
			if(pos[0] < worldWidth - fireSize[0])
			{
				pos[0] += v;
				
			}
			else
				fireDir = !fireDir;
		}
		else
		{
			if(pos[0] > 0 )
			{
				pos[0]+= -v;
				
			}
			else
				fireDir = !fireDir;
		}

	}
	
	public static void hunt2(long deltaTimeMS , int[] pos) 
	{
		float v = 300 * ((float) deltaTimeMS / 1000);
		if(pos[0] > 0)
		{
			pos[0] -= v;
			
		}
		else
			pos[0] = worldWidth - boarSize[0];
	}
}

