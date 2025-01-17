package com.dmschrein.monkey;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    private MediaPlayer mediaPlayer;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        //using the same media player from the Main Activity
        this.mediaPlayer = MainActivity.mediaPlayer;

        super.onCreate(savedInstanceState);

        // set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // find the size of the screen
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        // create a new instance of gameView with the size of the screen
        gameView = new GameView(this, point.x, point.y);

        setContentView(gameView);

    }

    protected void onPause() {
        super.onPause();
        gameView.pause();
        mediaPlayer.pause();;
    }

    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

}

