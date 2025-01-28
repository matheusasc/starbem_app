package com.ifam.pdm.starbemapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ifam.pdm.starbemapp.R;

import java.io.ByteArrayOutputStream;

public class ProfileImageManager {

    public static void loadProfileImage(ImageView imageView, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String imageBase64 = sharedPreferences.getString("profileImage", null);

        if (imageBase64 != null) {
            byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            Glide.with(context)
                    .load(bitmap)
                    .apply(RequestOptions.circleCropTransform()) // Aplica recorte circular
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_profile_picture)  // Imagem padrão
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
    }

    public static void saveProfileImage(Bitmap bitmap, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Log.d("ProfileImageManager", "Imagem codificada: " + encodedImage); // Log para verificar

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImage", encodedImage);
        editor.apply();
    }

}
