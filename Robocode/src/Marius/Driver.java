package Marius;

import java.util.List;

public class Driver {

    static double [][] X= {
            {1,2},
            {3,4},
            {5,6},
            {7,8}
    };
    static double [][] Y= {
            {0},{0.33},{0.66},{1} //Zielwerte sollten wegen Aktivierungsfunktion (sigmoid) zwischen 0 und 1 liegen
    };

    public static void main(String[] args) {

        NeuralNetwork nn = new NeuralNetwork(2,10,1);


        List<Double>output;

        nn.fit(X, Y, 50000);
        double [][] input = {
                {1,2},{3,4},{5,6},{7,8},{7,7}
        };
        for(double d[]:input)
        {
            output = nn.predict(d);
            System.out.println(output.toString());
        }

    }

}