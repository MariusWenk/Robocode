package Marius;
import java.util.List;

public class NeuralNetwork {

    Matrix weights_ih,weights_ho , bias_h,bias_o;
    double l_rate=0.01;

    public NeuralNetwork(int i,int h,int o) { // in, hidden, out Layer Neuron amount (3 Layer, benötigt 2 Übergangsmatrizen)
        weights_ih = new Matrix(h,i); // Zufällig mit Werten aus [-1,1] initialisiert
        weights_ho = new Matrix(o,h);

        bias_h= new Matrix(h,1);
        bias_o= new Matrix(o,1);

    }

    public List<Double> predict(double[] X) // Berechnug eines Ausgabewertes für gegebene Eingabe X
    {
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input); // i muss mit Länge von X übereinstimmen; Übertragungsfunktion hier durch Matrix weigths_ih
        hidden.add(bias_h); // Schwellwert bias_h
        hidden.sigmoid(); // Anwendung Aktivierungsfunktion nach Addition mit Schwellwert bias_h

        Matrix output = Matrix.multiply(weights_ho,hidden);
        output.add(bias_o);
        output.sigmoid();

        return output.toArray();
    }


    public void fit(double[][]X,double[][]Y,int epochs)
    {
        for(int i=0;i<epochs;i++)
        {
            int sampleN =  (int)(Math.random() * X.length );
            this.train(X[sampleN], Y[sampleN]);
        }
    }

    public void train(double [] X,double [] Y)
    {
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden.sigmoid();

        Matrix output = Matrix.multiply(weights_ho,hidden);
        output.add(bias_o);
        output.sigmoid();

        Matrix target = Matrix.fromArray(Y);

        Matrix error = Matrix.subtract(target, output);
        Matrix gradient = output.dsigmoid();
        gradient.multiply(error);
        gradient.multiply(l_rate);

        Matrix hidden_T = Matrix.transpose(hidden);
        Matrix who_delta =  Matrix.multiply(gradient, hidden_T);

        weights_ho.add(who_delta);
        bias_o.add(gradient);

        Matrix who_T = Matrix.transpose(weights_ho);
        Matrix hidden_errors = Matrix.multiply(who_T, error);

        Matrix h_gradient = hidden.dsigmoid();
        h_gradient.multiply(hidden_errors);
        h_gradient.multiply(l_rate);

        Matrix i_T = Matrix.transpose(input);
        Matrix wih_delta = Matrix.multiply(h_gradient, i_T);

        weights_ih.add(wih_delta);
        bias_h.add(h_gradient);

    }

    public Matrix getWeights_ih(){
        return weights_ih;
    }

    public Matrix getWeights_ho(){
        return weights_ho;
    }

    public Matrix getBias_h(){
        return bias_h;
    }

    public Matrix getBias_o(){
        return bias_o;
    }

    public void setWeights_ih(Matrix weights_ih){
        this.weights_ih = weights_ih;
    }

    public void setWeights_ho(Matrix weights_oh){
        this.weights_ho = weights_oh;
    }

    public void setBias_h(Matrix bias_h){
        this.bias_h = bias_h;
    }

    public void setBias_o(Matrix bias_o){
        this.bias_o = bias_o;
    }


}
