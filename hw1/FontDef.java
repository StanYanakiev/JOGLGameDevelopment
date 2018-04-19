package hw1;

import com.jogamp.opengl.GL2;

public class FontDef {
	int lineHeight = 45;
	GlyphDef[] array;
	
	public FontDef(GlyphDef[] a) {
		array = a;
	}
	
	public GlyphDef[] getArray() {
		return array;
	}

	public void setArray(GlyphDef[] array) {
		this.array = array;
	}

	
	public void DrawText(GlyphDef[] font, String s, float x, float y)
	{
		
//	 foreach (codept in str ) {
//	 glyph = font[codept];
//	 DrawGlyph(glyph, x, y);
//	 x += GlyphWidth( glyph );
//	 }
		for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j < font.length; j++) {
				if (s.substring(i, i + 1).equals(font[j].getChars())) {
					glDrawSprite(DemoGame.gl, font[j].getTexture(), x, y, font[j].getSize()[0], font[j].getSize()[1]);
					x += font[j].getSize()[0];
					break;
				}
			}

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
}
