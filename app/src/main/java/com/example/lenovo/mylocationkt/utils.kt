package com.example.lenovo.mylocationkt

import android.content.Context

fun pxFromDp(dp: Float, context: Context):Float {
    return dp * context.resources.displayMetrics.density;
}