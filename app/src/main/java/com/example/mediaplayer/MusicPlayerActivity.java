package com.example.mediaplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MusicPlayerActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ProgressBar loadingBar;
    private TextView tvStatus, tvCurrent, tvTotal, tvTitle;
    private ImageButton btnPlayPause;
    private Handler handler = new Handler();
    private boolean isPrepared = false;
    private String musicUrl;

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && isPrepared && mediaPlayer.isPlaying()) {
                int current = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(current);
                tvCurrent.setText(formatTime(current));
            }
            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        musicUrl = getIntent().getStringExtra("url");

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        loadingBar = (ProgressBar) findViewById(R.id.loading_bar);
        btnPlayPause = (ImageButton) findViewById(R.id.btn_play_pause);

        // 截取 URL 最后部分作为标题显示
        String[] parts = musicUrl.split("/");
        tvTitle.setText(parts.length > 0 ? parts[parts.length - 1] : "在线音乐");
        tvStatus.setText("正在缓冲...");

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(musicUrl);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                    loadingBar.setVisibility(View.GONE);
                    tvStatus.setText("已就绪");
                    int duration = mp.getDuration();
                    seekBar.setMax(duration);
                    tvTotal.setText(formatTime(duration));
                    btnPlayPause.setEnabled(true);
                    mp.start();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    tvStatus.setText("播放中");
                    handler.post(updateSeekBar);
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    loadingBar.setVisibility(View.GONE);
                    tvStatus.setText("加载失败，请检查地址或网络");
                    Toast.makeText(MusicPlayerActivity.this, "音乐加载失败", Toast.LENGTH_LONG).show();
                    return true;
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    tvStatus.setText("播放完成");
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
                }
            });

            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            tvStatus.setText("地址解析失败: " + e.getMessage());
            loadingBar.setVisibility(View.GONE);
        }

        btnPlayPause.setEnabled(false);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPrepared) return;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    tvStatus.setText("已暂停");
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    tvStatus.setText("播放中");
                }
            }
        });

        // 后退 10 秒
        ImageButton btnRewind = (ImageButton) findViewById(R.id.btn_rewind);
        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPrepared) return;
                int pos = Math.max(0, mediaPlayer.getCurrentPosition() - 10000);
                mediaPlayer.seekTo(pos);
            }
        });

        // 快进 10 秒
        ImageButton btnForward = (ImageButton) findViewById(R.id.btn_forward);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPrepared) return;
                int pos = Math.min(mediaPlayer.getDuration(),
                        mediaPlayer.getCurrentPosition() + 10000);
                mediaPlayer.seekTo(pos);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isPrepared) {
                    mediaPlayer.seekTo(progress);
                    tvCurrent.setText(formatTime(progress));
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private String formatTime(int ms) {
        int seconds = (ms / 1000) % 60;
        int minutes = (ms / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBar);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
