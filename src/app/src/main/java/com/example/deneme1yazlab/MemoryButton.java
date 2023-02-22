package com.example.deneme1yazlab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class MemoryButton extends androidx.appcompat.widget.AppCompatButton {

    protected int row;
    protected int column;
    protected int frontDrawableId;
    protected boolean isFlipped = false;
    protected boolean isMatched = false;
    protected Drawable front;
    protected Drawable back;
    protected final int score;
    protected final int house;


    private static final Map<Integer, Integer> paddings;

    static {
        paddings = new HashMap<>();
        paddings.put(2, 170);
        paddings.put(3, 300);
        paddings.put(4, 40);
        paddings.put(5, 260);
        paddings.put(6, 6);
        paddings.put(7, 130);
    }


    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int r, int c, int frontImageDrawableId, String base64, int diff) {
        super(context);
        row = r;
        column = c;
        frontDrawableId = frontImageDrawableId;

        String[] data;
        data = base64.split("&");
        score = Integer.parseInt(data[0]);
        house = Integer.parseInt(data[1]);

        byte[] imageBytes = Base64.decode(data[2], Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);

        front = drawable;
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.logo);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        if ((r == 0 && c == 0)) {
            tempParams.setMargins(paddings.get(diff), paddings.get(diff + 1), 0, 0);
        }
        for (int i = 1; i < diff; i++) {
            if ((r == 0 && c == i)) {
                tempParams.setMargins(0, paddings.get(diff + 1), 0, 0);
            }
            if ((r == i && c == 0)) {
                tempParams.setMargins(paddings.get(diff), 0, 0, 0);
            }
        }

        if (diff == 6) {
            tempParams.width = 170;
            tempParams.height = 170;
        } else if (diff == 4) {
            tempParams.width = 230;
            tempParams.height = 230;
        } else {
            tempParams.width = 340;
            tempParams.height = 340;
        }


        setLayoutParams(tempParams);


    }

    public boolean isMatched() {
        return isMatched;
    }

    public int getScore() {
        return score;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public void flip() {
        if (isMatched) {
            return;
        }

        if (isFlipped) {
            setBackground(back);
            isFlipped = false;
        } else {
            setBackground(front);
            isFlipped = true;
        }
    }


}
