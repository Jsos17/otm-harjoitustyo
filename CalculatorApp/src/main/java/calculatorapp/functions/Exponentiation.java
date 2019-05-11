/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorapp.functions;

/**
 *
 * @author jpssilve
 */
public class Exponentiation implements Function {

    @Override
    public double calculate(double[] arguments) {
        if (arguments[0] == 0 && arguments[1] == 0) {
            return Double.NaN;
        }
        return Math.pow(arguments[0], arguments[1]);
    }

}
