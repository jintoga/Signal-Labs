package com.dat.signallabs;

import android.content.Context;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by DAT on 1/15/2017.
 */

public class DialogBuilder {
    public static MaterialDialog createSimpleProgressDialog(@NonNull Context context) {
        return new MaterialDialog.Builder(context).backgroundColorRes(R.color.white)
            .contentColorRes(R.color.black)
            .content("Loading...")
            .progress(true, 0)
            .build();
    }
}
