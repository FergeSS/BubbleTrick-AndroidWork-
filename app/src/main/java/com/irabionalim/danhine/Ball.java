package com.irabionalim.danhine;

import static android.content.Context.MODE_PRIVATE;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.ImageView;
import com.irabionalim.danhine.databinding.ActivityGameBinding;

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
            if (!isRicochet) {
                int res = intersects(rectangle);
                if (res != 0) {
                    return res;
                }
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

    private void RicLeft() {
        sound();
        isRicochet = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRicochet = false;
            }
        }, 172);

        float tmp = vx;
        vx = -elasticity * vy;
        vy = -elasticity * tmp;
    }

    private void RicRight() {
        sound();
        isRicochet = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRicochet = false;
            }
        }, 172);

        float tmp = vx;
        vx = elasticity * vy;
        vy = elasticity * tmp;
    }

    private int intersects(Rectangle rect) {
        float coordX;
        float coordY;

        if (ball.getHeight() + ball.getY() > binding.glass.getY()) {
            if (ball.getX() + ball.getWidth() / 2 < binding.glass.getX() && ball.getX() + ball.getWidth() > binding.glass.getX() && glassRic ||
                    ball.getX() + ball.getWidth() / 2 < binding.glass.getX() + binding.glass.getWidth() &&
                            ball.getX() + ball.getWidth() > binding.glass.getX() + binding.glass.getWidth() && glassRic) {
                sound();
                float tmp = vx;
                vx = -elasticity * vy;
                vy = -elasticity * tmp;
                glassRic = false;
                return 0;
            }
            if (ball.getX() + ball.getWidth() / 2 > binding.glass.getX() && x < binding.glass.getX() && glassRic ||
                    ball.getX() + ball.getWidth() / 2 > binding.glass.getX() + binding.glass.getWidth() &&
                            ball.getX() < binding.glass.getX() + binding.glass.getWidth() && glassRic) {
                sound();
                float tmp = vx;
                vx = elasticity * vy;
                vy = elasticity * tmp;
                glassRic = false;
                return 0;
            }

        }

        if (ball.getHeight() / 2 + ball.getY() > binding.glass.getY()) {
            if (ball.getX() > binding.glass.getX() && ball.getX() + ball.getWidth() < binding.glass.getX() + binding.glass.getWidth()) {
                return 1;
            }
            return -1;

        }

        if (ball.getX() < 0.1f && vx <= 0) {
            sound();
            vx = -vx;
            isRicochet = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRicochet = false;
                }
            }, 100);
            return 0;
        }
        if (ball.getWidth() + ball.getX() - binding.imageView6.getWidth() > -0.1f && vx >= 0) {
            sound();
            vx = -vx;
            isRicochet = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRicochet = false;
                }
            }, 100);
            return 0;
        }

        if (centerX <= rect.x2 && centerX + radius >= rect.x1 && centerY <= rect.y1 && centerY + radius >= rect.y2 && Math.abs(vx - vy) >= 1f) {
            for (int i = 0; i <= 10; ++i) {
                coordX = centerX + (float) (radius * Math.cos(Math.toRadians(9 * i)));
                coordY = centerY + (float) (radius * Math.sin(Math.toRadians(9 * i)));

                if (coordY > -coordX + rect.b1) {
                    RicLeft();
                    return 0;
                }
            }
        }


        if (centerX >= rect.x2 && centerX - radius <= rect.x3 && centerY <= rect.y3 && centerY + radius >= rect.y2 && Math.abs(vx - vy) >= 1f) {
            for (int i = 0; i <= 10; ++i) {
                coordX = centerX - (float) (radius * Math.cos(Math.toRadians(9 * i)));
                coordY = centerY + (float) (radius * Math.sin(Math.toRadians(9 * i)));

                if (coordY > coordX + rect.b2) {
                    RicRight();
                    return 0;
                }
            }
        }

        if (centerX >= rect.x4 && centerX - radius <= rect.x3 && centerY >= rect.y3 && centerY - radius <= rect.y4) {
            for (int i = 0; i <= 10; ++i) {
                coordX = centerX - (float) (radius * Math.cos(Math.toRadians(9 * i)));
                coordY = centerY - (float) (radius * Math.sin(Math.toRadians(9 * i)));

                if (coordY < -coordX + rect.b3 && ((vx <= 0 && vy <= 0) || (vx <= 0 && vy > 0 && Math.abs(vx / vy) > 1) || (vx >= 0 && vy < 0 && Math.abs(vx / vy) < 1) )) {
                    RicLeft();
                    return 0;
                }
            }
        }

        if (centerX <= rect.x2 && centerX + radius >= rect.x1 && centerY >= rect.y3 && centerY - radius <= rect.y4 && Math.abs(vx - vy) >= 1f) {
            for (int i = 0; i <= 10; ++i) {
                coordX = centerX + (float) (radius * Math.cos(Math.toRadians(9 * i)));
                coordY = centerY - (float) (radius * Math.sin(Math.toRadians(9 * i)));

                if (coordY < coordX + rect.b4 && ((vx >= 0 && vy <= 0) || (vx <= 0 && vy < 0 && Math.abs(vx / vy) < 1) || (vx >= 0 && vy > 0 && Math.abs(vx / vy) > 1) )) {
                    RicRight();
                    return 0;
                }
            }
        }

        return 0;
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
