package hw1;

class Background
{
	int width;
	int height;
	
	// 0 == grass
	// 1 == stone
	// 2 == water
	// 3 == sand1
	// 4 == sand2
	// 5 == lava
	// 6 == pavement
	// 7 - 10 = house
	// 11 = grassFlower
	int[] worldLevelOne = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,
			        1,0,0,0,0,0,0,0,0,7,8,7,8,7,8,0,0,0,0,0,0,0,0,0,0,0,0,3,4,2,
			        1,0,0,0,0,0,0,0,0,9,10,9,10,9,10,0,0,0,0,0,0,0,0,0,0,0,0,3,4,2,
			        1,6,6,6,6,6,6,6,6,6,6,6,6,6,6,0,0,0,0,0,0,0,0,0,0,0,0,4,3,1,
			        1,6,6,6,6,6,6,6,6,6,6,6,6,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,0,11,6,6,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,0,11,6,6,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,3,3,3,3,3,3,3,4,0,0,0,0,6,6,0,0,0,0,0,0,0,11,11,0,0,0,0,0,1,
			        1,2,2,2,2,2,2,2,4,0,0,0,0,6,6,0,0,0,0,0,0,0,0,11,11,0,0,0,0,1,
			        1,2,2,2,2,2,2,2,4,0,0,0,0,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,2,2,2,2,2,2,2,4,0,0,0,0,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,2,2,2,2,2,2,2,4,0,0,0,0,6,6,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,
			        1,3,3,3,3,3,3,3,4,0,0,0,0,6,6,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,
			        1,0,0,0,0,0,0,0,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,4,3,2,2,2,
			        1,0,0,0,0,0,0,0,6,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,4,2,2,2,2,
			        1,0,0,0,0,0,0,0,6,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,4,3,3,3,1,
			        1,0,0,0,0,0,0,0,6,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,6,0,0,0,0,0,6,6,6,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,6,6,6,6,6,6,6,6,0,0,3,3,0,0,0,6,0,0,0,11,11,0,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,3,3,0,0,0,6,0,0,0,0,11,11,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,4,3,3,4,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,3,2,2,3,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,4,3,3,4,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,1,
			        1,0,0,0,0,0,1,5,1,0,0,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,11,
			        1,0,0,0,0,0,1,1,1,11,11,0,0,0,0,0,6,0,0,0,0,0,0,0,3,4,3,4,3,4,
			        1,0,0,0,0,0,0,0,0,11,11,11,0,0,0,0,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
			        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,4,3,4,3,1,
			        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 };
	
	// 0 = ground1			13 = stoneWall2
	// 1 = ground2			14= redGround
	// 2 = ground3			15 = rockGround
	// 3 = grass				16 = rockWater
	// 4 = grass/wood		17 = crackedWater
	// 5 = mud				18 = water
	// 6 = woodWall			19-22 = curveWater
	// 7 = water				3 = markGround
	// 8/9/10/11 = waterEdge
	// 12 = stoneWall					0,1,2,3,4,5,14,15,3
	int[] worldLevelTwo = {
		   12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,
		   11, 3, 3, 2, 6, 3, 0, 0, 0,15, 0, 3, 6, 3, 3, 3, 6, 0, 1, 2, 0, 0, 3, 2, 1, 0, 6, 0, 0,13,
		   11, 4, 1, 2, 6, 3, 4, 0, 0, 0, 0, 3, 5, 0, 0, 0, 6, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0,13,
		   11, 0, 2, 1, 6, 3, 3, 3, 5, 0, 0, 3, 6, 0, 2, 5, 6, 6, 6, 6, 5, 5,15, 5, 0, 0, 6, 0, 0,13,
		   11, 0, 1,15, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 0,14, 0, 6, 6, 6, 6, 6, 0, 0, 6, 6, 6,13,
		   11, 0, 0, 0, 3, 3, 0, 0, 5, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,15, 6,
		   11, 0, 1, 0, 2, 1, 0, 2, 1, 0, 1, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,15, 6,
		   11,15, 3, 5, 4, 0, 0, 1, 2, 4, 3, 3, 3, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6,
		   11, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 2, 6, 0, 0, 0, 0, 0, 2, 0, 0, 5, 0, 6, 0, 6,
		   11, 0, 0, 0, 0, 0,15,15, 0, 0, 0, 4, 6, 0, 1, 0, 6, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 6, 0, 6,
		   11, 0,14, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 0, 0, 6, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 6, 0, 6,
		   21,10, 6, 6, 2, 3, 0, 0, 0, 0, 4,14, 3, 0, 0, 0, 6, 0, 0, 1, 0, 0, 0, 0, 0, 4, 0, 6, 0, 6,
			7,11, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 6, 3, 0, 0, 0, 0, 2, 0, 0, 0, 0, 6, 0, 6,
			7,11, 2, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 6, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6,
			7,11, 3, 0, 0, 0, 0, 0, 3, 0, 1, 0, 2, 0, 2, 0, 6, 3, 0, 3, 0, 0, 0, 0,14, 0, 0, 0, 0,13,
		   17,11, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 6, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,13,
			7,11, 0, 2, 0, 1, 0, 0, 0, 0, 0,14, 6, 0, 0, 0, 6, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,13,
			7,11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 6, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0,15, 0, 0, 0, 0, 9,
		   18,11, 0, 0, 0, 0, 0, 2, 0, 0, 0, 3, 6, 0, 0, 0, 6, 6, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0,14, 9,
			7,11, 0, 0, 0, 0, 0, 3, 0, 0, 0, 1, 6, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 2, 0, 2, 9,
		   18,11, 0, 3, 0, 0, 0, 4, 0, 0, 0,14, 6, 0, 0, 0, 0, 0, 0, 6, 0, 2, 0, 0, 0, 0, 0, 0, 3, 9,
			7,11, 0, 0, 0, 2, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 0, 0, 0, 0, 0, 9,
			7,11, 0, 0, 0, 2, 6,14, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 6, 9,
			7,11, 0, 1, 0, 0, 6, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 6, 9,
			7,11, 0, 0, 2, 0, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 5, 5, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6,
			7,11, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0,16,10,10,10,10,10,10,16, 0, 0, 2, 1, 6, 6,
			7,11, 0, 0, 3, 0, 0, 0, 2, 0, 3, 0, 0, 0, 0, 0, 9, 7, 7, 7, 7, 7, 7,11, 0, 3, 3, 3, 0, 6,
			7,11,15,15, 0, 0, 0, 0, 0, 0,15, 0, 0, 0, 1, 0, 9, 7,17, 7, 7, 7, 7,11,15, 1, 0, 2, 0,14,
		   17,21,10,10,10,10,10,10,10,10,10,10,10,10,10,10,20, 7, 7, 7,17, 7, 7,21,10,16, 0, 4, 0, 0,
			7, 7, 7, 7,18, 7,18, 7, 7,18, 7, 7,18, 7, 7, 7,16, 7, 7,17, 7, 7, 7, 7, 7,11, 0, 0, 5, 0,
			
	};

	Tile[] tiles;
	
	public Background(int[] images, int w, int h, int level) {
		width = w;
		height = h;

		tiles = new Tile[width * height];
		if (level == 1) {
			for (int i = 0; i < tiles.length; i++) {
				
				if (worldLevelOne[i] == 0)
					tiles[i] = new Tile(images[0], false);
				else if (worldLevelOne[i] == 1)
					tiles[i] = new Tile(images[1], true);
				else if (worldLevelOne[i] == 2)
					tiles[i] = new Tile(images[2], true);
				else if (worldLevelOne[i] == 3)
					tiles[i] = new Tile(images[3], false);
				else if (worldLevelOne[i] == 4)
					tiles[i] = new Tile(images[4], false);
				else if (worldLevelOne[i] == 5)
					tiles[i] = new Tile(images[5], true);
				else if (worldLevelOne[i] == 6)
					tiles[i] = new Tile(images[6], false);
				else if (worldLevelOne[i] == 7)
					tiles[i] = new Tile(images[7], true);
				else if (worldLevelOne[i] == 8)
					tiles[i] = new Tile(images[8], true);
				else if (worldLevelOne[i] == 9)
					tiles[i] = new Tile(images[9], true);
				else if (worldLevelOne[i] == 10)
					tiles[i] = new Tile(images[10], true);
				else if (worldLevelOne[i] == 11)
					tiles[i] = new Tile(images[11], false);
			}
		}
		else if(level == 2) {
			for (int i = 0; i < tiles.length; i++) {
				// 0 = ground1			13 = stoneWall2
				// 1 = ground2			14= redGround
				// 2 = ground3			15 = rockGround
				// 3 = grass				16 = rockWater
				// 4 = grass/wood		17 = crackedWater
				// 5 = mud				18 = water
				// 6 = woodWall			19-22 = curveWater
				// 7 = water				23 = markGround
				// 8/9/10/11 = wateredge
				// 12 = stoneWall
				if (worldLevelTwo[i] == 0)
					tiles[i] = new Tile(images[0], false);
				else if (worldLevelTwo[i] == 1)
					tiles[i] = new Tile(images[1], false);
				else if (worldLevelTwo[i] == 2)
					tiles[i] = new Tile(images[2], false);
				else if (worldLevelTwo[i] == 3)
					tiles[i] = new Tile(images[3], false);
				else if (worldLevelTwo[i] == 4)
					tiles[i] = new Tile(images[4], false);
				else if (worldLevelTwo[i] == 5)
					tiles[i] = new Tile(images[5], false);
				else if (worldLevelTwo[i] == 6)
					tiles[i] = new Tile(images[6], true);
				else if (worldLevelTwo[i] == 7)
					tiles[i] = new Tile(images[7], true);
				else if (worldLevelTwo[i] == 8)
					tiles[i] = new Tile(images[8], true);
				else if (worldLevelTwo[i] == 9)
					tiles[i] = new Tile(images[9], true);
				else if (worldLevelTwo[i] == 10)
					tiles[i] = new Tile(images[10], true);
				else if (worldLevelTwo[i] == 11)
					tiles[i] = new Tile(images[11], true);
				else if (worldLevelTwo[i] == 12)
					tiles[i] = new Tile(images[12], true);
				else if (worldLevelTwo[i] == 13)
					tiles[i] = new Tile(images[13], true);
				else if (worldLevelTwo[i] == 14)
					tiles[i] = new Tile(images[14], false);
				else if (worldLevelTwo[i] == 15)
					tiles[i] = new Tile(images[15], false);
				else if (worldLevelTwo[i] == 16)
					tiles[i] = new Tile(images[16], true);
				else if (worldLevelTwo[i] == 17)
					tiles[i] = new Tile(images[17], true);
				else if (worldLevelTwo[i] == 18)
					tiles[i] = new Tile(images[18], true);
				else if (worldLevelTwo[i] == 19)
					tiles[i] = new Tile(images[19], true);
				else if (worldLevelTwo[i] == 20)
					tiles[i] = new Tile(images[20], true);
				else if (worldLevelTwo[i] == 21)
					tiles[i] = new Tile(images[21], true);
				else if (worldLevelTwo[i] == 22)
					tiles[i] = new Tile(images[22], true);
//				else if (worldLevelTwo[i] == 23)
//					tiles[i] = new Tile(images[23], false);
			}
			
		}
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Tile getTile(float x, float y)
	{
		return tiles[(int) (y * width + x)];
	}

}
	
