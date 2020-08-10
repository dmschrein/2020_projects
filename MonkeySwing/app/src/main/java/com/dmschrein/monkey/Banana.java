package com.dmschrein.monkey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Banana {

    int x, y, width, height;
    public int speed;
    Bitmap banana;
    public boolean active;

    Banana(Resources res) {
        banana = BitmapFactory.decodeResource(res, R.drawable.banana);

        width = banana.getWidth() / 4;
        height = banana.getHeight() / 4;

        banana = Bitmap.createScaledBitmap(banana, width, height, false);

        y = - height;
        speed = 10;
        active = true;
    }
    public Bitmap getBanana() {
        return banana;
    }
    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
