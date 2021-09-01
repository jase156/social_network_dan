package com.halfbyte.danv1.Reconocimiento;

public class RealDoubleFFT extends RealDoubleFFT_Mixed{
    public RealDoubleFFT(int n)
    {
        ndim = n;
        norm_factor = n;
        if(wavetable == null || wavetable.length !=(2*ndim+15))
        {
            wavetable = new double[2*ndim + 15];
        }
        rffti(ndim, wavetable);
    }


    /**
     * Forward real FFT transform.  It computes the discrete transform
     * of a real data sequence.
     *
     * <p>The x parameter is both input and output data.  After the FFT, x
     * contains the transform coefficients used to construct n
     * complex FFT coefficients.
     *
     * <p>The real part of the first complex FFT coefficients is x[0];
     * its imaginary part is 0.  If n is even set m = n/2, if n is odd set
     * m = (n+1)/2, then for k = 1, ..., m-1:
     * <ul>
     * <li>the real part of k-th complex FFT coefficient is x[2 * k];
     * <li>the imaginary part of k-th complex FFT coefficient is x[2 * k - 1].
     * </ul>
     *
     * <p>If n is even, the real of part of (n/2)-th complex FFT
     * coefficient is x[n - 1]; its imaginary part is 0.
     *
     * <p>The remaining complex FFT coefficients can be obtained by the
     * symmetry relation: the (n-k)-th complex FFT coefficient is the
     * conjugate of n-th complex FFT coefficient.
     *
     * @param   x       An array which contains the sequence to be
     *                  transformed.
     */
    public void ft(double[] x) {
        if (x.length != ndim)
            throw new IllegalArgumentException("The length of data can not match that of the wavetable");
        rfftf(ndim, x, wavetable);
    }



    public void ft(double x[], Complex1D y) {
        if (x.length != ndim)
            throw new IllegalArgumentException("The length of data can not match that of the wavetable");
        rfftf(ndim, x, wavetable);

        if(ndim%2 == 0)
        {
            y.x = new double[ndim/2 + 1];
            y.y = new double[ndim/2 + 1];
        }
        else
        {
            y.x = new double[(ndim+1)/2];
            y.y = new double[(ndim+1)/2];
        }


        y.x[0] = x[0];
        y.y[0] = 0.0D;
        for(int i=1; i<(ndim+1)/2; i++)
        {
            y.x[i] = x[2*i-1];
            y.y[i] = x[2*i];
        }
        if(ndim%2 == 0)
        {
            y.x[ndim/2] = x[ndim-1];
            y.y[ndim/2] = 0.0D;
        }

    }


    public void bt(double x[])
    {
        if(x.length != ndim)
            throw new IllegalArgumentException("The length of data can not match that of the wavetable");
        rfftb(ndim, x, wavetable);
    }



    public void bt(Complex1D x, double y[])
    {
        if(ndim%2 == 0)
        {
            if(x.x.length != ndim/2+1)
                throw new IllegalArgumentException("The length of data can not match that of the wavetable");
        }
        else
        {
            if(x.x.length != (ndim+1)/2)
                throw new IllegalArgumentException("The length of data can not match that of the wavetable");
        }

        y[0] = x.x[0];
        for(int i=1; i<(ndim+1)/2; i++)
        {
            y[2*i-1]=x.x[i];
            y[2*i]=x.y[i];
        }
        if(ndim%2 == 0)
        {
            y[ndim-1]=x.x[ndim/2];
        }
        rfftb(ndim, y, wavetable);
    }


    public double norm_factor;
    private double wavetable[];
    private int ndim;
}
