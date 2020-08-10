package com.dmschrein.monkey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Swing {

    public boolean isSwinging;
    public boolean canSwing;
    int x, y, screenX, screenY, startingX, startingY;
    int width, height, width2, height2, width3, height3, pos;
    public float speed;
    public float maxSpeed;
    public boolean isFalling;
    protected float gravity;
//    private GameView gameView;

    Bitmap swing1, swing2, swing3, swing4, swing5, swing6, swing7, dead;

    public Swing(int screenY, Resources res) {
        pos = 0;
        isSwinging = false;
        canSwing = true;
        isFalling = false;
        gravity = 1.2f;
        speed = 0f;
        maxSpeed = 7f;

//        this.gameView = gameView;

        swing1 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey1);
        swing2 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey2);
        swing3 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey3);
        swing4 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey4);
        swing5 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey5);
        swing6 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey6);
        swing7 = BitmapFactory.decodeResource(res, R.drawable.hangingmonkey7);


        width = swing1.getWidth();
        height = swing1.getHeight();

        width /= 4;
        height /= 4;

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

//        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
//        flight2 = Bitmap.createScaledBitmap(flight1, width, height, false);
//
//        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
//        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
//        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
//        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
//        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        swing1 = Bitmap.createScaledBitmap(swing1, width, height, false);
        swing2 = Bitmap.createScaledBitmap(swing2, width, height, false);
        swing3 = Bitmap.createScaledBitmap(swing3, width, height, false);
        swing4 = Bitmap.createScaledBitmap(swing4, width, height, false);
        swing5 = Bitmap.createScaledBitmap(swing5, width, height, false);
        swing6 = Bitmap.createScaledBitmap(swing6, width, height, false);
        swing7 = Bitmap.createScaledBitmap(swing7, width, height, false);


        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        this.screenY = screenY;
        // setting the starting position
        y = (int) ((screenY - screenY / 2) * GameView.screenRatioY);
        x = 300 * (int) GameView.screenRatioX;

    }
    public boolean isCanSwing() {
        if (pos == 0 && x == startingX) {
            canSwing = true;
            return canSwing;
        }
        else {
            canSwing = false;
            return canSwing;
        }
    }

    Bitmap getSwing() {

        if (pos % 6 == 0) {
            pos++;
            return swing1;
        }
        if (pos % 6 == 1) {
            pos++;
            return swing2;
        }
        if (pos % 6 == 2) {
            pos++;
            return swing3;
        }
        if (pos % 6 == 3) {
            pos++;
            return swing4;
        }
        if (pos % 6 == 4) {
            pos++;
            return swing5;
        }
        if (pos % 6 == 5) {
            pos++;
            return swing6;
        }
        if (pos % 6 == 6) {
            pos++;
            return swing7;
        }
        return swing7;
    }

    public void fall() {
        while(y >= 0) {
            if (isFalling) {
                speed += gravity;
                y -= gravity;
                if (speed > maxSpeed) {
                    speed = maxSpeed;
                }
            }
        }
        isFalling =  false;
    }

    public void bigSwing() {
        int maxWidth = (int) (screenX / 1.5);
        if (isCanSwing()) {
            canSwing = false;
            while(y > maxWidth) {
                y -= 50;
            }
        }
    }

    Rect getCollisionShape () {     // creates a new class
        return new Rect(x, y, x + width, y + height);

    }

    Bitmap getDead () {
        return dead;  //return dead bitmap
    }
}
