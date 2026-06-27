package com.example.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_player);

        videoView = (VideoView) findViewById(R.id.video_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        videoUrl = getIntent().getStringExtra("url");

        // 添加系统自带的媒体控制条（播放/暂停/进度条/快进快退）
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        tvStatus.setText("正在连接: " + videoUrl);
        progressBar.setVisibility(View.VISIBLE);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);
                mp.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressBar.setVisibility(View.GONE);
                tvStatus.setText("播放失败，请检查地址或网络连接\n错误码: " + what);
                tvStatus.setVisibility(View.VISIBLE);
                Toast.makeText(VideoPlayerActivity.this, "视频加载失败", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                tvStatus.setText("播放完成");
                tvStatus.setVisibility(View.VISIBLE);
            }
        });

        try {
            videoView.setVideoURI(Uri.parse(videoUrl));
            videoView.requestFocus();
        } catch (Exception e) {
            tvStatus.setText("地址解析失败: " + e.getMessage());
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
