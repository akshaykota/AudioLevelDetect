package com.kota.akshay.snoredetect;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private CDrawer.CDrawThread mDrawThread;
    private CDrawer mdrawer;

    private OnClickListener listener;
    private Boolean m_bStart = Boolean.valueOf(false);
    private Boolean recording;
    private CSampler sampler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdrawer = (CDrawer) findViewById(R.id.drawer);
        m_bStart = Boolean.valueOf(false);


        while (true)
        {
            recording = Boolean.valueOf(false);
            run();
            System.out.println("mDrawThread NOT NULL");
            System.out.println("recorder NOT NULL");
            return;
        }
    }
    /**
     * Pause the visualizer when the app is paused
     */
    @Override
    protected void onPause()
    {
        System.out.println("onpause");
        sampler.SetRun(Boolean.valueOf(false));
        mDrawThread.setRun(Boolean.valueOf(false));
        sampler.SetSleeping(Boolean.valueOf(true));
        mDrawThread.SetSleeping(Boolean.valueOf(true));
        Boolean.valueOf(false);
        super.onPause();
    }
    /**
     * Resters the visualizer when the app restarts
     */
    @Override
    protected void onRestart()
    {
        m_bStart = Boolean.valueOf(true);
        System.out.println("onRestart");
        super.onRestart();
    }
    /**
     * Resume the visualizer when the app resumes
     */
    @Override
    protected void onResume()
    {
        System.out.println("onresume");
        int i = 0;
        while (true)
        {
            if ((sampler.GetDead2().booleanValue()) && (mdrawer.GetDead2().booleanValue()))
            {
                System.out.println(sampler.GetDead2() + ", " + mdrawer.GetDead2());
                sampler.Restart();
                if (!m_bStart.booleanValue())
                    mdrawer.Restart(Boolean.valueOf(true));
                sampler.SetSleeping(Boolean.valueOf(false));
                mDrawThread.SetSleeping(Boolean.valueOf(false));
                m_bStart = Boolean.valueOf(false);
                super.onResume();
                return;
            }
            try
            {
                Thread.sleep(500L);
                System.out.println("Hang on..");
                i++;
                if (!sampler.GetDead2().booleanValue())
                    System.out.println("sampler not DEAD!!!");
                if (!mdrawer.GetDead2().booleanValue())
                {
                    System.out.println("mDrawer not DeAD!!");
                    mdrawer.SetRun(Boolean.valueOf(false));
                }
                if (i <= 4)
                    continue;
                mDrawThread.SetDead2(Boolean.valueOf(true));
            }
            catch (InterruptedException localInterruptedException)
            {
                localInterruptedException.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart()
    {
        System.out.println("onstart");
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        System.out.println("onstop");
        super.onStop();
    }


    /**
     * Recives the buffer from the sampler
     * @param buffer
     */
    public void setBuffer(short[] paramArrayOfShort)
    {
        mDrawThread = mdrawer.getThread();
        mDrawThread.setBuffer(paramArrayOfShort);
    }

    public void amplitudeDetect(short[] paramArrayOfShort)
    {
        int lenArray = paramArrayOfShort.length;
        for (int i = 0; i < lenArray; i++)
        {
            if(paramArrayOfShort[i] > 0.5)
            {
                TextView textView = (TextView)findViewById(R.id.textView4);
                textView.setText("Yes");
                textView.setTextColor(0xFF0000);
            }
        }
    }

    /**
     * Called by OnCreate to get everything up and running
     */
    public void run()
    {
        try
        {
            if (mDrawThread == null)
            {
                mDrawThread = mdrawer.getThread();
            }
            if (sampler == null)
                sampler = new CSampler(this);
            Context localContext = getApplicationContext();
            Display localDisplay = getWindowManager().getDefaultDisplay();
            Toast localToast = Toast.makeText(localContext, "Please make some noise..", Toast.LENGTH_LONG);
            localToast.setGravity(48, 0, localDisplay.getHeight() / 8);
            localToast.show();
            mdrawer.setOnClickListener(listener);
            if (sampler != null){
                sampler.Init();
                sampler.StartRecording();
                sampler.StartSampling();
            }
        } catch (NullPointerException e) {
            Log.e("Main_Run", "NullPointer: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
