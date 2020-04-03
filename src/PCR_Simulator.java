import java.util.Calendar;
import java.util.Random;

public class PCR_Simulator {

    private static int count;
    private int detectedPositive;
    private int testCount;
    private double testSize ;


    public void work() {
        fillCovid(0.01, 10000000);
    }


    private void fillCovid(double positiveRatio, int smapleLength) {

        boolean[] samples = new boolean[smapleLength];
        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        while (count < positiveRatio * samples.length) {
            int val = random.nextInt(smapleLength);
            if (samples[val])
                continue;
            samples[val] = true;
            count++;
        }
        testSize = 128;//positiveRatio * samples.length/100;
        test(samples, 0, samples.length);
        double ratio = (((double) testCount) / smapleLength) * 100;
        System.out.println("total tests:" + testCount + ", ratio:" + Math.round(ratio) + "%");
        System.out.println("positive detected:" + detectedPositive + " from " + count);

    }


    void test(boolean[] samples, int startIndex, int endIndex) {
        if (startIndex >= endIndex)
            return;
        boolean negative = true;
        int l = endIndex - startIndex;
        if (l < testSize) {
            negative = doTest(samples, startIndex, endIndex);
            if (!negative && l == 1) {
                //System.out.println("positive detected:" + startIndex);
                detectedPositive++;
                return;
            }
            if (negative)
                return;
        }
        int endIndex1 = startIndex + l / 2;
        int startIndex2 = endIndex1;

        test(samples, startIndex, endIndex1);
        test(samples, startIndex2, endIndex);
    }

    private boolean doTest(boolean[] samples, int startIndex, int endIndex) {
        testCount++;
        //System.out.println("testing from "+startIndex+ " to "+endIndex);
        for (int i = startIndex; i < endIndex; i++) {
            if (samples[i]) {
                return false;
            }
        }
        return true;
    }
}
