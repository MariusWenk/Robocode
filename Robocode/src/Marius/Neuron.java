package Marius;
import java.util.ArrayList;

public class Neuron {
    double[] weight = new double[100];
    double borderValue;
    double bias = 0;
    ArrayList<Neuron> sendToArrayList;

    public Neuron(ArrayList<Neuron> sendTo)
    {
        this.sendToArrayList = sendTo;
    }

    public void train(double[] input, int result)
    {
        double alpha = 0.01;
        int step = fire(input);
        if(step - result < 0.1)
        {
            return;
        }
        for(int i = 0; i < weight.length; i++)
        {
            weight[i] = weight[i] + alpha * input[i] * (result - step);
        }
        train(input, result);
    }

    public int fire(double[] input)
    {
        double sum = 0;
        double length = Math.min(input.length, weight.length);
        for(int i = 0; i < length; i++)
        {
            sum += weight[i] * input[i];
        }
        sum += bias;
        if(borderValue <= sum)
        {
            return 1;
        }
        else{
            return 0;
        }
    }

    public int fire(int[] input)
    {
        double sum = 0;
        double length = Math.min(input.length, weight.length);
        for(int i = 0; i < length; i++)
        {
            sum += weight[i] * input[i];
        }
        sum += bias;
        if(borderValue <= sum)
        {
            return 1;
        }
        else{
            return 0;
        }
    }
}
