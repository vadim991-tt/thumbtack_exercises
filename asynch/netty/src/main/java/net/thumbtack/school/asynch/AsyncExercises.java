package net.thumbtack.school.asynch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.IntStream;

public class AsyncExercises {


    // 1. Реализовать pipeline для параллельного вычисления коэффициента корреляции двух числовых массивов
    public static double rPearsonAsync(double[] xArray, double[] yArray) throws ExecutionException, InterruptedException {
        CompletableFuture<Double> firstAverage = CompletableFuture.supplyAsync(
                () -> Arrays.stream(xArray).average().orElse(Double.NaN));

        CompletableFuture<Double> secondAverage = CompletableFuture.supplyAsync(
                () -> Arrays.stream(yArray).average().orElse(Double.NaN));

        Double xAvg = firstAverage.get();
        Double yAvg = secondAverage.get();


        // (Xi - xAvg)
        CompletableFuture<double[]> firstDiff = CompletableFuture.supplyAsync(
                () -> Arrays.stream(xArray).map((s) -> s - xAvg).toArray()
        );

        // (Yi - yAvg)
        CompletableFuture<double[]> secondDiff = CompletableFuture.supplyAsync(
                () -> Arrays.stream(yArray).map((s) -> s - yAvg).toArray()
        );

        double[] xArrayDiff = firstDiff.get();
        double[] yArrayDiff = secondDiff.get();

        // Σ(Xi - xAvg)^2
        CompletableFuture<Double> xSum = CompletableFuture.supplyAsync(
                () -> Arrays.stream(xArrayDiff).map((s) -> s * s).sum()
        );

        // Σ(Yi - yAvg)^2
        CompletableFuture<Double> ySum = CompletableFuture.supplyAsync(
                () -> Arrays.stream(yArrayDiff).map((s) -> s * s).sum()
        );

        CompletableFuture<Double> numerator = CompletableFuture.supplyAsync(() -> sumMultDiff(xArrayDiff, yArrayDiff));
        CompletableFuture<Double> denominator = xSum.thenCombine(ySum, (d1, d2) -> Math.sqrt(d1 * d2));

        CompletableFuture<Double> rPearson = numerator.thenCombine(denominator, (d1, d2) -> d1 / d2);
        return rPearson.get();
    }

    private static double sumMultDiff(double[] xDiff, double[] yDiff) {
        DoubleBinaryOperator operator = ((x, y) -> x * y);
        return IntStream.range(0, xDiff.length)
                .mapToDouble(index -> operator.applyAsDouble(xDiff[index], yDiff[index])).sum();
    }

    // 2. С помощью CompletableFuture реализовать параллельное чтение, затем записать два файла с суммой и произведением этих чисел
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Integer>> firstFutureList = CompletableFuture
                .supplyAsync(() -> readFile("file1.txt"))
                .handle((r, e) -> {
                    System.out.println("result " + r + "\nexception " + e);
                    return r;
                });

        CompletableFuture<List<Integer>> secondFutureList = CompletableFuture
                .supplyAsync(() -> readFile("file2.txt"))
                .handle((r, e) -> {
                    System.out.println("result " + r + "\nexception " + e);
                    return r;
                });

        List<Integer> firstList = firstFutureList.get();
        List<Integer> secondList = secondFutureList.get();

        CompletableFuture<Boolean> sum = CompletableFuture
                .supplyAsync(() -> writeSum(firstList, secondList))
                .handle((r, e) -> {
                    System.out.println("result " + r + "\nexception " + e);
                    return r;
                });

        CompletableFuture<Boolean> mult = CompletableFuture
                .supplyAsync(() -> writeMult(firstList, secondList))
                .handle((r, e) -> {
                    System.out.println("result " + r + "\nexception " + e);
                    return r;
                });

        sum.get();
        mult.get();
    }

    private static List<Integer> readFile(String filename) {
        System.out.println("solver thread: " + Thread.currentThread().getId());
        List<Integer> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static Boolean writeSum(List<Integer> firstList, List<Integer> secondList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("file3.txt"))) {
            for (int i = 0; i < firstList.size(); i++) {
                bw.write(String.valueOf(firstList.get(i) + secondList.get(i)));
                bw.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static Boolean writeMult(List<Integer> firstList, List<Integer> secondList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("file4.txt"))) {
            for (int i = 0; i < firstList.size(); i++) {
                bw.write(String.valueOf(firstList.get(i) * secondList.get(i)));
                bw.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    private static void createFiles() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("file1.txt"))) {
            for (int i = 1; i < 11; i++) {
                bw.write(String.valueOf(i));
                bw.newLine();
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("file2.txt"))) {
            for (int i = 1000; i < 11000; i = i + 1000) {
                bw.write(String.valueOf(i));
                bw.newLine();
            }
        }
    }


}
