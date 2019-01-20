package com.example.lenovo.mylocationkt

import android.content.Context

/**
 * Convert dp value to px.
 * @param dp      value in dp
 * @param context context
 * @return value in px
 */
fun pxFromDp(dp: Float, context: Context):Float {
    return dp * context.resources.displayMetrics.density;
}