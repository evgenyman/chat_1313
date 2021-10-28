import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> cars = new ArrayList<>();
        cars.add("Mazda");
        cars.add("Toyota");
        System.out.println(cars);
        for (int i = 0; i < cars.size(); i++) {
            System.out.println("Марка: "+ cars.get(i));
        }
    }
}
