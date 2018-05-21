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
	private static int score = 0;
	private static int enemiesLeft = 0;
	public static GL2 gl;
	public static FontDef font;
	private static int blinkCount = 0;
	public static long deltaTimeMS;

	// Camera
	private static Camera camera = new Camera(0, 0); // 200,250

	// Tile
	// Declare backgrounds
	private static Background backgroundLevelOne;
	private static Background backgroundLevelTwo;
	private static int[] tileSize = new int[2];

	// Declare Sprites
	// Shinjou
	private static Shinjou shinjou;
	private static float[] spritePos = new float[] { 160, 192 }; // 550, 550
	private static int[] spriteSizeIdle = new int[2];
	private static int[] spriteSizeMoving = new int[2];

	// Slime
	private static int[] enemySize = new int[2];
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
	private static Animation frostBoarLeftAnimation;
	private static Animation frostBoarRightAnimation;
	private static Animation shooterRightAnimation;
	private static Animation shooterLeftAnimation;
	private static Animation shooterAttackLeftAnimation;
	private static Animation shooterAttackRightAnimation;
	private static Animation blueKnightLeftAnimation;
	private static Animation blueKnightRightAnimation;
	private static Animation blueKnightAttackLeftAnimation;
	private static Animation blueKnightAttackRightAnimation;
	private static Animation darkKnightLeftAnimation;
	private static Animation darkKnightRightAnimation;
	private static Animation darkKnightAttackLeftAnimation;
	private static Animation darkKnightAttackRightAnimation;
	

	// Fire
	private static int[] firePos = new int[] { 200, 380 };
	private static int[] fireSize = new int[2];
	// Boar
	private static int[] boarSize = new int[2];
	private static int[] boarPos = new int[] { 0, 0 };
	private static int[] shooterSize = new int[2];
	private static int[] shooterAttackSize = new int[2];
	private static int[] knightSize = new int[2];
	private static int[] knightAttackSize = new int[2];
	// Shooter

	// Extra
	private static boolean fireDir = true;
	// Projectile
	public static int[] projectileSizeH = new int[2];
	public static int[] projectileSizeV = new int[2];
	public static int[] knightSpearSize = new int[2];
	public static int[] projectileQuiver = new int[2];
	public static int fireQuiver;
	public static ArrayList<FireQuiver> quiverList = new ArrayList<>();

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
		window.setTitle("Final Screen");
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
		gl = window.getGL().getGL2();
		gl.glViewport(0, 0, screenX, screenY);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(0, screenX, screenY, 0, 0, 100);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Game initialization goes
		// here ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		// Initialization of backgroundTexturesLevelOne
		int[] backgroundTexturesLevelOne = { glTexImageTGAFile(gl, "data/grass.tga", tileSize),
				glTexImageTGAFile(gl, "data/wall.tga", tileSize), glTexImageTGAFile(gl, "data/water.tga", tileSize),
				glTexImageTGAFile(gl, "data/sand1.tga", tileSize), glTexImageTGAFile(gl, "data/sand2.tga", tileSize),
				glTexImageTGAFile(gl, "data/lava.tga", tileSize), glTexImageTGAFile(gl, "data/tile.tga", tileSize),
				glTexImageTGAFile(gl, "data/house1.tga", tileSize), glTexImageTGAFile(gl, "data/house2.tga", tileSize),
				glTexImageTGAFile(gl, "data/house3.tga", tileSize), glTexImageTGAFile(gl, "data/house4.tga", tileSize),
				glTexImageTGAFile(gl, "data/grassFlower.tga", tileSize), };
		backgroundLevelOne = new Background(backgroundTexturesLevelOne, 30, 30, 1);

		// 13 = stoneWall2
		// 14= redGround
		// 15 = rockGround
		// 16 = rockwater
		// 17 = crackedwater
		// 18 water
		// 19-22 = curveWater
		int[] backgroundTexturesLevelTwo = { glTexImageTGAFile(gl, "data/ground1.tga", tileSize),
				glTexImageTGAFile(gl, "data/ground2.tga", tileSize),
				glTexImageTGAFile(gl, "data/ground3.tga", tileSize),
				glTexImageTGAFile(gl, "data/grassGround1.tga", tileSize),
				glTexImageTGAFile(gl, "data/grassGround2.tga", tileSize),
				glTexImageTGAFile(gl, "data/grassGround3.tga", tileSize),
				glTexImageTGAFile(gl, "data/wall1.tga", tileSize), glTexImageTGAFile(gl, "data/water1.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterEdge1.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterEdge2.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterEdge3.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterEdge4.tga", tileSize),
				glTexImageTGAFile(gl, "data/wall3.tga", tileSize), glTexImageTGAFile(gl, "data/wall4.tga", tileSize),
				glTexImageTGAFile(gl, "data/grassGround5.tga", tileSize),
				glTexImageTGAFile(gl, "data/grassGround5.tga", tileSize),
				glTexImageTGAFile(gl, "data/water2.tga", tileSize), glTexImageTGAFile(gl, "data/water3.tga", tileSize),
				glTexImageTGAFile(gl, "data/water4.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterCurve1.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterCurve2.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterCurve3.tga", tileSize),
				glTexImageTGAFile(gl, "data/waterCurve4.tga", tileSize), };
		backgroundLevelTwo = new Background(backgroundTexturesLevelTwo, 30, 30, 2);

		// Make a method after in BackGround
		worldWidth = 64 * 30;
		worldHeight = 64 * 30;

		// Slime Texture
		int slimeGreen = glTexImageTGAFile(gl, "data/slimeGreen.tga", enemySize);
		int slimeYellow = glTexImageTGAFile(gl, "data/slimeYellow.tga", enemySize);
		int mushroomOne = glTexImageTGAFile(gl, "data/blueShroomOne.tga", enemySize);
		int mushroomTwo = glTexImageTGAFile(gl, "data/blueShroomTwo.tga", enemySize);

		// Health and Score Texture
		int[] healthSize = new int[2];
		int[] scoreSize = new int[2];
		int[] enemiesLeftSize = new int[2];
		int[] lockedSize = new int[2];
		int[] startSreenSize = { screenX, screenY };
		int startScreen = glTexImageTGAFile(gl, "data/startScreen.tga", startSreenSize);
		int healthTex = glTexImageTGAFile(gl, "data/health.tga", healthSize);
		int scoreTex = glTexImageTGAFile(gl, "data/score.tga", scoreSize);
		int enemiesLeftTex = glTexImageTGAFile(gl, "data/eLeft.tga", enemiesLeftSize);
		int lockedTex = glTexImageTGAFile(gl, "data/locked.tga", lockedSize);
		AABBCamera lockedBox = new AABBCamera(1860, 1600, 100, 192);
		fireQuiver = glTexImageTGAFile(gl, "data/fireQuiver.tga", projectileQuiver);

		// Glyphs and Font
		GlyphDef[] glyphList = { new GlyphDef("data/zero.tga", 33, "0"), new GlyphDef("data/one.tga", 22, "1"),
				new GlyphDef("data/two.tga", 31, "2"), new GlyphDef("data/three.tga", 34, "3"),
				new GlyphDef("data/four.tga", 35, "4"), new GlyphDef("data/five.tga", 31, "5"),
				new GlyphDef("data/six.tga", 34, "6"), new GlyphDef("data/seven.tga", 31, "7"),
				new GlyphDef("data/eight.tga", 32, "8"), new GlyphDef("data/nine.tga", 33, "9") };

		font = new FontDef(glyphList);

		// Animation Texture
		// Character
		AnimationFrame[] idle = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleOne.tga", spriteSizeIdle), (float) 300),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleTwo.tga", spriteSizeIdle), (float) 300),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleThree.tga", spriteSizeIdle), (float) 300),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteIdleFour.tga", spriteSizeIdle), (float) 300) };
		AnimationFrame[] movingLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftOne.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftTwo.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftThree.tga", spriteSizeMoving),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftFour.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftFive.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveLeftSix.tga", spriteSizeMoving),
						(float) 200) };
		AnimationFrame[] movingRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightOne.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightTwo.tga", spriteSizeMoving), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightThree.tga", spriteSizeMoving),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightFour.tga", spriteSizeMoving),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightFive.tga", spriteSizeMoving),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteMoveRightSix.tga", spriteSizeMoving),
						(float) 200), };
		AnimationFrame[] attackLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackLeftOne.tga", spriteSizeMoving),
						(float) 150),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackLeftTwo.tga", spriteSizeMoving),
						(float) 150),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackLeftThree.tga", spriteSizeMoving),
						(float) 150) };
		AnimationFrame[] attackRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackRightOne.tga", spriteSizeMoving),
						(float) 150),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackRightTwo.tga", spriteSizeMoving),
						(float) 150),
				new AnimationFrame(glTexImageTGAFile(gl, "data/spriteAttackRightThree.tga", spriteSizeMoving),
						(float) 150) };

		// Level One Shooter
		AnimationFrame[] shooterLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftOne.tga", shooterSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftTwo.tga", shooterSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftThree.tga", shooterSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftFour.tga", shooterSize), (float) 200) };
		AnimationFrame[] shooterRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightOne.tga", shooterSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightTwo.tga", shooterSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightThree.tga", shooterSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightFour.tga", shooterSize), (float) 200) };
		AnimationFrame[] shooterAttackLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftFive.tga", shooterAttackSize),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftSix.tga", shooterAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterLeftSeven.tga", shooterAttackSize),
						(float) 200), };
		AnimationFrame[] shooterAttackRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightFive.tga", shooterAttackSize),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightSix.tga", shooterAttackSize),
						(float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/badShooterRightSeven.tga", shooterAttackSize),
						(float) 200), };

		// Level Two Shooters
		AnimationFrame[] blueKnightLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightLeftOne.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightLeftTwo.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightLeftThree.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightLeftFour.tga", knightSize), (float) 200) };
		AnimationFrame[] blueKnightRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightRightOne.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightRightTwo.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightRightThree.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightRightFour.tga", knightSize), (float) 200) };
		AnimationFrame[] blueKnightAttackLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackLeftOne.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackLeftTwo.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackLeftThree.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackLeftFour.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackLeftFive.tga", knightAttackSize), (float) 200) };
		AnimationFrame[] blueKnightAttackRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackRightOne.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackRightTwo.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackRightThree.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackRightFour.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/blueKnightAttackRightFive.tga", knightAttackSize), (float) 200) };
		
		AnimationFrame[] darkKnightLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightLeftOne.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightLeftTwo.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightLeftThree.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightLeftFour.tga", knightSize), (float) 200) };
		AnimationFrame[] darkKnightRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightRightOne.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightRightTwo.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightRightThree.tga", knightSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightRigthFour.tga", knightSize), (float) 200) };
		AnimationFrame[] darkKnightAttackLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackLeftOne.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackLeftTwo.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackLeftThree.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackLeftFour.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackLeftFive.tga", knightAttackSize), (float) 200) };
		AnimationFrame[] darkKnightAttackRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackRightOne.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackRightTwo.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackRightThree.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackRightFour.tga", knightAttackSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/darkKnightAttackRightFive.tga", knightAttackSize), (float) 200) };

		
		// Boars
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
		AnimationFrame[] frostBoarLeft = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/frostBoarLeftOne.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/frostBoarLeftTwo.tga", boarSize), (float) 200),
				new AnimationFrame(glTexImageTGAFile(gl, "data/frostBoarLeftThree.tga", boarSize), (float) 200), };
		AnimationFrame[] frostBoarRight = {
				new AnimationFrame(glTexImageTGAFile(gl, "data/frostBoarRightOne.tga", boarSize), (float) 400),
				new AnimationFrame(glTexImageTGAFile(gl, "data/frostBoarRightTwo.tga", boarSize), (float) 400),
				new AnimationFrame(glTexImageTGAFile(gl, "data/frostBoarRightThree.tga", boarSize), (float) 400), };

		int[] arrowDir = { glTexImageTGAFile(gl, "data/arrowUp.tga", projectileSizeV),
				glTexImageTGAFile(gl, "data/arrowRight.tga", projectileSizeH),
				glTexImageTGAFile(gl, "data/arrowDown.tga", projectileSizeV),
				glTexImageTGAFile(gl, "data/arrowLeft.tga", projectileSizeH) };
		int[] fireArrowDir = { glTexImageTGAFile(gl, "data/fireArrowUp.tga", projectileSizeV),
				glTexImageTGAFile(gl, "data/fireArrowRight.tga", projectileSizeH),
				glTexImageTGAFile(gl, "data/fireArrowDown.tga", projectileSizeV),
				glTexImageTGAFile(gl, "data/fireArrowLeft.tga", projectileSizeH) };

		int[] badArrowDir = { glTexImageTGAFile(gl, "data/badArrowLeft.tga", projectileSizeH),
				glTexImageTGAFile(gl, "data/badArrowRight.tga", projectileSizeH) };
		
		int[] blueKnightArrowDir = { glTexImageTGAFile(gl, "data/blueKnightSpearLeft.tga", knightSpearSize),
				glTexImageTGAFile(gl, "data/blueKnightSpearRight.tga", knightSpearSize) };
		
		int[] darkKnightArrowDir = { glTexImageTGAFile(gl, "data/darkKnightSpearLeft.tga", knightSpearSize),
				glTexImageTGAFile(gl, "data/darkKnightSpearRight.tga", knightSpearSize) };

		// Slimes
		ArrayList<Enemy> enemies = new ArrayList<>();
		enemies.add(new Slime(enemyPos[0] + 200, enemyPos[1] + 200, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 600, enemyPos[1] + 900, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 1800, enemyPos[1] + 500, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 896, enemyPos[1] + 256, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 832, enemyPos[1] + 256, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 832, enemyPos[1] + 192, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 896, enemyPos[1] + 192, tileSize[0], tileSize[1], slimeGreen));
		enemies.add(new Slime(enemyPos[0] + 1536, enemyPos[1] + 1728, tileSize[0], tileSize[1], slimeGreen));

		ArrayList<Enemy> shrooms = new ArrayList<>();
		shrooms.add(new Slime(enemyPos[0] + 1152, enemyPos[1] + 256, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 1088, enemyPos[1] + 64, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 1792, enemyPos[1] + 896, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 1280, enemyPos[1] + 1280, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 960, enemyPos[1] + 1728, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 448, enemyPos[1] + 1472, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 448, enemyPos[1] + 1024, tileSize[0], tileSize[1], mushroomOne));
		shrooms.add(new Slime(enemyPos[0] + 128, enemyPos[1] + 768, tileSize[0], tileSize[1], mushroomOne));

		// Animation
		idleAnimation = new Animation(idle);
		moveLeftAnimation = new Animation(movingLeft);
		moveRightAnimation = new Animation(movingRight);
		attackLeftAnimation = new Animation(attackLeft);
		attackRightAnimation = new Animation(attackRight);
		shooterLeftAnimation = new Animation(shooterLeft);
		shooterRightAnimation = new Animation(shooterRight);
		shooterAttackLeftAnimation = new Animation(shooterAttackLeft);
		shooterAttackRightAnimation = new Animation(shooterAttackRight);
		// fireAnimation = new Animation(fire);
		fireBoarLeftAnimation = new Animation(fireBoarLeft);
		fireBoarRightAnimation = new Animation(fireBoarRight);
		wildBoarLeftAnimation = new Animation(wildBoarLeft);
		wildBoarRightAnimation = new Animation(wildBoarRight);
		ironBoarLeftAnimation = new Animation(ironBoarLeft);
		ironBoarRightAnimation = new Animation(ironBoarRight);
		//Level Two Animation
		frostBoarLeftAnimation = new Animation(frostBoarLeft);
		frostBoarRightAnimation = new Animation(frostBoarRight);
		blueKnightLeftAnimation = new Animation(blueKnightLeft);
		blueKnightRightAnimation = new Animation(blueKnightRight);
		blueKnightAttackLeftAnimation = new Animation(blueKnightAttackLeft);
		blueKnightAttackRightAnimation = new Animation(blueKnightAttackRight);
		darkKnightLeftAnimation = new Animation(darkKnightLeft);
		darkKnightRightAnimation = new Animation(darkKnightRight);
		darkKnightAttackLeftAnimation = new Animation(darkKnightAttackLeft);
		darkKnightAttackRightAnimation = new Animation(darkKnightAttackRight);
		

		ArrayList<Enemy> boars = new ArrayList<Enemy>();
		boars.add(new WildBoar(enemyPos[0] + 64, enemyPos[1] + 64, boarSize[0], boarSize[1],
				wildBoarRight[0].getImage(), wildBoarLeftAnimation, wildBoarRightAnimation));
		boars.add(new WildBoar(enemyPos[0] + 240, enemyPos[1] + 240, boarSize[0], boarSize[1],
				wildBoarRight[0].getImage(), wildBoarLeftAnimation, wildBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 400, enemyPos[1] + 400, boarSize[0], boarSize[1],
				ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 240, enemyPos[1] + 800, boarSize[0], boarSize[1],
				ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 1000, enemyPos[1] + 65, boarSize[0], boarSize[1],
				ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new IronBoar(enemyPos[0] + 1700, enemyPos[1] + 1000, boarSize[0], boarSize[1],
				ironBoarRight[0].getImage(), ironBoarLeftAnimation, ironBoarRightAnimation));
		boars.add(new FireBoar(enemyPos[0] + 240, enemyPos[1] + 240, boarSize[0], boarSize[1],
				fireBoarRight[0].getImage(), fireBoarLeftAnimation, fireBoarRightAnimation));
		boars.add(new FireBoar(enemyPos[0] + 640, enemyPos[1] + 840, boarSize[0], boarSize[1],
				fireBoarRight[0].getImage(), fireBoarLeftAnimation, fireBoarRightAnimation));
		boars.add(new FireBoar(enemyPos[0] + 1700, enemyPos[1] + 1700, boarSize[0], boarSize[1],
				fireBoarRight[0].getImage(), fireBoarLeftAnimation, fireBoarRightAnimation));
		
		ArrayList<Enemy> frostBoars = new ArrayList<Enemy>();
		frostBoars.add(new FrostBoar(enemyPos[0] + 1597, enemyPos[1] +  1427, boarSize[0], boarSize[1],
				frostBoarRight[0].getImage(), frostBoarLeftAnimation, frostBoarRightAnimation));
		frostBoars.add(new FrostBoar(enemyPos[0] + 1408, enemyPos[1] + 128, boarSize[0], boarSize[1],
				frostBoarRight[0].getImage(),  frostBoarLeftAnimation, frostBoarRightAnimation));
		frostBoars.add(new FrostBoar(enemyPos[0] + 1408, enemyPos[1] + 768, boarSize[0], boarSize[1],
				frostBoarRight[0].getImage(), frostBoarLeftAnimation, frostBoarRightAnimation));
		frostBoars.add(new FrostBoar(enemyPos[0] + 448, enemyPos[1] + 1555, boarSize[0], boarSize[1],
				frostBoarRight[0].getImage(),  frostBoarLeftAnimation, frostBoarRightAnimation));

		shinjou = new Shinjou(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle[0], idle[0].getImage(),
				idleAnimation, moveLeftAnimation, moveRightAnimation, attackLeftAnimation, attackRightAnimation);

		ArrayList<Enemy> hunters = new ArrayList<>();
		hunters.add(new Hunter(enemyPos[0] + 640, enemyPos[1] + 840, shooterSize[0], shooterSize[1],
				shooterRight[0].getImage(), shooterLeftAnimation, shooterRightAnimation, shooterAttackLeftAnimation,
				shooterAttackRightAnimation, badArrowDir));
		hunters.add(new Hunter(enemyPos[0] + 200, enemyPos[1] + 200, shooterSize[0], shooterSize[1],
				shooterLeft[0].getImage(), shooterLeftAnimation, shooterRightAnimation, shooterAttackLeftAnimation,
				shooterAttackRightAnimation, badArrowDir));

		ArrayList<Enemy> knights = new ArrayList<>();
		knights.add(new Hunter(enemyPos[0] + 1597, enemyPos[1] + 1427, knightSize[0], knightSize[1],
				blueKnightLeft[0].getImage(), blueKnightLeftAnimation, blueKnightRightAnimation, blueKnightAttackLeftAnimation,
				blueKnightAttackRightAnimation, blueKnightArrowDir));
		knights.add(new Hunter(enemyPos[0] + 1597, enemyPos[1] + 1427, knightSize[0], knightSize[1],
				blueKnightLeft[0].getImage(), blueKnightLeftAnimation, blueKnightRightAnimation, blueKnightAttackLeftAnimation,
				blueKnightAttackRightAnimation, blueKnightArrowDir));
		knights.add(new Hunter(enemyPos[0] + 300, enemyPos[1] + 300, knightSize[0], knightSize[1],
				darkKnightLeft[0].getImage(), darkKnightLeftAnimation, darkKnightRightAnimation, darkKnightAttackLeftAnimation,
				darkKnightAttackRightAnimation, darkKnightArrowDir));
		knights.add(new Hunter(enemyPos[0] + 300, enemyPos[1] + 300, knightSize[0], knightSize[1],
				darkKnightLeft[0].getImage(), darkKnightLeftAnimation, darkKnightRightAnimation, darkKnightAttackLeftAnimation,
				darkKnightAttackRightAnimation, darkKnightArrowDir));
		knights.add(new Hunter(enemyPos[0] + 300, enemyPos[1] + 300, knightSize[0], knightSize[1],
				darkKnightLeft[0].getImage(), darkKnightLeftAnimation, darkKnightRightAnimation, darkKnightAttackLeftAnimation,
				darkKnightAttackRightAnimation, darkKnightArrowDir));
		
		

		// Create a bounding box camera for both the monkey and camera
		AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSizeIdle[0], spriteSizeIdle[1]);
		AABBCamera fireAABB = new AABBCamera(firePos[0], firePos[1], fireSize[0], fireSize[1]);
		// AABBCamera boarAABB = new AABBCamera(boarPos[0], boarPos[1], boarSize[0],
		// boarSize[1]);
		AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), screenX, screenY);

		// ~~~~~~~~ The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~
		// The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~
		// ~~~~~~~~ The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~
		// The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~ The Game Loop ~~~~~~~~

		long lastFrameNS;
		long curFrameNS = System.nanoTime();

		// Physics runs at 100fps, or 10ms / physics frame
		int physicsDeltaMs = 10;
		int lastPhysicsFrameMs = 0;

		boolean startGame = false;
		boolean levelOne = false;
		boolean readyForLevelTwo = false;
		boolean levelTwo = false;

		while (!shouldExit) {
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
			// Actually, this runs the entire OS message pump.
			window.display();

			if (!window.isVisible()) {
				shouldExit = true;
				break;
			}
			if (kbState[KeyEvent.VK_ESCAPE])
				shouldExit = true;

			// Start Screen
			if (!startGame) {
				glDrawSprite(gl, startScreen, 0, 0, startSreenSize[0], startSreenSize[1]);
				if (kbState[KeyEvent.VK_ENTER]) {
					startGame = true;
					levelTwo = true;
				}

			}

			// Game logic goes here.
			lastFrameNS = curFrameNS;
			curFrameNS = System.nanoTime();
			deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
			float velocity = 150 * ((float) deltaTimeMS / 1000);

			ArrayList<Projectile> projList = shinjou.getProjectiles();

			// Level One!
			if (levelOne) {

				// Physics Update
				do {
					// 1. Physics movement
					// 2. Physics collision detection
					// 3. Physics collision resolution

					// Char Movement
					characterMove(velocity, deltaTimeMS, backgroundLevelOne);

					// Character Projectile Collision with Background
					if (projList.size() > 0) {
						for (int i = 0; i < projList.size(); i++) {
							// System.out.println("proj: " + projList.get(i).getX());
							Projectile proj = projList.get(i);
							if (backgroundLevelOne
									.getTile((float) Math.floor((proj.getX() + proj.getWidth() / 2) / 64),
											(float) Math.floor((proj.getY() + proj.getHeight() / 2) / 64))
									.getCollision())
								projList.remove(i);

							if (proj.getX() > worldWidth || proj.getX() < 0 || proj.getY() > worldHeight
									|| proj.getY() < 0) {
								projList.remove(i);
							} else
								proj.update(velocity * 2);
						}
					}

					// Hunter Projectile Collision with Background
					for (Enemy h : hunters) {
						ArrayList<Projectile> projList2 = h.getProjectiles();
						if (h.getProjectiles().size() > 0) {
							for (int i = 0; i < projList2.size(); i++) {

								// System.out.println("proj: " + projList.get(i).getX());
								Projectile proj = projList2.get(i);
								try {
									if (backgroundLevelOne
											.getTile((float) Math.floor((proj.getX() + proj.getWidth() / 2) / 64),
													(float) Math.floor((proj.getY() + proj.getHeight() / 2) / 64))
											.getCollision())
										projList2.remove(i);

									if (proj.getX() > worldWidth || proj.getX() < 0 || proj.getY() > worldHeight
											|| proj.getY() < 0) {
										projList2.remove(i);
									} else
										proj.update(velocity * 2);
								} catch (ArrayIndexOutOfBoundsException exception) {
									projList2.remove(i);
								}
							}
						}
					}

					score += shinjou.hitDetection(boars, slimeYellow);
					score += shinjou.hitDetection(enemies, slimeYellow);
					score += shinjou.hitDetection(hunters, slimeYellow);

					// Boars Attacking Char
					for (Character b : boars) {
						if (AABBIntersect(b.getCollisionBox(), shinjou.getCollisionBox()) && !shinjou.getIsHit()
								&& b.isAlive()) {
							shinjou.decHealth(b.getDamage());
							System.out.println("Damage Done: " + b.getDamage());
							// lastTrueTime=now;
							shinjou.setHit(true);
							System.out.println("health: " + shinjou.getHealth());
						}
					}

					// Char Collision with Quiver
					for (FireQuiver q : quiverList) {
						if (AABBIntersect(q.getCollisionBox(), shinjou.getCollisionBox())) {
							q.setVisible(false);
							shinjou.setFireQuiverOn(true);
							shinjou.setDamage();
						}
					}

					// Get enemies left
					enemiesLeft = 0;
					for (Enemy e : enemies) {
						if (e.isAlive())
							enemiesLeft += 1;
					}
					for (Enemy e : boars) {
						if (e.isAlive())
							enemiesLeft += 1;
					}
					for (Enemy e : hunters) {
						if (e.isAlive())
							enemiesLeft += 1;
					}
					if (enemiesLeft == 0)
						readyForLevelTwo = true;

					lastPhysicsFrameMs += physicsDeltaMs;
				} while (lastPhysicsFrameMs + physicsDeltaMs < (curFrameNS / 1000000));

				if (kbState[KeyEvent.VK_ESCAPE]) {
					shouldExit = true;
				}

				//
				// Camera Movement
				//

				cameraMove(velocity, backgroundLevelOne);

				// fireAnimation.updateSprite(deltaTimeMS);
				//
				// hunt1(deltaTimeMS, firePos);

				for (Enemy b : boars) {
					if (b.isAlive())
						b.update(deltaTimeMS, spritePos, backgroundLevelOne);
				}
				for (Enemy h : hunters) {
					if (h.isAlive()) {
						h.update(deltaTimeMS, spritePos, backgroundLevelOne);
						h.hitDetection(shinjou);
					}
				}
				// Bullet Release
				if (kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE] && !shinjou.getShooting()) {
					Projectile proj1;
					if (projDir == 0 || projDir == 2) {
						if (!shinjou.isFireQuiverOn())
							proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
									spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
									projDir, arrowDir);
						else
							proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
									spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
									projDir, fireArrowDir);
					} else if (!shinjou.isFireQuiverOn())
						proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
								spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
								projDir, arrowDir);
					else
						proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
								spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
								projDir, fireArrowDir);
					shinjou.addProjectile(proj1);
					shinjou.setShooting(true);
				}

				if (shinjou.getShooting()) {
					if (projDir == 0 || projDir == 1) {
						attackRightAnimation.updateSprite(deltaTimeMS);
						shinjou.setCurrentTexture(attackRightAnimation.getCurrentFrame());
					} else {
						attackLeftAnimation.updateSprite(deltaTimeMS);
						shinjou.setCurrentTexture(attackLeftAnimation.getCurrentFrame());
					}
					if (attackLeftAnimation.finished || attackRightAnimation.finished) {
						shinjou.setShooting(false);
						attackLeftAnimation.setFinished(false);
						attackRightAnimation.setFinished(false);
					}
				}
				spriteAABB.setX(shinjou.getX());
				spriteAABB.setY(shinjou.getY());


				cameraAABB.setX(camera.getX());
				cameraAABB.setY(camera.getY());
				
//				gl.glClearColor(0, 0, 0, 1);
//				gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
			

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
						glDrawSprite(gl, backgroundLevelOne.getTile(i, j).getImage(), tileX - camera.getX(),
								tileY - camera.getY(), tileSize[0], tileSize[1]);
					}
				}

				// Draw sprites

				// FireAnimation
				// if (AABBIntersect(cameraAABB, fireAABB)) {
				// glDrawSprite(gl, fireAnimation.getCurrentFrame(), firePos[0] - camera.getX(),
				// firePos[1] - camera.getY(), fireSize[0], fireSize[1]);
				// }

				// Slimes
				for (Enemy s : enemies) {
					if (AABBIntersect(cameraAABB, s.getCollisionBox()) && s.isAlive()) {
						glDrawSprite(gl, s.getCurrentTexture(), s.getX() - camera.getX(), s.getY() - camera.getY(),
								tileSize[0], tileSize[1]);
					}
				}

				// Boars
				for (Enemy b : boars) {
					if (AABBIntersect(cameraAABB, b.getCollisionBox()) && b.isAlive()) {
						glDrawSprite(gl, b.getCurrAnimation().getCurrentFrame(), b.getX() - camera.getX(),
								b.getY() - camera.getY(), boarSize[0], boarSize[1]);
					}
				}

				// Quiver
				for (FireQuiver q : quiverList) {
					if (q.isVisible())
						glDrawSprite(gl, fireQuiver, q.x - camera.getX(), q.y - camera.getY(), projectileQuiver[0],
								projectileQuiver[1]);
				}

				// Hunters
				for (Enemy h : hunters) {
					if (AABBIntersect(cameraAABB, h.getCollisionBox()) && h.isAlive())
						glDrawSprite(gl, h.getCurrAnimation().getCurrentFrame(), h.getX() - camera.getX(),
								h.getY() - camera.getY(), shooterSize[0], shooterSize[1]);

					// Projectiles
					for (int i = 0; i < h.getProjectiles().size(); i++) {
						if (AABBIntersect(cameraAABB, h.getProjectiles().get(i).getCollisionBox())) {
							glDrawSprite(gl, h.getProjectiles().get(i).getCurrentTexture(),
									h.getProjectiles().get(i).getX() - camera.getX(),
									h.getProjectiles().get(i).getY() - camera.getY(), projectileSizeH[0],
									projectileSizeH[1]);
						}
					}
				}
				// Char's Projectiles
				for (int i = 0; i < projList.size(); i++) {
					if (AABBIntersect(cameraAABB, projList.get(i).getCollisionBox())) {
						if (shinjou.getProjectiles().get(i).getDir() == 0
								|| shinjou.getProjectiles().get(i).getDir() == 2)
							glDrawSprite(gl, shinjou.getProjectiles().get(i).getCurrentTexture(),
									projList.get(i).getX() - camera.getX(), projList.get(i).getY() - camera.getY(),
									projectileSizeV[0], projectileSizeV[1]);
						else
							glDrawSprite(gl, shinjou.getProjectiles().get(i).getCurrentTexture(),
									projList.get(i).getX() - camera.getX(), projList.get(i).getY() - camera.getY(),
									projectileSizeH[0], projectileSizeH[1]);
					}
				}
				// Character
				if (AABBIntersect(spriteAABB, cameraAABB)) {
					if (shinjou.getIsHit()) {
						blinkCount++;
						if (blinkCount % 8 == 0)
							glDrawSprite(gl, shinjou.getCurrentTexture(), shinjou.getX() - camera.getX(),
									shinjou.getY() - camera.getY(), spriteSizeMoving[0], spriteSizeMoving[1]);
						// System.out.println("Count: " + blinkCount);
						if (blinkCount > 50) {
							shinjou.setHit(false);
							blinkCount = 0;
						}
					} else
						glDrawSprite(gl, shinjou.getCurrentTexture(), shinjou.getX() - camera.getX(),
								shinjou.getY() - camera.getY(), spriteSizeMoving[0], spriteSizeMoving[1]);

				}

				// UI
				glDrawSprite(gl, healthTex, 0, 0, healthSize[0], healthSize[1]);
				glDrawSprite(gl, scoreTex, 128, 0, scoreSize[0], scoreSize[1]);
				glDrawSprite(gl, enemiesLeftTex, 350, 0, enemiesLeftSize[0], enemiesLeftSize[1]);
				if (shinjou.isFireQuiverOn())
					glDrawSprite(gl, fireQuiver, 10, 64, projectileQuiver[0], projectileQuiver[1]);
				if (AABBIntersect(shinjou.getCollisionBox(), lockedBox) && !readyForLevelTwo)
					glDrawSprite(gl, lockedTex, 1825 - camera.getX(), 1670 - camera.getY(), lockedSize[0],
							lockedSize[1]);
				font.DrawText(font.getArray(), Integer.toString(score), 128 + scoreSize[0], 10);
				font.DrawText(font.getArray(), Integer.toString(enemiesLeft), 350 + enemiesLeftSize[0], 10);
				font.DrawText(font.getArray(), Integer.toString(shinjou.getHealth()), 65, 10);

				font.DrawText(font.getArray(), Integer.toString((int) shinjou.getX()), 0 + enemiesLeftSize[0], 128);
				font.DrawText(font.getArray(), Integer.toString((int) shinjou.getY()), 0 + enemiesLeftSize[0], 328);


				// Move To Level Two
				if (AABBIntersect(shinjou.getCollisionBox(), lockedBox) && readyForLevelTwo) {
					levelOne = false;
					levelTwo = true;
					spritePos[0] =  100;
					spritePos[1] = 100;
					shinjou.setX(100);
					shinjou.setY(100);
				}
			}

			// ############################################ LEVEL TWO #######################################################
			// ############################################ LEVEL TWO#######################################################
			// ############################################ LEVEL TWO#######################################################
			// ############################################ LEVEL TWO#######################################################
			// ############################################ LEVEL TWO #######################################################
			// ############################################ LEVEL TWO #######################################################

			else if (levelTwo) {

				// Physics Update
				do {
					// 1. Physics movement
					// 2. Physics collision detection
					// 3. Physics collision resolution

					// Char Movement
					characterMove(velocity, deltaTimeMS, backgroundLevelTwo);

					// Character Projectile Collision with Background
					if (projList.size() > 0) {
						for (int i = 0; i < projList.size(); i++) {
							// System.out.println("proj: " + projList.get(i).getX());
							Projectile proj = projList.get(i);
							if (backgroundLevelTwo
									.getTile((float) Math.floor((proj.getX() + proj.getWidth() / 2) / 64),
											(float) Math.floor((proj.getY() + proj.getHeight() / 2) / 64))
									.getCollision())
								projList.remove(i);

							if (proj.getX() > worldWidth || proj.getX() < 0 || proj.getY() > worldHeight
									|| proj.getY() < 0) {
								projList.remove(i);
							} else
								proj.update(velocity * 2);
						}
					}
					// Hunter Projectile Collision with Background
					for (Enemy h : knights) {
						ArrayList<Projectile> projList2 = h.getProjectiles();
						if (h.getProjectiles().size() > 0) {
							for (int i = 0; i < projList2.size(); i++) {

								// System.out.println("proj: " + projList.get(i).getX());
								Projectile proj = projList2.get(i);
								try {
									if (backgroundLevelTwo
											.getTile((float) Math.floor((proj.getX() + proj.getWidth() / 2) / 64),
													(float) Math.floor((proj.getY() + proj.getHeight() / 2) / 64))
											.getCollision())
										projList2.remove(i);

									if (proj.getX() > worldWidth || proj.getX() < 0 || proj.getY() > worldHeight
											|| proj.getY() < 0) {
										projList2.remove(i);
									} else {			System.out.println("Spear Updating" + velocity);
										proj.update(velocity * 2);
									}
								} catch (ArrayIndexOutOfBoundsException exception) {
									projList2.remove(i);
								}
							}
						}
					}

					// Boars Attacking Char
					for (Character b : frostBoars) {
						if (AABBIntersect(b.getCollisionBox(), shinjou.getCollisionBox()) && !shinjou.getIsHit()
								&& b.isAlive()) {
							shinjou.decHealth(b.getDamage());
							System.out.println("Damage Done: " + b.getDamage());
							// lastTrueTime=now;
							shinjou.setHit(true);
							System.out.println("health: " + shinjou.getHealth());
						}
					}

					score += shinjou.hitDetection(shrooms, mushroomTwo);
					score += shinjou.hitDetection(frostBoars, mushroomTwo);
					score += shinjou.hitDetection(knights, mushroomTwo);

					// Get enemies left
					enemiesLeft = 0;
					for (Enemy e : shrooms) {
						if (e.isAlive())
							enemiesLeft += 1;
					}
					for (Enemy e : frostBoars) {
						if (e.isAlive())
							enemiesLeft += 1;
					}
					for (Enemy e : knights) {
						if (e.isAlive())
							enemiesLeft += 1;
					}

					lastPhysicsFrameMs += physicsDeltaMs;
				} while (lastPhysicsFrameMs + physicsDeltaMs < (curFrameNS / 1000000));
				// Camera Move
				cameraMove(velocity, backgroundLevelTwo);

				// Update Enemies
				for (Enemy b : frostBoars) {
					if (b.isAlive())
						b.update(deltaTimeMS, spritePos, backgroundLevelTwo);
				}
				for (Enemy k : knights) {
					if (k.isAlive()) {
						k.update(deltaTimeMS, spritePos, backgroundLevelTwo);
						k.hitDetection(shinjou);
					}
				}
				// Bullet Release
				if (kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE] && !shinjou.getShooting()) {
					Projectile proj1;
					if (projDir == 0 || projDir == 2) {
						if (!shinjou.isFireQuiverOn())
							proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
									spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
									projDir, arrowDir);
						else
							proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
									spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
									projDir, fireArrowDir);
					} else if (!shinjou.isFireQuiverOn())
						proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
								spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
								projDir, arrowDir);
					else
						proj1 = new Projectile(spritePos[0] + (spriteSizeMoving[0] / 4),
								spritePos[1] + (spriteSizeMoving[1] / 4), projectileSizeV[0], projectileSizeV[1],
								projDir, fireArrowDir);
					shinjou.addProjectile(proj1);
					shinjou.setShooting(true);
				}

				if (shinjou.getShooting()) {
					if (projDir == 0 || projDir == 1) {
						attackRightAnimation.updateSprite(deltaTimeMS);
						shinjou.setCurrentTexture(attackRightAnimation.getCurrentFrame());
					} else {
						attackLeftAnimation.updateSprite(deltaTimeMS);
						shinjou.setCurrentTexture(attackLeftAnimation.getCurrentFrame());
					}
					if (attackLeftAnimation.finished || attackRightAnimation.finished) {
						shinjou.setShooting(false);
						attackLeftAnimation.setFinished(false);
						attackRightAnimation.setFinished(false);
					}
				}
				spriteAABB.setX(shinjou.getX());
				spriteAABB.setY(shinjou.getY());

				cameraAABB.setX(camera.getX());
				cameraAABB.setY(camera.getY());

				// gl.glClearColor(0, 0, 0, 1);
				// gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

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
						glDrawSprite(gl, backgroundLevelTwo.getTile(i, j).getImage(), tileX - camera.getX(),
								tileY - camera.getY(), tileSize[0], tileSize[1]);
					}
				}

				// Shrooms
				for (Enemy s : shrooms) {
					if (AABBIntersect(cameraAABB, s.getCollisionBox()) && s.isAlive()) {
						glDrawSprite(gl, s.getCurrentTexture(), s.getX() - camera.getX(), s.getY() - camera.getY(),
								tileSize[0], tileSize[1]);
					}
				}
				// Boars
				for (Enemy b : frostBoars) {
					if (AABBIntersect(cameraAABB, b.getCollisionBox()) && b.isAlive()) {
						glDrawSprite(gl, b.getCurrAnimation().getCurrentFrame(), b.getX() - camera.getX(),
								b.getY() - camera.getY(), boarSize[0], boarSize[1]);
					}
				}
				// Hunters
				for (Enemy k : knights) {
					if (AABBIntersect(cameraAABB, k.getCollisionBox()) && k.isAlive())
						glDrawSprite(gl, k.getCurrAnimation().getCurrentFrame(), k.getX() - camera.getX(),
								k.getY() - camera.getY(), shooterSize[0], shooterSize[1]);

					// Projectiles
					for (int i = 0; i < k.getProjectiles().size(); i++) {
						if (AABBIntersect(cameraAABB, k.getProjectiles().get(i).getCollisionBox())) {
							glDrawSprite(gl, k.getProjectiles().get(i).getCurrentTexture(),
									k.getProjectiles().get(i).getX() - camera.getX(),
									k.getProjectiles().get(i).getY() - camera.getY(), knightSpearSize[0],
									knightSpearSize[1]);
						}
					}

				}

				// Char's Projectiles
				for (int i = 0; i < projList.size(); i++) {
					if (AABBIntersect(cameraAABB, projList.get(i).getCollisionBox())) {
						if (shinjou.getProjectiles().get(i).getDir() == 0
								|| shinjou.getProjectiles().get(i).getDir() == 2)
							glDrawSprite(gl, shinjou.getProjectiles().get(i).getCurrentTexture(),
									projList.get(i).getX() - camera.getX(), projList.get(i).getY() - camera.getY(),
									projectileSizeV[0], projectileSizeV[1]);
						else
							glDrawSprite(gl, shinjou.getProjectiles().get(i).getCurrentTexture(),
									projList.get(i).getX() - camera.getX(), projList.get(i).getY() - camera.getY(),
									projectileSizeH[0], projectileSizeH[1]);
					}
				}
				// Character
				if (AABBIntersect(spriteAABB, cameraAABB)) {
					if (shinjou.getIsHit()) {
						blinkCount++;
						if (blinkCount % 8 == 0)
							glDrawSprite(gl, shinjou.getCurrentTexture(), shinjou.getX() - camera.getX(),
									shinjou.getY() - camera.getY(), spriteSizeMoving[0], spriteSizeMoving[1]);
						// System.out.println("Count: " + blinkCount);
						if (blinkCount > 50) {
							shinjou.setHit(false);
							blinkCount = 0;
						}
					} else
						glDrawSprite(gl, shinjou.getCurrentTexture(), shinjou.getX() - camera.getX(),
								shinjou.getY() - camera.getY(), spriteSizeMoving[0], spriteSizeMoving[1]);

				}

				// UI
				glDrawSprite(gl, healthTex, 0, 0, healthSize[0], healthSize[1]);
				glDrawSprite(gl, scoreTex, 128, 0, scoreSize[0], scoreSize[1]);
				glDrawSprite(gl, enemiesLeftTex, 350, 0, enemiesLeftSize[0], enemiesLeftSize[1]);
				if (shinjou.isFireQuiverOn())
					glDrawSprite(gl, fireQuiver, 10, 64, projectileQuiver[0], projectileQuiver[1]);
				if (AABBIntersect(shinjou.getCollisionBox(), lockedBox) && !readyForLevelTwo)
					glDrawSprite(gl, lockedTex, 1825 - camera.getX(), 1670 - camera.getY(), lockedSize[0],
							lockedSize[1]);
				font.DrawText(font.getArray(), Integer.toString(score), 128 + scoreSize[0], 10);
				font.DrawText(font.getArray(), Integer.toString(enemiesLeft), 350 + enemiesLeftSize[0], 10);
				font.DrawText(font.getArray(), Integer.toString(shinjou.getHealth()), 65, 10);

				font.DrawText(font.getArray(), Integer.toString((int) shinjou.getX()), 0 + enemiesLeftSize[0], 128);
				font.DrawText(font.getArray(), Integer.toString((int) shinjou.getY()), 0 + enemiesLeftSize[0], 328);

			}
			
			// General Actions Used in All Levels

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

	public static int DrawText(FontDef font, String str, float x, float y) {
		return 0;
	}

	public static void hunt1(long deltaTimeMS, int[] pos) {
		// float v = 200 * ((float) deltaTimeMS / 1000);
		float v = (deltaTimeMS / 16) * 3;
		if (backgroundLevelOne.getTile((float) Math.floor((pos[0]) / 64), (float) Math.floor(pos[1] / 64))
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

	public static void characterMove(float velocity, long deltaTimeMS, Background bgLevel) {
		// move up
		if (kbState[KeyEvent.VK_W] && !shinjou.getShooting()) {
			if (!bgLevel.getTile((float) Math.floor((spritePos[0] + 15) / 64), (float) Math.floor(spritePos[1] / 64))
					.getCollision()
					&& !bgLevel.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0] / 2) / 64),
							(float) Math.floor(spritePos[1] / 64)).getCollision()
					&& !bgLevel.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0] - 15) / 64),
							(float) Math.floor(spritePos[1] / 64)).getCollision()) {
				spritePos[1] -= velocity;

				if (spritePos[1] < 0)
					spritePos[1] = 1;
			} else
				spritePos[1] = (float) (Math.ceil(spritePos[1] / 64) * 64);

			moveRightAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(moveRightAnimation.getCurrentFrame());

			if (spritePos[1] < worldHeight - 365)
				camera.setY(spritePos[1] - 235);
			if (camera.getY() < 0)
				camera.setY(0);

			projDir = 0;
		}

		// move down
		if (kbState[KeyEvent.VK_S] && !shinjou.getShooting()) {
			if (spritePos[1] + spriteSizeMoving[1] >= worldHeight )
				spritePos[1] = worldHeight - spriteSizeMoving[1] -1;
			if (!bgLevel.getTile((float) Math.floor((spritePos[0] + 15) / 64),
					(float) Math.floor((spritePos[1] + spriteSizeMoving[1]) / 64)).getCollision()
					&& !bgLevel.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0] / 2) / 64),
							(float) Math.floor((spritePos[1] + spriteSizeMoving[1]) / 64)).getCollision()
					&& !bgLevel.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0] - 15) / 64),
							(float) Math.floor((spritePos[1] + spriteSizeMoving[1]) / 64)).getCollision()) {
				// if((kbState[KeyEvent.VK_S] && kbState[KeyEvent.VK_D]) ||
				// (kbState[KeyEvent.VK_S] && kbState[KeyEvent.VK_A]))
				// spritePos[1] += velocity/2;
				// else
				spritePos[1] += velocity;
				
			
			} else
				spritePos[1] = (float) (Math.floor((spritePos[1] + spriteSizeMoving[1]) / 64) * 64)
						- spriteSizeMoving[1];

			moveLeftAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());

			if (spritePos[1] > 235)
				camera.setY(spritePos[1] - 235);
			if (camera.getY() + 1 > tileSize[1] * bgLevel.getHeight() - screenY)
				camera.setY(tileSize[1] * bgLevel.getHeight() - screenY);

			projDir = 2;
		}
		// move left
		if (kbState[KeyEvent.VK_A] && !shinjou.getShooting()) {
			if (!bgLevel.getTile((float) Math.floor((spritePos[0]) / 64), (float) Math.floor((spritePos[1] + 15) / 64))
					.getCollision()
					&& !bgLevel.getTile((float) Math.floor((spritePos[0]) / 64),
							(float) Math.floor((spritePos[1] + spriteSizeMoving[1] / 2) / 64)).getCollision()
					&& !bgLevel
							.getTile((float) Math.floor((spritePos[0]) / 64),
									(float) Math.floor((spritePos[1] - 15 + spriteSizeMoving[1]) / 64))
							.getCollision()) {

				if ((kbState[KeyEvent.VK_A] && kbState[KeyEvent.VK_W])
						|| (kbState[KeyEvent.VK_A] && kbState[KeyEvent.VK_S]))
					spritePos[0] -= velocity / 2;
				else
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
		if (kbState[KeyEvent.VK_D] && !shinjou.getShooting()) {

			if (!bgLevel.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64),
					(float) Math.floor((spritePos[1] + 15) / 64)).getCollision()
					&& !bgLevel.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64),
							(float) Math.floor((spritePos[1] + spriteSizeMoving[1] / 2) / 64)).getCollision()
					&& !bgLevel
							.getTile((float) Math.floor((spritePos[0] + spriteSizeMoving[0]) / 64),
									(float) Math.floor((spritePos[1] - 15 + spriteSizeMoving[1]) / 64))
							.getCollision()) {

				if ((kbState[KeyEvent.VK_D] && kbState[KeyEvent.VK_W])
						|| (kbState[KeyEvent.VK_D] && kbState[KeyEvent.VK_S]))
					spritePos[0] += velocity / 2;
				else
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
			if (camera.getX() > tileSize[0] * bgLevel.getWidth() - screenX)
				camera.setX(tileSize[0] * bgLevel.getWidth() - screenX);
			projDir = 1;
		}
		if (!shinjou.getShooting() && !kbState[KeyEvent.VK_D] && !kbState[KeyEvent.VK_W] && !kbState[KeyEvent.VK_A]
				&& !kbState[KeyEvent.VK_S]) {
			idleAnimation.updateSprite(deltaTimeMS);
			shinjou.setCurrentTexture(idleAnimation.getCurrentFrame());
		}

		shinjou.setX(spritePos[0]);
		shinjou.setY(spritePos[1]);
		// Trying to upgrade the moving by using AABBox Collisions
		// boolean collChar = false;
		// float moveBy = 0;
		//
		// // Find Tiles In Camera
		// int startX = (int) (camera.getX() / 64);
		// int endX = (int) (camera.getX() + (screenX - 1)) / 64;
		// int startY = (int) (camera.getY() / 64);
		// int endY = (int) (camera.getY() + (screenY - 1)) / 64;
		// // move up
		// if (kbState[KeyEvent.VK_W]) {
		// for (int i = startX; i < endX + 1; i++) {
		// for (int j = startY; j < endY + 1; j++) {
		// if (backgroundLevelOne.getTile(i, j) != null) {
		// AABBCamera tileAABB = new AABBCamera(i * tileSize[0], j * tileSize[1],
		// tileSize[0], tileSize[1]);
		// if (AABBIntersect(shinjou.getCollisionBox(), tileAABB)
		// && backgroundLevelOne.getTile(i, j).getCollision()) {
		// moveBy = ((j + 1) * tileSize[1]) - spritePos[1] + 5;
		// System.out.println(moveBy);
		// collChar = true;
		// break;
		// }
		// }
		// }
		// if (collChar)
		// break;
		// }
		// if (collChar) {
		// spritePos[1] += moveBy;
		// shinjou.setY(spritePos[1]);
		// collChar = false;
		// } else {
		// spritePos[1] -= velocity;
		// shinjou.setY(spritePos[1]);
		// if (spritePos[1] < 0)
		// spritePos[1] = 0;
		// }
		//
		// if (spritePos[1] < worldHeight - 365)
		// camera.setY(spritePos[1] - 235);
		// if (camera.getY() < 0)
		// camera.setY(0);
		//
		// moveLeftAnimation.updateSprite(deltaTimeMS);
		// shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());
		// projDir = 0;
		// }
		//
		// // move down
		// else if (kbState[KeyEvent.VK_S]) {
		// for (int i = startX; i < endX + 1; i++) {
		// for (int j = startY; j < endY + 1; j++) {
		// if (backgroundLevelOne.getTile(i, j) != null) {
		// AABBCamera tileAABB = new AABBCamera(i * tileSize[0], (j-1) * tileSize[1],
		// tileSize[0], tileSize[1]);
		// if (AABBIntersect(shinjou.getCollisionBox(), tileAABB)
		// && backgroundLevelOne.getTile(i, j).getCollision()) {
		// moveBy = (spritePos[1] + spriteSizeMoving[1])- j * (tileSize[1]) - 5;
		// collChar = true;
		// break;
		// }
		// }
		// }
		// if (collChar)
		// break;
		// }
		// if (collChar) {
		// spritePos[1] -= moveBy;
		// shinjou.setY(spritePos[1]);
		// collChar = false;
		// } else {
		// spritePos[1] += velocity;
		// shinjou.setY(spritePos[1]);
		// if (spritePos[1] < 0)
		// spritePos[1] = 0;
		// }
		//
		// moveLeftAnimation.updateSprite(deltaTimeMS);
		// shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());
		//
		// if (spritePos[1] > 235)
		// camera.setY(spritePos[1] - 235);
		// if (camera.getY() + 1 > tileSize[1] * backgroundLevelOne.getHeight() -
		// screenY)
		// camera.setY(tileSize[1] * backgroundLevelOne.getHeight() - screenY);
		//
		// projDir = 2;
		// }
		//
		// // move left
		// else if (kbState[KeyEvent.VK_A]) {
		// for (int i = startX; i < endX + 1; i++) {
		// for (int j = startY; j < endY + 1; j++) {
		// if (backgroundLevelOne.getTile(i, j) != null) {
		// AABBCamera tileAABB = new AABBCamera(i * tileSize[0], j * tileSize[1],
		// tileSize[0],
		// tileSize[1]);
		// if (AABBIntersect(shinjou.getCollisionBox(), tileAABB)
		// && backgroundLevelOne.getTile(i, j).getCollision()) {
		// moveBy = ((i +1) * tileSize[0]) - spritePos[0] + 5;
		// System.out.println(moveBy);
		// collChar = true;
		// break;
		// }
		// }
		// }
		// if (collChar)
		// break;
		// }
		// if (collChar) {
		// spritePos[0] += moveBy;
		// shinjou.setX(spritePos[0]);
		// collChar = false;
		// } else {
		// spritePos[0] -= velocity;
		// shinjou.setX(spritePos[0]);
		// if (spritePos[0] < 0)
		// spritePos[0] = 0;
		// }
		//
		// moveLeftAnimation.updateSprite(deltaTimeMS);
		// shinjou.setCurrentTexture(moveLeftAnimation.getCurrentFrame());
		//
		// if (spritePos[0] < worldWidth - 480)
		// camera.setX(spritePos[0] - 320);
		// if (camera.getX() - 1 < 0)
		// camera.setX(0);
		//
		// projDir = 3;
		// }
		////
		// // move right
		// else if (kbState[KeyEvent.VK_D]) {
		// for (int i = startX; i < endX + 1; i++) {
		// for (int j = startY; j < endY + 1; j++) {
		// if (backgroundLevelOne.getTile(i, j) != null) {
		// AABBCamera tileAABB = new AABBCamera(i * tileSize[0], j * tileSize[1],
		// tileSize[0],
		// tileSize[1]);
		// if (AABBIntersect(shinjou.getCollisionBox(), tileAABB)
		// && backgroundLevelOne.getTile(i, j).getCollision()) {
		// moveBy = (spritePos[0] + spriteSizeMoving[0]) - (i * tileSize[0]) - 5;
		// collChar = true;
		// break;
		// }
		// }
		// }
		// if (collChar)
		// break;
		// }
		// if (collChar) {
		// spritePos[0] -= moveBy;
		// shinjou.setX(spritePos[0]);
		// collChar = false;
		// } else {
		// spritePos[0] += velocity;
		// if (spritePos[0] > (worldWidth - spriteSizeMoving[0]))
		// spritePos[0] = worldWidth - spriteSizeMoving[0];
		// shinjou.setX(spritePos[0]);
		// }
		//
		// moveRightAnimation.updateSprite(deltaTimeMS);
		// shinjou.setCurrentTexture(moveRightAnimation.getCurrentFrame());
		//
		// if (spritePos[0] > 320)
		// camera.setX(spritePos[0] - 320);
		// if (camera.getX() > tileSize[0] * backgroundLevelOne.getWidth() - screenX)
		// camera.setX(tileSize[0] * backgroundLevelOne.getWidth() - screenX);
		// projDir = 1;
		// } else {
		// idleAnimation.updateSprite(deltaTimeMS);
		// shinjou.setCurrentTexture(idleAnimation.getCurrentFrame());
		// }

	}

	public static void cameraMove(float v, Background bgLevel) {
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
			if (camera.getY() + 1 > tileSize[1] * bgLevel.getHeight() - screenY)
				camera.setY(tileSize[1] * bgLevel.getHeight() - screenY);
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
			if (camera.getX() > tileSize[0] * bgLevel.getWidth() - screenX)
				camera.setX(tileSize[0] * bgLevel.getWidth() - screenX);
		}

	}
}



