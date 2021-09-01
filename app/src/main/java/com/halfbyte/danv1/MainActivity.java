package com.halfbyte.danv1;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.halfbyte.danv1.Reconocimiento.RealDoubleFFT;
import com.halfbyte.danv1.lib.PortraitCameraBridgeViewBase;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements PortraitCameraBridgeViewBase.CvCameraViewListener {

    // General
    TextView mensaje;
    boolean rostro = false;
    Random r = new Random();

    //Voz
    int frequency = 8000;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private RealDoubleFFT transformer;
    int blockSize = 256;

    Button startStopButton;
    boolean started = false;

    RecordAudio recordTask;

    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;

    //Camera
    private static final String TAG = "CMA::DetectorActivity";
    private PortraitCameraBridgeViewBase cameraView = null;
    private CascadeClassifier detector = null;
    private boolean camara = true;

    private Mat gray;

    //Botones
    private ImageButton cambiarCamara;
    private ImageButton activarFlash;

    //inicializa data
    private BaseLoaderCallback ocvLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:

                    Log.i(TAG, "Carga de OpenCV correcta...");
                    cameraView.enableView();

                    String path = CascadeHelper.generateXmlPath(this.mAppContext, R.raw.haarcascade_frontalface_alt2);

                    if (path != null) {
                        detector = new CascadeClassifier(path);

                        if (!detector.empty()) {
                            Log.i(TAG, "Cargar clasificador .xml, archivo: " + path);
                        }
                    }
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA}, 1);
        }

        //General
        mensaje = (TextView)findViewById(R.id.Mensaje);
        final ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, 1000);
        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                long currentTime = System.currentTimeMillis();
                mensaje.setText("Hola Javier, ¿Como estas?");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                long currentTime = System.currentTimeMillis();
                if(!rostro){
                    int numero = r.nextInt(2);
                    switch (numero){
                        case 0:
                            mensaje.setText("No te encuentro,¿Sigues ahi?");
                            break;
                        case 1:
                            mensaje.setText("Intenta encender el flash");
                            break;
                    }
                }else{
                    mensaje.setText("Hola Javier, ¿Como estas?");
                    animator.end();
                    cambio();

                }
            }
        });
        animator.start();
        /*

        Thread thread = new Thread(){
            Random r = new Random();
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!rostro){
                                    int numero = r.nextInt(1);
                                    switch (numero){
                                        case 0:
                                            mensaje.setText("No te encuentro,¿Sigues ahi?");
                                            break;
                                        case 1:
                                            mensaje.setText("Intenta encender el flash");
                                            break;
                                    }
                                }else{
                                    mensaje.setText("Hola Javier, ¿Como estas?");
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();*/

        //Reconocimiento

        transformer = new RealDoubleFFT(blockSize);

        imageView = (ImageView) this.findViewById(R.id.ImageView01);
        bitmap = Bitmap.createBitmap((int) 256, (int) 100,
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);


        //Camara
        cameraView = (PortraitCameraBridgeViewBase) findViewById(R.id.cameraView);
        cameraView.setCvCameraViewListener(this);
        cameraView.disableFpsMeter();
        cameraView.setCameraIndex(PortraitCameraBridgeViewBase.CAMERA_ID_FRONT);

        //Botones
        cambiarCamara = findViewById(R.id.CambiarCamara);
        activarFlash =  findViewById(R.id.ActivarFlash);

        cambiarCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.disableView();
                if (camara)
                    cameraView.setCameraIndex(PortraitCameraBridgeViewBase.CAMERA_ID_BACK);
                else
                    cameraView.setCameraIndex(PortraitCameraBridgeViewBase.CAMERA_ID_FRONT);
                cameraView.enableView();
                camara = !camara;
            }
        });

        activarFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }


    @Override
    protected void onResume(){
        super.onResume();

        Log.i(TAG, "Cargando librería .so incluida en proyecto...");
        if (!OpenCVLoader.initDebug()) {

            Log.i(TAG, "Cargando librería con OpenCV Manager...");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, ocvLoaderCallback);

        } else {
            ocvLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        estadoAudio();
    }

    @Override
    public void onPause(){
        super.onPause();
        if (cameraView != null)
            cameraView.disableView();
        estadoAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cameraView != null)
            cameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        gray = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        gray.release();
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        Imgproc.cvtColor(inputFrame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, gray);

        int height = gray.rows();
        int faceSize = Math.round(height * 0.5F);

        Mat temp = gray.clone();
        Core.transpose(gray, temp);
        Core.flip(temp, temp, -1);

        MatOfRect faces = new MatOfRect();
        if (detector != null && !detector.empty()) {
            detector.detectMultiScale(temp, faces, 1.1, 3, 2, new Size(faceSize, faceSize), new Size());
        }

        Rect rec;
        for (Rect rc : faces.toList()) {
            rec = rc.clone();
            rec.y = rc.x;
            rec.x = rc.y;
            rec.width = rc.height;
            rec.height = rc.width;
            Imgproc.rectangle(inputFrame, rec.tl(), rec.br(), new Scalar(0, 255, 0));
            Imgproc.putText(inputFrame, "Puto el que lea", new Point(rec.x, rec.y),Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0,255,0));
            rostro = true;
        }

        return inputFrame;
    }
    /**
     * Chat
     */
    private void cambio(){
        Intent intent = new Intent(this, Chat.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    /**
     * Reconocimiento
     */
    private void estadoAudio(){
        started = true;
        recordTask = new RecordAudio();
        recordTask.execute();
    }
    private class RecordAudio extends AsyncTask<Void, double[], Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                int bufferSize = AudioRecord.getMinBufferSize(frequency,
                        channelConfiguration, audioEncoding);

                AudioRecord audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.MIC, frequency,
                        channelConfiguration, audioEncoding, bufferSize);

                short[] buffer = new short[blockSize];
                double[] toTransform = new double[blockSize];

                audioRecord.startRecording();

                while (started) {
                    int bufferReadResult = audioRecord.read(buffer, 0,
                            blockSize);

                    for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                        toTransform[i] = (double) buffer[i] / 32768.0;
                    }

                    transformer.ft(toTransform);
                    publishProgress(toTransform);
                }

                audioRecord.stop();
            } catch (Throwable t) {
                Log.e("AudioRecord", "Recording Failed");
            }

            return null;
        }

        protected void onProgressUpdate(double[]... toTransform) {
            canvas.drawColor(Color.TRANSPARENT);

            for (int i = 0; i < toTransform[0].length; i++) {
                int x = i;
                int downy = (int) (100 - (toTransform[0][i] * 10));
                int upy = 100;

                canvas.drawLine(x, downy, x, upy, paint);
            }
            imageView.postInvalidate();;
        }
    }
}
