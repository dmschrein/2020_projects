package com.dmschrein.monkey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Spider {

    public int speed;
    int x, y, width, height, counter;
    Bitmap spider1, spider2;
    public boolean active;

    Spider(Resources res) {
        spider1 = BitmapFactory.decodeResource(res, R.drawable.spidershort);
        spider2 = BitmapFactory.decodeResource(res, R.drawable.spiderlong);

        width = spider1.getWidth() / 4;
        height = spider2.getHeight() / 4;

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

        spider1 = Bitmap.createScaledBitmap(spider1, width, height, false);
        spider2 = Bitmap.createScaledBitmap(spider2, width, height, false);

        y = - height;
        counter = 1;

        speed = 20;
    }
    public Bitmap getSpider() {
        if (counter == 1) {
            counter++;
            return spider1;
        }
        counter = 1;
        return spider2;

    }
    public Rect getCollisionShape() {
        return new Rect(x + 25, y + 5, x + width - 25, y + height);
    }
}
