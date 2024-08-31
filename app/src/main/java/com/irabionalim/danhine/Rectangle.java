package com.irabionalim.danhine;

import android.widget.ImageView;

import com.irabionalim.danhine.databinding.ActivityGameBinding;

public class Rectangle {
    float x1, y1, y2, x2, x3, y3, x4, y4, b1, b2, b3, b4;
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
        this.b1 = this.y1 + this.x1;
        this.b2 = this.y2 - this.x2;
        this.b3 = this.y3 + this.x3;
        this.b4 = this.y4 - this.x4;
    }
}