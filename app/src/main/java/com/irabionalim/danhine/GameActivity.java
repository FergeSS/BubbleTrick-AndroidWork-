package com.irabionalim.danhine;

import static com.irabionalim.danhine.Settings.action;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.irabionalim.danhine.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {
    public ActivityGameBinding binding;
    public static boolean active = false;
    ObjectAnimator animStartToRightFirst;
    ObjectAnimator animStartToRight;
    ObjectAnimator animStartToLeft;
    ObjectAnimator animStartToRightFirstBub;
    ObjectAnimator animStartToRightBub;
    ObjectAnimator animStartToLeftBub;
    Handler updateLogic;
    Dialog dialog;
    Runnable runLogic;
    int countLife = 3;
    int level = 1;
    boolean isCancel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        windowSettings();
        audioSettings();
        binding.buble.setOnClickListener(v -> click());
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        }, 100);
        active = true;
    }

    private void bubbleBrusting() {
        binding.buble.setImageResource(R.drawable.bubble02);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.buble.setImageResource(R.drawable.bubble03);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.buble.setImageResource(R.drawable.bubble04);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.buble.setAlpha(0.0f);
                                binding.buble.setImageResource(R.drawable.bubble01);
                            }
                        }, 20);
                    }
                }, 20);
            }
        }, 20);
    }

    private void fallingBall() {
        Ball ball = new Ball(binding.ball.getX(), binding.ball.getY(), binding.ball, binding);
        updateLogic = new Handler();
        runLogic = new Runnable() {
            @Override
            public void run() {
                int res = ball.update(0.1f);
                if (res == 0) {
                    updateLogic.postDelayed(this, 10);
                } else {
                    binding.ball.setTranslationY(0);
                    binding.ball.setTranslationX(0);
                    binding.buble.setAlpha(1.0f);
                    binding.buble.setTranslationX(0);
                    isCancel = false;
                    if (res == 1) {
                        ++level;
                        binding.live1.setChecked(true);
                        binding.live2.setChecked(true);
                        binding.live3.setChecked(true);
                        countLife = 3;
                        if (level == 6) {
                            level = 1;
                            dialogShow(true);
                        } else {
                            animStartToRightFirst.start();
                            animStartToRightFirstBub.start();
                        }
                        setLevel(level);
                        binding.level.setText(level + "");
                    } else {
                        --countLife;
                        if (countLife == 0) {
                            binding.live1.setChecked(false);
                            dialogShow(false);
                        }
                        if (countLife == 1) {
                            animStartToRightFirst.start();
                            animStartToRightFirstBub.start();
                            binding.live2.setChecked(false);
                        }
                        if (countLife == 2) {
                            animStartToRightFirst.start();
                            animStartToRightFirstBub.start();
                            binding.live3.setChecked(false);
                        }

                    }
                }
            }
        };
        updateLogic.postDelayed(runLogic, 10);

    }

    private void click() {
        if (animStartToRightFirst == null) {
            return;
        }
        if (animStartToRightFirst.isRunning()) {
            isCancel = true;
            animStartToRightFirst.cancel();
            animStartToRightFirstBub.cancel();
            bubbleBrusting();
            fallingBall();
            return;
        }
        if (animStartToRight.isRunning()) {
            isCancel = true;
            animStartToRight.cancel();
            animStartToRightBub.cancel();
            bubbleBrusting();
            fallingBall();
            return;
        }
        if (animStartToLeft.isRunning()) {
            isCancel = true;
            animStartToLeft.cancel();
            animStartToLeftBub.cancel();
            bubbleBrusting();
            fallingBall();
            return;
        }
    }

    private void startGame() {
        animStartToRightFirst = ObjectAnimator.ofFloat(binding.ball, "translationX", binding.getRoot().getWidth() / 2 - binding.buble.getWidth() / 2).setDuration(2000);
        animStartToRight = ObjectAnimator.ofFloat(binding.ball, "translationX", binding.getRoot().getWidth() / 2 - binding.buble.getWidth() / 2).setDuration(4000);
        animStartToLeft = ObjectAnimator.ofFloat(binding.ball, "translationX", -binding.getRoot().getWidth() / 2 + binding.buble.getWidth() / 2).setDuration(4000);

        animStartToRightFirst.setInterpolator(null);
        animStartToRight.setInterpolator(null);
        animStartToLeft.setInterpolator(null);

        animStartToRightFirst.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    animStartToLeft.start();
                }
            }
        });
        animStartToLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    animStartToRight.start();
                }
            }
        });
        animStartToRight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    animStartToLeft.start();
                }
            }
        });

        animStartToRightFirst.start();

        animStartToRightFirstBub = ObjectAnimator.ofFloat(binding.buble, "translationX", binding.getRoot().getWidth() / 2 - binding.buble.getWidth() / 2).setDuration(2000);
        animStartToRightBub = ObjectAnimator.ofFloat(binding.buble, "translationX", binding.getRoot().getWidth() / 2 - binding.buble.getWidth() / 2).setDuration(4000);
        animStartToLeftBub = ObjectAnimator.ofFloat(binding.buble, "translationX", -binding.getRoot().getWidth() / 2 + binding.buble.getWidth() / 2).setDuration(4000);

        animStartToRightFirstBub.setInterpolator(null);
        animStartToRightBub.setInterpolator(null);
        animStartToLeftBub.setInterpolator(null);

        animStartToRightFirstBub.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    animStartToLeftBub.start();
                }
            }
        });
        animStartToLeftBub.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    animStartToRightBub.start();
                }
            }
        });
        animStartToRightBub.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    animStartToLeftBub.start();
                }
            }
        });

        animStartToRightFirstBub.start();
    }

    public void home(View v) {
        action(GameActivity.this);
        finish();
    }

    public void audioSettings() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }

    public void windowSettings() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateLogic != null) {
            updateLogic.removeCallbacks(runLogic);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (animStartToRightFirst != null && animStartToRightFirst.isRunning()) {
            animStartToRightFirst.pause();
            animStartToRightFirstBub.pause();
            return;
        }
        if (animStartToRight != null && animStartToRight.isRunning()) {
            animStartToRight.pause();
            animStartToRightBub.pause();
            return;
        }
        if (animStartToLeft != null && animStartToLeft.isRunning()) {
            animStartToLeft.pause();
            animStartToLeftBub.pause();
            return;
        }
        if (updateLogic != null) {
            updateLogic.removeCallbacks(runLogic);
        }

    }

    private void setLevel(int level) {
        ConstraintSet con = new ConstraintSet();
        con.clone(binding.getRoot());
        if (level == 1) {
            con.setHorizontalBias(R.id.cube1, 0.786f);
            con.setVerticalBias(R.id.cube1, 0.672f);

            con.setHorizontalBias(R.id.cube2, 0.225f);
            con.setVerticalBias(R.id.cube2, 0.654f);

            con.setHorizontalBias(R.id.cube3, 0.750f);
            con.setVerticalBias(R.id.cube3, 0.334f);
        }
        if (level == 2) {
            con.setHorizontalBias(R.id.cube1, 0.743f);
            con.setVerticalBias(R.id.cube1, 0.67f);

            con.setHorizontalBias(R.id.cube2, 0.8f);
            con.setVerticalBias(R.id.cube2, 0.332f);

            con.setHorizontalBias(R.id.cube3, 0.250f);
            con.setVerticalBias(R.id.cube3, 0.5f);
        }
        if (level == 3) {
            con.setHorizontalBias(R.id.cube1, 0.243f);
            con.setVerticalBias(R.id.cube1, 0.681f);

            con.setHorizontalBias(R.id.cube2, 0.196f);
            con.setVerticalBias(R.id.cube2, 0.347f);

            con.setHorizontalBias(R.id.cube3, 0.700f);
            con.setVerticalBias(R.id.cube3, 0.561f);
        }
        if (level == 4) {
            con.setHorizontalBias(R.id.cube1, 0.253f);
            con.setVerticalBias(R.id.cube1, 0.712f);

            con.setHorizontalBias(R.id.cube2, 0.8f);
            con.setVerticalBias(R.id.cube2, 0.663f);

            con.setHorizontalBias(R.id.cube3, 0.502f);
            con.setVerticalBias(R.id.cube3, 0.366f);
        }
        if (level == 5) {
            con.setHorizontalBias(R.id.cube1, 0.203f);
            con.setVerticalBias(R.id.cube1, 0.306f);

            con.setHorizontalBias(R.id.cube2, 0.85f);
            con.setVerticalBias(R.id.cube2, 0.736f);

            con.setHorizontalBias(R.id.cube3, 0.361f);
            con.setVerticalBias(R.id.cube3, 0.543f);
        }

        con.applyTo(binding.getRoot());
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (animStartToRightFirst != null && animStartToRightFirst.isPaused()) {
            animStartToRightFirst.resume();
            animStartToRightFirstBub.resume();
            return;
        }
        if (animStartToRight != null && animStartToRight.isPaused()) {
            animStartToRight.resume();
            animStartToRightBub.resume();
            return;
        }
        if (animStartToLeft != null && animStartToLeft.isPaused()) {
            animStartToLeft.resume();
            animStartToLeftBub.resume();
            return;
        }
        if (runLogic != null) {
            updateLogic.post(runLogic);
        }
    }

    public void playAgain(View v) {
        setLevel(1);
        level = 1;
        countLife = 3;
        binding.live1.setChecked(true);
        binding.live2.setChecked(true);
        binding.live3.setChecked(true);
        animStartToRightFirst.start();
        animStartToRightFirstBub.start();
        binding.level.setText(level + "");
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    private void dialogShow(boolean isWin) {
        dialog = new Dialog(GameActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(wlp);
        dialog.setContentView(R.layout.dialog);
        if (isWin) {
            dialog.findViewById(R.id.winFrame).setAlpha(1.0f);
        } else {
            dialog.findViewById(R.id.winFrame).setAlpha(0.0f);
        }

        dialog.show();
    }
}