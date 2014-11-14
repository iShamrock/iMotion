package com.iMotion.AR;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by 逢双 on 14-2-22.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
    private Camera camera;

    public CameraPreview(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        try{
            camera.setPreviewDisplay(holder);
        }catch (IOException e){
            e.printStackTrace();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            camera.startPreview();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (camera != null){
                        try {
                        camera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if(success){
                                    System.out.println("自动对焦成功");
                                }
                            }
                        });

                            Thread.sleep(3000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }).start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release();
    }
}
