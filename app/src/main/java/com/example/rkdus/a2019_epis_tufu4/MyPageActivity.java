package com.example.rkdus.a2019_epis_tufu4;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.BradleyLocalThreshold;

/*
 * 사용자
 * 나의 동물 등록증 표시하는 액티비티
 * - 이해원
 */
public class MyPageActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    final static int CHECK_PICTURE = 200;
    static final int REQUEST_TAKE_PICTURE = 1;

    TessBaseAPI tessBaseAPI;
    Button cameraBtn;
    ImageView imageView;
    Bitmap resultBitmap;
    TextView textView;

    String currentPicturePath;
    Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 권한 확인 및 요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission();

        }
        else
            init();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
//                Intent intent = new Intent(getApplicationContext(), MyPageCameraActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void init() {
        setContentView(R.layout.activity_my_page);

        // 뷰 정의
        cameraBtn = (Button) findViewById(R.id.cameraBtn);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.resultTextView);

        // Tesseract 설정
        Log.d(TAG, "init end ");
        tessBaseAPI = new TessBaseAPI();
        String dir = getFilesDir() + "/tesseract";
        String[] language = {"eng"};
        if(checkLanguageFile(dir + "/tessdata", language)) {
            Log.d(TAG, "tessBaseAPI start ");
            tessBaseAPI.init(dir, "eng");
            Log.d(TAG, "tessBaseAPI init end ");
        }
    }

    /*
    Tesseract API 중 인식할 문자의 언어 체크하는 함수
     */
    boolean checkLanguageFile(String dir, String[] language)
    {
        Log.d(TAG, "checkLanguageFile");
        File file = new File(dir);
        file.mkdirs();
        for (int i = 0; i < language.length; i++) {
            String lang = language[i];
            String filePath = dir + "/" + lang + ".traineddata"; // ex) dir/kor.traineddata
            File langDataFile = new File(filePath);
            if(!langDataFile.exists())
                createFiles(filePath, lang);
        }
        return true;
    }

    private void createFiles(String destfile, String language)
    {
        Log.d(TAG, "createFiles");
        AssetManager assetMgr = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = assetMgr.open("tessdata/" + language + ".traineddata");   // ex) tessdata/kor.trainneddata
            outputStream = new FileOutputStream(destfile);

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

    /*
    tesseract API를 사용하여 OCR 진행
     */
    public void processImage() {
        Log.d(TAG, "processImage");
        String OCRresult = null;
        tessBaseAPI.setImage(resultBitmap);
        Log.d(TAG, "setImage");
        OCRresult = tessBaseAPI.getUTF8Text();
        Log.d(TAG, "getUTF8Text");
        textView.setText(OCRresult);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MyCard_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPicturePath = image.getAbsolutePath();
        return image;
    }

    /*
    인텐트로 카메라로 사진을 찍으라고 요청을 보내는 함수
    Uri값도 같이 넘김.
     */
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    /*
    이미지 회전시키는 함수
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /*
    정면으로 회전시킨 비트맵 이미지 반환 함수
     */
    /*
     *bitmap 흑백으로 변환
     */
    private Bitmap bitmapGrayScale(final Bitmap orgBitmap){
        Log.i("gray", "in");
        int width, height;
        height = orgBitmap.getHeight();
        width = orgBitmap.getWidth();
        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(orgBitmap, 0, 0, paint);
        return bmpGrayScale;

    }

    // 카메라로 촬영한 영상을 가져오는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
//                case REQUEST_TAKE_PICTURE:
//                    if (resultCode == RESULT_OK && intent.hasExtra("data")) {
//                        Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
//                        if (bitmap != null) {
//                            // imageView.setImageBitmap(bitmap);
//                            // 비트맵 -> Byte Array로 변경하여 인텐트 담아 전송
////                            Intent myPageImageIntent = new Intent(getApplicationContext(), MyPageCameraActivity.class);
////                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
////                            byte[] byteArray = stream.toByteArray();
////                            intent.putExtra("image", byteArray);
////                            startActivityForResult(myPageImageIntent, CHECK_PICTURE);
//                        }
//                    }
//                    break;
                case REQUEST_TAKE_PICTURE:
                    File file = new File(currentPicturePath);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            // 촬영한 비트맵을 정면으로 보이기 위해 회전시키는 작업
                            ExifInterface ei = new ExifInterface(currentPicturePath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                            switch(orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    Log.d(TAG, "90도");
                                    resultBitmap = rotateImage(bitmap, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    Log.d(TAG, "180도");
                                    resultBitmap = rotateImage(bitmap, 180);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    Log.d(TAG, "270도");
                                    resultBitmap = rotateImage(bitmap, 270);
                                    break;
                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    resultBitmap = bitmap;
                            }
                            // resultBitmap = bitmapGrayScale(resultBitmap);
                            FastBitmap fb = new FastBitmap(resultBitmap);
                            fb.toGrayscale();
                            BradleyLocalThreshold bradley = new BradleyLocalThreshold();
                            bradley.applyInPlace(fb);
                            resultBitmap = fb.toBitmap();

//                            Paint paint = new Paint();
//                            paint.setColorFilter(new ColorMatrixColorFilter(createThresholdMatrix(128)));
//                            Canvas c = new Canvas(resultBitmap);
//                            c.drawBitmap(resultBitmap, 0, 0, paint);

                            imageView.setImageBitmap(resultBitmap);

                            Toast.makeText(getApplicationContext(), "이미지 출력 완료! 문자 추출합니다.", Toast.LENGTH_SHORT).show();
                            cameraBtn.setEnabled(false);
                            cameraBtn.setText("텍스트 인식중...");
                            new AsyncTess().execute(resultBitmap);
                            // processImage();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CHECK_PICTURE:
                    break;
            }
        }
        else {
            if(intent.hasExtra("result")) {
                switch (intent.getStringExtra("result")) {
                    case "300":
                        Toast.makeText(getApplicationContext(), "Intent에 이미지 값이 담겨오지 않은 경우", Toast.LENGTH_LONG).show();
                        break;
                    case "301":
                        Toast.makeText(getApplicationContext(), "Intent가 null인 경우", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }

    // matrix that changes gray scale picture into black and white at given threshold.
// It works this way:
// The matrix after multiplying returns negative values for colors darker than threshold
// and values bigger than 255 for the ones higher.
// Because the final result is always trimed to bounds (0..255) it will result in bitmap made of black and white pixels only
    public static ColorMatrix createThresholdMatrix(int threshold) {
        ColorMatrix matrix = new ColorMatrix(new float[] {
                85.f, 85.f, 85.f, 0.f, -255.f * threshold,
                85.f, 85.f, 85.f, 0.f, -255.f * threshold,
                85.f, 85.f, 85.f, 0.f, -255.f * threshold,
                0f, 0f, 0f, 1f, 0f
        });
        return matrix;
    }

    private class AsyncTess extends AsyncTask<Bitmap, Integer, String> {
        @Override
        protected String doInBackground(Bitmap... mRelativeParams) {
            Log.d(TAG, "AsyncTess doinBackground");
            tessBaseAPI.setImage(mRelativeParams[0]);
            return tessBaseAPI.getUTF8Text();
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "result : " + result);
            String resultText = result.replaceAll("[^ㄱ-ㅎ가-힣a-zA-Z0-9]+", " ");  // 한글, 영어, 숫자 빼고 다 지우기
            textView.setText(result);
            Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_LONG).show();

            cameraBtn.setEnabled(true);
            cameraBtn.setText("텍스트 인식");
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
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
            {
                // ...
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else {
            init();
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
