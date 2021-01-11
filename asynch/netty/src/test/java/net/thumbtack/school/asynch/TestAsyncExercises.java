package net.thumbtack.school.asynch;



import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class TestAsyncExercises {

    @Test
    public void rPearsonTest() throws ExecutionException, InterruptedException {
        double[] xArray = {1, 3, 5, 7, 9};
        double[] yArray = {20, 14, 23, 3, 32};
        double rPearson = AsyncExercises.rPearsonAsync(xArray, yArray);

        double testDelta = 1e-5;
        Assert.assertEquals(0.1906, rPearson, testDelta);
    }
}
