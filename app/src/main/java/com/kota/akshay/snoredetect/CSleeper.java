package com.kota.akshay.snoredetect;

import java.io.PrintStream;

public class CSleeper
        implements Runnable
{
    private Boolean done = Boolean.valueOf(false);
    private MainActivity m_ma;
    private CSampler m_sampler;

    public CSleeper(MainActivity paramMainActivity, CSampler paramCSampler)
    {
        m_ma = paramMainActivity;
        m_sampler = paramCSampler;
    }

    public void run()
    {
        try {
            m_sampler.Init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (true)
            try
            {
                Thread.sleep(1000L);
                System.out.println("Tick");
                continue;
            }
            catch (InterruptedException localInterruptedException)
            {
                localInterruptedException.printStackTrace();
            }
    }
}