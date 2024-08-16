package com.company.danhine;

import static android.content.Context.MODE_PRIVATE;

import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import com.company.danhine.databinding.ActivityGameBinding;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Ball {
    private ImageView ball;
    private float x, y, centerX, centerY;
    private float radius;
    private float vx, vy;
    private final float gravity = 9.8f;
    private final float elasticity = 0.8f;
    boolean isRicochet = false;
    boolean glassRic = true;
    private Rectangle[] rectangles = new Rectangle[3];
    ActivityGameBinding binding;

    public Ball(float x, float y, ImageView ball, ActivityGameBinding binding) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.ball = ball;
        this.centerX = x + 0.5f * ball.getWidth();
        this.centerY = y + 0.5f * ball.getHeight();
        this.radius = ball.getWidth() * 1.0f / 2;
        this.binding = binding;
        rectangles[0] = new Rectangle(binding.cube1, binding);
        rectangles[1] = new Rectangle(binding.cube2, binding);
        rectangles[2] = new Rectangle(binding.cube3, binding);
    }

    public int update(float deltaTime) {
        vy += gravity * deltaTime;

        for (Rectangle rectangle : rectangles) {
            int res = intersects(rectangle);
            if (res != 0) {
                return res;
            }
        }

        x += vx * deltaTime;
        y += vy * deltaTime;

        centerX += vx * deltaTime;
        centerY += vy * deltaTime;

        ball.setX(x);
        ball.setY(y);
        return 0;
    }

    private int intersects(Rectangle rect) {
        float coordX = (float) ((float) centerX + radius * 1.0f / Math.sqrt(2));
        float coordY = (float) ((float) centerY + radius * 1.0f / Math.sqrt(2));

        if (length(rect.x1, rect.y1, coordX, coordY) + length(coordX, coordY, rect.x2, rect.y2) - length(rect.x1, rect.y1, rect.x2, rect.y2) < 1f && !isRicochet) {
            sound();
            isRicochet = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRicochet = false;
                }
            }, 20);

            float tmp = vx;
            vx = -elasticity * vy;
            vy = elasticity * tmp;
            return 0;
        }
        coordX -= 2 * radius * 1.0f / Math.sqrt(2);

        if (length(rect.x2, rect.y2, coordX, coordY) + length(coordX, coordY, rect.x3, rect.y3) - length(rect.x3, rect.y3, rect.x2, rect.y2) < 1f && !isRicochet) {
            sound();
            isRicochet = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRicochet = false;
                }
            }, 20);
            float tmp = vx;
            vx = elasticity * vy;
            vy = -elasticity * tmp;
            return 0;
        }

        coordY -= 2 * radius * 1.0f / Math.sqrt(2);

        if (length(rect.x4, rect.y4, coordX, coordY) + length(coordX, coordY, rect.x3, rect.y3) - length(rect.x3, rect.y3, rect.x4, rect.y4) < 1f && !isRicochet) {
            sound();
            isRicochet = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRicochet = false;
                }
            }, 20);
            float tmp = vx;
            vx = -elasticity * vy;
            vy = -elasticity * tmp;
            return 0;
        }

        coordX += 2 * radius * 1.0f / Math.sqrt(2);

        if (length(rect.x4, rect.y4, coordX, coordY) + length(coordX, coordY, rect.x1, rect.y1) - length(rect.x1, rect.y1, rect.x4, rect.y4) < 1f && !isRicochet) {
            sound();
            isRicochet = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRicochet = false;
                }
            }, 20);
            float tmp = vx;
            vx = elasticity * vy;
            vy = -elasticity * tmp;
            return 0;
        }

        if (ball.getX() < 1f) {
            sound();
            vx = -vx;
            return 0;
        }
        if (ball.getWidth() + ball.getX() - binding.imageView6.getWidth() > -1f) {
            sound();
            vx = -vx;
            return 0;
        }

        if (ball.getHeight() + ball.getY() > binding.glass.getY()) {
            if (ball.getX() + ball.getWidth() / 2 < binding.glass.getX() && ball.getX() + ball.getWidth() > binding.glass.getX() && glassRic) {
                sound();
                float tmp = vx;
                vx = -elasticity * vy;
                vy = elasticity * tmp;
                glassRic = false;
            }
            if (ball.getX() + ball.getWidth() / 2 > binding.glass.getX() && ball.getX() < binding.glass.getX() && glassRic) {
                sound();
                float tmp = vx;
                vx = elasticity * vy;
                vy = elasticity * tmp;
                glassRic = false;
            }
            if (ball.getX() + ball.getWidth() / 2 < binding.glass.getX() + binding.glass.getWidth() &&
                ball.getX() + ball.getWidth() > binding.glass.getX() + binding.glass.getWidth() && glassRic) {
                sound();
                float tmp = vx;
                vx = -elasticity * vy;
                vy = elasticity * tmp;
                glassRic = false;
            }
            if (ball.getX() + ball.getWidth() / 2 > binding.glass.getX() + binding.glass.getWidth() &&
                ball.getX() < binding.glass.getX() + binding.glass.getWidth() && glassRic) {
                sound();
                float tmp = vx;
                vx = elasticity * vy;
                vy = elasticity * tmp;
                glassRic = false;
            }
        }
        if (ball.getHeight() / 2 + ball.getY() > binding.glass.getY()) {
            if (ball.getX() > binding.glass.getX() && ball.getX() + ball.getWidth() < binding.glass.getX() + binding.glass.getWidth()) {
                return 1;
            }
            return -1;

        }

        return 0;
    }

    private float length(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    private void sound() {
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                if (ball.getContext().getSharedPreferences("settings", MODE_PRIVATE).getBoolean("sound_enabled", true)) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(ball.getContext(), R.raw.hit);
                    mediaPlayer.setVolume(0.2f, 0.2f);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                }
            }
        });
        thr.start();

    }
}
