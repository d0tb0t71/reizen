package com.example.reizen.interfaces;

import android.view.View;

public interface OnClickListeners {

    <T> void onClick (T model);
    <T> void onOptionMenuClicked(T model, View v);

}
