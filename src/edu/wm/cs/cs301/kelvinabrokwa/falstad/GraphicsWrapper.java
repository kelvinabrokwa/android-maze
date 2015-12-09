package edu.wm.cs.cs301.kelvinabrokwa.falstad;

import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class GraphicsWrapper extends View {

	private Canvas canvas;
	private Paint paint;
	private Bitmap bitmap;
	
	public GraphicsWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
        bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		paint = new Paint();
	}
	
	public static enum GWEvent { LEFT, RIGHT, UP, DOWN };
	
	public static class GWPoint {
		public int x, y;
		GWPoint(int _x, int _y) {
			x = _x;
			y = _y;
		}
	}
	
	public static class GWColor extends Color {
		public final static int darkGray = DKGRAY;
		public final static int black = BLACK;
		public final static int white = WHITE;
		public final static int gray = GRAY;
		public final static int red = RED;
		public final static int yellow = YELLOW;

		private int r, g, b, a = 255;
		private int RGB = -1;
		public GWColor(int i, int val1, int val12) {
			super();
			r = i;
			g = val1;
			b = val12;
		}
		public GWColor(int rgb) {
			super();
			RGB =  0xff000000 | rgb;
		}
		public int getR() { return r; }
		public int getG() { return g; }
		public int getB() { return b; }
		public int getRGB() {
			if (RGB != -1) return RGB;
			return ((a & 0xFF) << 24) |
	               ((r & 0xFF) << 16) |
	               ((g & 0xFF) <<  8) |
	               ((b & 0xFF) <<  0);
		}
	}
	
	public GraphicsWrapper setColor(int color) {
		paint.setColor(color);
		return this;
	}

	public GraphicsWrapper setColor(GWColor color) {
		paint.setARGB(255, color.getR(), color.getG(), color.getB());
		return this;
	}
	
	public GraphicsWrapper drawLine(int x1, int y1, int x2, int y2) {
		canvas.drawLine(x1, y1, x2, y2, paint);
		return this;
	}
	
	public GraphicsWrapper fillOval(int x, int y, int width, int height) {
		paint.setStyle(Paint.Style.FILL);
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
		return this;
	}
	
	public GraphicsWrapper fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		paint.setStyle(Paint.Style.FILL);
		Path path = new Path(); 
		path.moveTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < nPoints; i++) {
			path.lineTo(xPoints[i], yPoints[i]);
		}
		path.lineTo(xPoints[0], yPoints[0]);
		canvas.drawPath(path, paint);
		return this;
	}
	
	public GraphicsWrapper fillRect(int x, int y, int width, int height) {
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(new Rect(x, y, x + width, y + height), paint);
		return this;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0,  0, paint);
		invalidate();
	}

}
