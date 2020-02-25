//Сведения о каждом поезде
//содержат: номер, пункт отправления, пункт назначения, время отправления, время прибытия, стоимость билета
package sample;

import sample.graph.GraphBridge;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;

/**
 * @author Яна
 */
public class Train implements GraphBridge<Station, Train> {

    private int number;
    private Station A;
    private Station B;
    private ZonedDateTime timeOut;
    private ZonedDateTime timeIn;
    private int Price;

    private static SimpleDateFormat formater = new SimpleDateFormat("dd MMM YYYY hh:mm");

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Station getA() {
        return A;
    }

    public void setA(Station pointout) {
        this.A = pointout;
        pointout.addOutputTrain(this);
    }

    public Station getB() {
        return B;
    }

    public void setB(Station pointin) {
        this.B = pointin;
        pointin.addInputTrain(this);
    }

    public ZonedDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(ZonedDateTime timeout) {
        this.timeOut = timeout;
    }

    public ZonedDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(ZonedDateTime timein) {
        this.timeIn = timein;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        this.Price = price;
    }

    public Train(int number, int price, Station pointout, Station pointin, ZonedDateTime timeout, ZonedDateTime timein) {
        setNumber(number);
        setPrice(price);
        setA(pointout);
        setB(pointin);
        setTimeOut(timeout);
        setTimeIn(timein);

        System.out.println(String.format("%20s", A.getName() + " -> " + B.getName()) + ": " + timeIn.toEpochSecond() + " " + timein.toEpochSecond() / 3600);
    }

    @Override
    public String toString() {
        return "" + number + " " + A + " " + timeOut + " " + B + " " + timeIn + " " + Price;
    }

    public String strMinus(int s) {
        if (s == 1) {
            String str = String.format("%3d %15s %", number, A, timeOut, B, timeIn, Price);
            return str;
        }
        return "" + number + " " + A + " " + timeOut + " " + B + " " + timeIn + " " + Price;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Train))
            return false;
        if (((Train) o).number == number && ((Train) o).timeOut == timeOut)
            return true;
        return false;
    }

    public long getWeight() {
        //return weights[number];
        return timeIn.toEpochSecond();
    }

    @Override
    public boolean isExpired(ZonedDateTime time) {
        return this.getTimeOut().isBefore(time);
    }

    @Override
    public int compareTo(Train o) {
        return Long.compare(this.getWeight(), o.getWeight());
    }
}
