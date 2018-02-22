package hw1;

class Background
{
	int width;
	int height;
	Tile[] tiles;

	public Background(int image, int w, int h) 
	{
	
		width = w;
		height = h;
		tiles = new Tile[width * height];
		for (int i = 0; i < tiles.length -1 ; i++) 
		{
			tiles[i] = new Tile(image);
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

	public void setImage(int image, int start, int end)
	{
		for (int i = start; i < end -1 ; i++) 
		{
			tiles[i].setImage(image);
		}
	}
	
	public Tile getTile(int x, int y) 
	{
		return tiles[y * width + x];
	}
}