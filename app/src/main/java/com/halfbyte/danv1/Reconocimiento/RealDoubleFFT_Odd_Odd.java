package com.halfbyte.danv1.Reconocimiento;

public class RealDoubleFFT_Odd_Odd extends RealDoubleFFT_Even_Odd {
    public RealDoubleFFT_Odd_Odd(int n)
    {
        super(n);
    }

    /**
     * Forward FFT transform of quarter wave data. It computes the coeffients in
     * sine series representation with only odd wave numbers.
     *
     * @param x an array which contains the sequence to be transformed. After FFT,
     * <em>x</em> contains the transform coeffients.
     */
    @Override
    public void ft(double x[])
    {
        sinqf(ndim, x, wavetable);
    }

    /**
     * Backward FFT transform of quarter wave data. It is the unnormalized inverse transform
     * of <em>ft</em>.
     *
     * @param x an array which contains the sequence to be tranformed. After FFT, <em>x</em> contains
     * the transform coeffients.
     */
    @Override
    public void bt(double x[])
    {
        sinqb(ndim, x, wavetable);
    }

    /*-----------------------------------------------
   sinqf: forward sine FFT with odd wave numbers.
  ----------------------------------------------*/
    void sinqf(int n, double x[], double wtable[])
    {
        int     k;
        double  xhold;
        int     kc, ns2;

        if(n==1) return;
        ns2=n / 2;
        for(k=0; k<ns2; k++)
        {
            kc=n-k-1;
            xhold=x[k];
            x[k]=x[kc];
            x[kc]=xhold;
        }
        cosqf(n, x, wtable);
        for(k=1; k<n; k+=2) x[k]=-x[k];
    }

    /*-----------------------------------------------
   sinqb: backward sine FFT with odd wave numbers.
  ----------------------------------------------*/
    void sinqb(int n, double x[], double wtable[])
    {
        int     k;
        double  xhold;
        int     kc, ns2;

        if(n<=1)
        {
            x[0]*=4;
            return;
        }
        ns2=n / 2;
        for(k=1; k<n; k+=2) x[k]=-x[k];
        cosqb(n, x, wtable);
        for(k=0; k<ns2; k++)
        {
            kc=n-k-1;
            xhold=x[k];
            x[k]=x[kc];
            x[kc]=xhold;
        }
    }

    /*
     void sinqi(int n, double wtable[])
     {
          cosqi(n, wtable);
     }
     */
}
