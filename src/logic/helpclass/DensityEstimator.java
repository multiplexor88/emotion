/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Estimates points density function (PDF) and finds point with the maximum 
 * occuring probability of input array of data
 * @author Igor Dumchykov
 */
public class DensityEstimator 
{
   private double []	X;                  // Data
   private int		N;                  // Input data length
   private double []	T;                  // Test points for pdf estimating
   private double []    Y;                  // Estimated density (pdf)
   private int		Np;                 // Number of test points 
   private double	Mean;               // Mean (average)
   private double	Var;                // Variance
   private double	StDev;              // Standard deviation
   private double	H;                  // Bandwidth
   private double	maxDensity;         //result
   private int		maxDensityIndex;    //index of max density	

   public DensityEstimator(int nPoints)
   {
       Np = nPoints;
       T = new double[Np];
       Y = new double[Np];
   }

   public int Density(double[] x)
   {
       N = x.length;
       
       int i;
       double a,b,min,max,h;
       
       X = new double[N];
       X = Arrays.copyOf(x, N);
       Arrays.sort(X);
       
       Mean = 0;
       
       for(i = 0; i < N; ++i)
       {
           Mean += (X[i]-Mean)/(i+1.0);
       }
       
       Var = 0;
       for(i = 0; i < N; ++i)
       {
           a = X[i] - Mean;
           X[i] = a;
           Var += a*a;
       }
       
       Var /= N;
       
       StDev = Math.sqrt(Var);
       
       for(i = 0; i < N; ++i)
       {
           X[i] = X[i] / StDev;
       }
       
       double minElement = minIndex(X);
       double maxElement = maxIndex(X);
       
       b = (maxElement - minElement)/(Np - 0.1);
       
       for(i = 0; i < Np; ++i)
       {
           T[i] = minElement + b*(double)i;
       }
       
       i = (int)((N-0.1)/4.0+0.5);
       a = (X[N-1-i] - X[i])/1.34;
       a = Math.min(a, 1.0);
       h = 0.9*a/Math.pow(N, 0.2);
       
       if(h<0.005)
           h = 0.005;
       
       H=h;
       
       gaussian(h);
       
       double maxDens = minIndex(Y);
       Arrays.sort(Y);
       int index = Arrays.binarySearch(Y, maxDens);
       
       return index;
   }

   private void	gaussian(double h)
   {
       double a,b,c;
       
       c = Math.sqrt(Math.PI*2)*N*h;
       
       for(int i = 0; i < N; ++i)
       {
           a=0;
           for(int j = 0; j < N; ++j)
           {
               b = (T[i] - X[j])/h;
               a += Math.exp(-b*b*0.5);
           }
           Y[i] = a / c;    //pdf
       }
   }
   
   private double minIndex(double[] Arr)
   {
       double min = Arr[0];
       for(int i = 1; i < N; ++i)
       {
           if(Arr[i] < min)
           {
               min = Arr[i];
           }
       }
       return min;
   }
   
   private double maxIndex(double[] Arr)
   {
       double max = Arr[0];
       for(int i = 1; i < N; ++i)
       {
           if(Arr[i] > max)
           {
               max = Arr[i];
           }
       }
       return max;
   }
}
