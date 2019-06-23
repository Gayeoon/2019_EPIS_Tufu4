package com.example.rkdus.a2019_epis_tufu4;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * 사용자
 * 나의 동물 등록증 표시하는 액티비티
 * - 이해원
 */
public class MyPageActivity extends AppCompatActivity {

    public static final String TAG = "LogGoGo";
    TessBaseAPI tessBaseAPI;

    Button button1, button2;
    ImageView imageView;
    MyPageScanCamera surfaceView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        imageView = (ImageView) findViewById(R.id.imageView);
        surfaceView = (MyPageScanCamera) findViewById(R.id.surfaceView);
        textView = (TextView) findViewById(R.id.textView);

        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.camera);

        // 권한 확인 및 요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission();
        }

        // 화면 켜진 상태 유지
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });

        tessBaseAPI = new TessBaseAPI();
        String dir = getFilesDir() + "/tesseract";
        if(checkLanguageFile(dir+"/tessdata"))
            tessBaseAPI.init(dir, "eng");
    }

    boolean checkLanguageFile(String dir)
    {
        Log.d(TAG, "checkLanguageFile");
        File file = new File(dir);
        if(file.mkdirs()) {
            if(!file.exists())
                createFiles(dir);
        }
        else if(file.exists()){
            String filePath = dir + "/eng.traineddata";
            File langDataFile = new File(filePath);
            if(!langDataFile.exists())
                createFiles(dir);
        }
        return true;
    }

    private void createFiles(String dir)
    {
        Log.d(TAG, "createFiles");
        AssetManager assetMgr = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = assetMgr.open("tessdata/eng.traineddata");

            String destFile = dir + "/eng.traineddata";

            outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "파일 저장 완료");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void capture()
    {
        surfaceView.capture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = GetRotatedBitmap(bitmap, 90);

                imageView.setImageBitmap(bitmap);

                button1.setEnabled(false);
                button1.setText("텍스트 인식중...");
                new AsyncTess().execute(bitmap);

                camera.startPreview();
            }
        });
    }

    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap = b2;
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

    private class AsyncTess extends AsyncTask<Bitmap, Integer, String> {
        @Override
        protected String doInBackground(Bitmap... mRelativeParams) {
            tessBaseAPI.setImage(mRelativeParams[0]);
            return tessBaseAPI.getUTF8Text();
        }

        protected void onPostExecute(String result) {
            textView.setText(result);
            Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_LONG).show();

            button1.setEnabled(true);
            button1.setText("텍스트 인식");
        }
    }

    /*
    Camera 관련 권한 체크 상태 확인 함수
    @return : boolean(true : 체크한 경우, false : 체크 안한 경우)
     */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        Log.d(TAG, "checkLocationPermission start ");
        if ( checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            // Should we show an explanation?
            // 권한 팝업에서 한번이라도 거부한 경우 true 리턴.
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // ...
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult start ");
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int i=0; i<grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // 하나라도 거부한다면.
                        new AlertDialog.Builder(this).setTitle("알림").setMessage("카메라 권한을 허용해주셔야 해당 서비스를 이용하실 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();
                        return;
                    }
                }
            }
        }
    }

}
