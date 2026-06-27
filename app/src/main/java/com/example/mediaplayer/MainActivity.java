package com.example.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText etVideoUrl;
    private EditText etMusicUrl;

    // 示例直播/视频地址（HTTP，兼容旧设备）
    private static final String SAMPLE_VIDEO_1 = "http://vjs.zencdn.net/v/oceans.mp4";
    private static final String SAMPLE_VIDEO_2 = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    private static final String SAMPLE_MUSIC_1 = "http://www.music163.com/song/media/outer/url?id=447925558.mp3";
    private static final String SAMPLE_MUSIC_2 = "http://www.hochmuth.com/mp3/Haydn_Cello_Concerto_D-1.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        // 视频 Tab
        TabHost.TabSpec videoTab = tabHost.newTabSpec("video");
        videoTab.setIndicator("视频");
        videoTab.setContent(R.id.tab_video);
        tabHost.addTab(videoTab);

        // 音乐 Tab
        TabHost.TabSpec musicTab = tabHost.newTabSpec("music");
        musicTab.setIndicator("音乐");
        musicTab.setContent(R.id.tab_music);
        tabHost.addTab(musicTab);

        // 视频区域
        etVideoUrl = (EditText) findViewById(R.id.et_video_url);
        Button btnPlayVideo = (Button) findViewById(R.id.btn_play_video);
        Button btnSampleVideo1 = (Button) findViewById(R.id.btn_sample_video1);
        Button btnSampleVideo2 = (Button) findViewById(R.id.btn_sample_video2);

        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etVideoUrl.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入视频地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                openVideoPlayer(url);
            }
        });

        btnSampleVideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etVideoUrl.setText(SAMPLE_VIDEO_1);
                openVideoPlayer(SAMPLE_VIDEO_1);
            }
        });

        btnSampleVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etVideoUrl.setText(SAMPLE_VIDEO_2);
                openVideoPlayer(SAMPLE_VIDEO_2);
            }
        });

        // 音乐区域
        etMusicUrl = (EditText) findViewById(R.id.et_music_url);
        Button btnPlayMusic = (Button) findViewById(R.id.btn_play_music);
        Button btnSampleMusic1 = (Button) findViewById(R.id.btn_sample_music1);
        Button btnSampleMusic2 = (Button) findViewById(R.id.btn_sample_music2);

        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etMusicUrl.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入音乐地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                openMusicPlayer(url);
            }
        });

        btnSampleMusic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMusicUrl.setText(SAMPLE_MUSIC_1);
                openMusicPlayer(SAMPLE_MUSIC_1);
            }
        });

        btnSampleMusic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMusicUrl.setText(SAMPLE_MUSIC_2);
                openMusicPlayer(SAMPLE_MUSIC_2);
            }
        });
    }

    private void openVideoPlayer(String url) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void openMusicPlayer(String url) {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
