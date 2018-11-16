package jgs.com.jogilsang.begginer;

/**
 * Created by User on 2017-02-20.
 */

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import jgs.com.jogilsang.begginer.R;

public class MainActivity extends AppCompatActivity {

    // 메트로놈 변수들
    SoundPool mPool;
    int mDding;
    int mDdong;

    // 메트로놈 쓰레드
    public MetronomeThread thread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mDding = mPool.load(this, R.raw.ding, 1);
        mDdong = mPool.load(this, R.raw.wood, 1);

        findViewById(R.id.play1).setOnClickListener(mClickListener);
        findViewById(R.id.play2).setOnClickListener(mClickListener);


    }

    private class MetronomeThread extends Thread {
        private static final String TAG = "MetronomeThread";

        double bpm;
        int measure;
        int counter = 0;
        boolean stop = false;

        public MetronomeThread(double bpm, int measure) {
            this.bpm = bpm;
            this.measure = measure;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }


        @Override
        public void run() {
            // 스레드에게 수행시킬 동작들 구현
            try {

                while (!Thread.currentThread().isInterrupted() && !stop) {
                    try {
                        Thread.sleep((long) (1000 * (60 / bpm)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter++;
                    if (counter % measure == 0) {
                        System.out.println("TICK");
                        mPool.play(mDding, 1, 0, -1, 0, 1);
                    } else {
                        System.out.println("TOCK");
                        mPool.play(mDdong, 1, 0, -1, 0, 1);
                    }
                }

            } catch (Exception e) {

            } finally {
                System.out.println("Thread is dead...");
            }

        }


    }

    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            MediaPlayer player;
            switch (v.getId()) {
                case R.id.play1:

                    // 쓰레드 실행
                    thread = new MetronomeThread(200, 2);
                    thread.start();

                    break;

                case R.id.play2:

                    // 쓰레드 중단
                    thread.interrupt();
                    thread.setStop(true);

                    break;
            }
        }
    };
}