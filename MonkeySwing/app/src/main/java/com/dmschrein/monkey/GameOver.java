package com.dmschrein.monkey;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameOver {
    public int x;
    public int y;
    public int width;
    public int height;

    private Bitmap gameOver;

    public GameOver(int screenX, int screenY, Resources res) {


        Swing swing = new Swing(screenY, res);
        gameOver =BitmapFactory.decodeResource(res,R.drawable.gameover);

        width =gameOver.getWidth()/2;
        height =gameOver.getHeight()/2;

        width *=(int)GameView.screenRatioX;
        height *=(int)GameView.screenRatioY;

        gameOver =Bitmap.createScaledBitmap(gameOver,width,height,false);

        y =screenY /3;
        x =screenX /3;
    }

    public Bitmap getGameOver() {
        return gameOver;
    }
}
