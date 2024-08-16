package com.company.danhine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.ImageView;

import com.company.danhine.databinding.ActivityGameBinding;

public class Rectangle {
    float x1, y1, y2, x2, x3, y3, x4, y4;
    public Rectangle(ImageView cube, ActivityGameBinding binding) {
        float diag = (float)Math.sqrt(2) * cube.getWidth();
        this.x1 = cube.getX() + cube.getWidth() * 1.0f / 2 - diag / 2;
        this.y1 = cube.getY() + cube.getHeight() * 1.0f / 2 ;
        this.x2 = cube.getX() + cube.getWidth() * 1.0f / 2;
        this.y2 = cube.getY() + cube.getHeight() * 1.0f / 2 - diag / 2;
        this.x3 = this.x1 + diag;
        this.y3 = this.y1;
        this.x4 = this.x2;
        this.y4 = this.y2 + diag;
    }
}
