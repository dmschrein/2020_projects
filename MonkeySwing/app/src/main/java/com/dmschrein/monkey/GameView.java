package com.dmschrein.monkey;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.os.Build;
import android.view.SurfaceView;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver;
    public static float screenRatioX, screenRatioY;
    private Background background1, background2;
    private int screenX, screenY, score, sound;
    private Paint paint;
    private Swing swing;
//    private Vine[4] vines;
    private Spider[] spiders;
    private Random random;
    private Banana[] bananas;
    private GameOver gameOver;
    private SharedPreferences prefs;
    private GameActivity activity;
    private SoundPool soundpool;
    private boolean pressed, goingLeft, goingRight;     //monkey swings left and right
    float swingLeftBy;
    float swingRightBy;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        pressed = false;
        goingLeft = false;
        goingRight = false;
        swingLeftBy = 250f;
        swingRightBy = 30f;

        // creates the sound for when a banana is collected
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                    setUsage(AudioAttributes.USAGE_GAME).build();

            soundpool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();

        } else {
            soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = soundpool.load(activity, R.raw.dingsound, 1);

        this.activity = activity;

        // stores score data
        this.prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        this.score = 0;

        this.screenX = screenX;
        this.screenY = screenY;

        // changes the x/y slightly for different screen sizes
        screenRatioX = 2220f / screenX;
        screenRatioY = 1080f / screenY;

        // uses two of same background to create side scroll effect
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        // set background2 to the right off of the screen after background1
        background2.x = screenX;

        // writes the score
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        // gets the instances of the other classes
        swing = new Swing(screenY, getResources());
//        vine = new Vine(screenY, getResources());
        spiders = new Spider[3];
        bananas = new Banana[4];
        random = new Random();
        gameOver = new GameOver(screenX, screenY, getResources());

        // setting game to not over when the game starts
        isGameOver = false;

        // creates two instances of the spider class
        for (int i = 0; i < 4; i++) {
            Spider spider = new Spider(getResources());
            spiders[i] = spider;
        }

        // creates the two instances of the banana class
        for (int i = 0; i < 3; i++) {
            Banana banana = new Banana(getResources());
            bananas[i] = banana;
        }

    }
    @Override
    public void run() {

        while (isPlaying) {

            update();
            draw();
            sleep();
        }
    }
    private void update() {
        // moves background from right to left for scrolling effect
        // repeats the two backgrounds to simulate movement
        background1.x -= 10;
        background2.x -= 10;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        // set spider drop down speeds to random
        for (Spider spider : spiders) {
            spider.y -= spider.speed;
            if(spider.y + spider.height < 0) {
                int bound = 30;
                spider.speed = random.nextInt(bound);
                if (spider.speed < 15) {
                    spider.speed = 20;
                }

                spider.x = screenY;
                spider.y = random.nextInt(screenY - (2 * spider.height));
                if (spider.y > swing.startingY - 200) {
                    spider.y = swing.startingY - 200;
                }
            }
            if (Rect.intersects(spider.getCollisionShape(), swing.getCollisionShape())) {
                isGameOver = true;
            }
        }
        // each banana appears at a random position
        // moves from right to left
        for (Banana banana : bananas) {
            banana.x -= banana.speed;
            if (banana.x + banana.width < 0) {
                int bound = 30;
                banana.speed = random.nextInt(bound);
                if (banana.speed < 15) {
                    banana.speed = 20;
                }
                banana.active = true;
                banana.x = screenX;
                banana.y = random.nextInt(screenY - (2 * banana.height));

            }

            // if monkey hits star, increment the score counter
            // and play noise if sound is not muted
            // remove banana image from screen after the collision
            if (Rect.intersects(banana.getCollisionShape(), swing.getCollisionShape())) {
                if (!prefs.getBoolean("isMute", false)) {
                    soundpool.play(sound, 1, 0, 0, 0, 1);
                }
                // add to score
                score = score + 1;
                // removes the banana
                banana.active = false;
                System.out.println(score);

            }

            // if monkey grabs vine, move monkey to new position in screen
//            if (Rect.intersects(vine.getCollisionShape(), swing.getCollisionShape())) {

        }

            if (pressed) {
                if (swing.x > 50 && goingRight) {

                    swingRightBy = swingRightBy * .99f;
                    swing.x -= swingRightBy;

                }
                else if (swing.x <= 50) {
                    goingRight = false;
                    goingLeft = true;
                    swing.x += swingLeftBy;
                }
                else if (swing.x <= swing.startingX - 60 && goingLeft) {
                    swingLeftBy = swingLeftBy * 4.2f;
                    swing.x += swingLeftBy;
                }

            }
        swingRightBy = 250f;
        swingLeftBy = 30f;
        }

        private void draw() {
            if(getHolder().getSurface().isValid()) {
                // creating the canvas
                Canvas canvas = getHolder().lockCanvas();
                // drawing both backgrounds
                canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

                // draw the spiders and bananas
                for (Spider spider: spiders) {
                    canvas.drawBitmap(spider.getSpider(), spider.x, spider.y, paint);
                }
                for (Banana banana: bananas) {
                    if (banana.active) {
                        canvas.drawBitmap(banana.getBanana(), banana.x, banana.y, paint);
                    }
                }
                // drawing the vines
                // similar to banana
//                for (Vine vine: vines) {
//                    canvas.drawBitmap(vine.getVine(), vine.x, vine.y, paint);
//                }

                // game over settings
                // saves the score if it's higher than the saved high score
                // waits a few secconds and then exits to title screen
                if (isGameOver) {
                    isPlaying = false;
                    canvas.drawBitmap(swing.getDead(), swing.x, swing.y, paint);
                    canvas.drawBitmap(gameOver.getGameOver(), gameOver.x, gameOver.y, paint);
                    getHolder().unlockCanvasAndPost(canvas);
                    saveIfHighScore();
                    waitBeforeExiting();
                    return;
                }

                // draws the swinging image
                canvas.drawBitmap(swing.getSwing(), swing.x, swing.y, paint);

                //painting is finished so now it can be drawn to the view's canvas
                getHolder().unlockCanvasAndPost(canvas);

        }

    }

    // senses the user pressing the screen
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            //Sent when the last pointer leaves the screen
            case MotionEvent.ACTION_UP:
                pressed = true;
                goingLeft = true;
        }
        return true;
    }
    // saves the score if it's higher than the current high score
    private void saveIfHighScore() {
        if (prefs.getInt("highscore", 1) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }
    // wait three seconds after game over before exiting
    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // starts the thread
    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        // starting a new thread will call the run function
        thread.start();
    }
    // stop the thread
    public void pause() {
        try {
            isPlaying = false;
            // terminates the thread
            thread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}
