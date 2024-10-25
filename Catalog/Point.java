import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class Point {
    int x;
    BigInteger y;

    Point(int x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
}

public class PolynomialSolver {

    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    private static List<Point> readTestCase(String filePath) {
        List<Point> points = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            JSONTokener tokener = new JSONTokener(fis);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject keys = jsonObject.getJSONObject("keys");

            jsonObject.remove("keys");
            for (String key : jsonObject.keySet()) {
                int x = Integer.parseInt(key);
                JSONObject root = jsonObject.getJSONObject(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                BigInteger y = decodeValue(value, base);
                points.add(new Point(x, y));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

    private static BigInteger findConstantTerm(List<Point> points) {
        BigInteger constantTerm = BigInteger.ZERO;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            BigInteger term = points.get(i).y;
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-points.get(j).x));
                    denominator = denominator.multiply(BigInteger.valueOf(points.get(i).x - points.get(j).x));
                }
            }
            BigInteger value = term.multiply(numerator).divide(denominator);
            constantTerm = constantTerm.add(value);
        }

        return constantTerm;
    }

    public static void main(String[] args) {
        List<Point> testCase1 = readTestCase("testcase1.json");
        List<Point> testCase2 = readTestCase("testcase2.json");

        BigInteger secret1 = findConstantTerm(testCase1);
        BigInteger secret2 = findConstantTerm(testCase2);

        System.out.println("Constant term (Secret) for Test Case 1: " + secret1);
        System.out.println("Constant term (Secret) for Test Case 2: " + secret2);
    }
}
