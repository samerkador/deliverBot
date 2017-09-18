package com.example.samer.gpapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RegistrationActivity extends AppCompatActivity
        implements SurfaceHolder.Callback{



//-------------------------------------------------------------------------------------------------------------------------
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
//            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
       }
   };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

//-------------------------------------------------------------------------------------------------------------------------


    SurfaceView cameraView,transparentView;

    private Camera.PictureCallback mPicture;
    boolean cameraFront=false;
    SurfaceHolder holder,holderTransparent;

    int counter=0;
    private Context myContext;
    Camera camera;

    private float RectLeft, RectTop,RectRight,RectBottom ;

    int  deviceHeight,deviceWidth,Center_X,Center_Y,Width_Start,Height_Start;


    ProgressDialog progressDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
//        mControlsView = findViewById(R.id.fullscreen_content_controls);
//
//        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggle();
//            }
//        });



        // //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        setContentView(R.layout.activity_registration);

        myContext = this;
        progressDialog = new ProgressDialog(myContext);


        cameraView = (SurfaceView)findViewById(R.id.CameraView);





        holder = cameraView.getHolder();

        holder.addCallback((SurfaceHolder.Callback) this);

        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            cameraView.setSecure(true);



        // Create second surface with another holder (holderTransparent)

        transparentView = (SurfaceView)findViewById(R.id.TransparentView);



        holderTransparent = transparentView.getHolder();



        holderTransparent.addCallback((SurfaceHolder.Callback) this);

        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);

        transparentView.setZOrderMediaOverlay(true);

        //getting the device heigth and width
        deviceWidth=getScreenWidth();
        deviceWidth= (int) (getResources().getDisplayMetrics().density*deviceWidth);

        deviceHeight=getScreenHeight();
        deviceHeight= (int) (getResources().getDisplayMetrics().density*deviceHeight);

        Center_X=deviceWidth/2;
        Center_Y=deviceHeight/2;

        Width_Start= (int) (Center_X*0.7);
        Height_Start= (int) ( Center_Y*0.8);

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        try {

            synchronized (holder)

            {Draw();}   //call a draw method

            int number=findFrontFacingCamera();
            camera = Camera.open(number);//open a camera

            mPicture = getPictureCallback();
        }



        catch (Exception e) {

            Log.i("Exception", e.toString());

            return;

        }

        Camera.Parameters param;

        param = camera.getParameters();



        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)

        {

            camera.setDisplayOrientation(90);

        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        camera.setParameters(param);



        try {

            camera.setPreviewDisplay(holder);

            camera.startPreview();

        }

        catch (Exception e) {



            return;

        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(); //call method for refress camera
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.release(); //for release a camera
    }


    private void Draw()
    {

        Canvas canvas = holderTransparent.lockCanvas(null);

        Paint  paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.GREEN);

        paint.setStrokeWidth(3);

        RectLeft =Center_X-Width_Start;

        RectTop = Center_Y-Height_Start ;

        RectRight = (int) (RectLeft+(Center_X*0.35));

        RectBottom = (int) (RectTop+(Center_Y*0.7));

        Rect rec=new Rect((int) RectLeft,(int)RectTop,(int)RectRight,(int)RectBottom);

        canvas.drawRect(rec,paint);


        holderTransparent.unlockCanvasAndPost(canvas);

        final Toast Position;


        if(counter==0) {
            Position = Toast.makeText(myContext, "Look To The Left", Toast.LENGTH_LONG);
            Position.show();
            new CountDownTimer(9000, 9800)
            {

                public void onTick(long millisUntilFinished) {Position.show();}
                public void onFinish() {Position.show();}

            }.start();
        }


    }

    public static int getScreenWidth() {

        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }

    public static int getScreenHeight() {

        return Resources.getSystem().getDisplayMetrics().heightPixels;

    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    public void Capture_Image(View view)
    {
        if(counter<4) {
            camera.takePicture(null, null, mPicture);

            //Intent myIntent = new Intent(MainActivity.this, purchase_itemActivity.class);
            //MainActivity.this.startActivity(myIntent);


        }
        counter++;

    }

    public void refreshCamera() {

        if (holder.getSurface() == null) {

            return;

        }



        try {

            camera.stopPreview();

        }



        catch (Exception e) {



        }



        try {



            camera.setPreviewDisplay(holder);

            camera.startPreview();

        }

        catch (Exception e) {

        }

    }





    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    private Camera.PictureCallback getPictureCallback() {


        Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //make a new picture file
                File pictureFile = getOutputMediaFile();

                if (pictureFile == null) {
                    return;
                }
                try {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(pictureFile);


                    fos.write(data);


                    ImageView imageView = (ImageView) findViewById(R.id.imageView_reg);
                    Bitmap image = BitmapFactory.decodeByteArray(data,0,data.length);
                    imageView.setImageBitmap(image);

                    String base64 = BitMapToString(image);
                    String name = pictureFile.getName().substring(0,pictureFile.getName().length() - 4);
                    postImage(name ,base64);

                    fos.close();
                    //Toast toast = Toast.makeText(myContext, "Picture saved: " + pictureFile.getName(), Toast.LENGTH_LONG);
                    // toast.show();

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }



                refreshCamera2();
                //refresh camera to continue previet
            }
        };
        return picture;
    }


    private static File getOutputMediaFile() {
        //make a new file directory inside the "sdcard" folder
        File mediaStorageDir = new File("/sdcard/", "JCG Camera");

        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            //if you cannot make this folder return
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //and make a media file:
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }



    public void refreshCamera2() {
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        final Toast Position;

        if(counter==1)
        {
            Position = Toast.makeText(myContext, "Look To The Camera", Toast.LENGTH_LONG);
            Position.show();
            new CountDownTimer(9000, 9800)
            {

                public void onTick(long millisUntilFinished) {Position.show();}
                public void onFinish() {Position.show();}

            }.start();
        }
        else if(counter==2)
        {
            Position = Toast.makeText(myContext, "Look To The Right", Toast.LENGTH_LONG);
            Position.show();
            new CountDownTimer(9000, 9500)
            {

                public void onTick(long millisUntilFinished) {Position.show();}
                public void onFinish() {Position.show();}

            }.start();
        }
        else if(counter==3)
        {
            Position = Toast.makeText(myContext, "Look To The Camera and move your Chin Slightly Up", Toast.LENGTH_LONG);
            Position.show();
            new CountDownTimer(9000, 9500)
            {

                public void onTick(long millisUntilFinished) {Position.show();}
                public void onFinish() {Position.show();}

            }.start();
        }


        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos) ;

        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    public void postImage( final String name , final String base)
    {

        progressDialog.setMessage("Uploading " + name);
        progressDialog.show();
        String url="http://gpapiandroid.000webhostapp.com/uploadimage.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(RegistrationActivity.this,response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                if (counter >= 4) {
                    finish();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                itemList_holder it = new itemList_holder();

                Map<String, String> params = new HashMap<String, String>();

                params.put("base",base);
                params.put("name",name);
                params.put("id",it.id);
                return params;
            }

        };
        // Adding request to request queue

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance().addToRequestQueue(strReq,"tag");
    }






    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
