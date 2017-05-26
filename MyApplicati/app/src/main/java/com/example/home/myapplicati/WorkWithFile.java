package com.example.home.myapplicati;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class WorkWithFile {
    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "locations.json";
    private File sdPath;

    WorkWithFile() {
        createDirectory();
    }

    void writeFileSD(String string) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write(string);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String readFileSD() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            while ((str = br.readLine()) != null) {
                return str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void copyImageToSd(Context context) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.zdanie);
        File file = new File(sdPath, "zdanie.png");
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Bitmap readImage(String name, Context context) {
        int px = context.getResources().getDimensionPixelSize(R.dimen.image_size);
        File file = new File(sdPath, name + ".png");
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bitmap = decodeSampledBitmapFromResource(file.getAbsolutePath(), px, px);
        Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        Log.d("log", String.format("Required size = %s, bitmap size = %sx%s, byteCount = %s",
                px, bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
        return rotated;

    }

    private void createDirectory() {
        sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        sdPath.mkdirs();
    }


    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    void saveMarkers() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < AddMarker.placesList.size(); i++) {
            jsonArray.put(AddMarker.placesList.get(i).getJsonObject());
        }
        writeFileSD(jsonArray.toString());
    }
}
