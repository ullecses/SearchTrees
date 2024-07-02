import java.io.*;

public class IsBinary {
    public static void main(String[] args) {
        try (PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
             BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            int numberOfVertex = Integer.parseInt(br.readLine());
            int[] nums = new int[numberOfVertex];
            int[] mins = new int[numberOfVertex];
            int[] maxs = new int[numberOfVertex];
            nums[0] = Integer.parseInt(br.readLine());
            mins[0] = Integer.MIN_VALUE;
            maxs[0] = Integer.MAX_VALUE;
            int key, numberOfParent, min, max;
            for (int i = 1; i < numberOfVertex; i++) {
                String[] values = br.readLine().split(" ");
                key = Integer.parseInt(values[0]);
                numberOfParent = Integer.parseInt(values[1]) - 1;
                if (values[2].charAt(0) == 'L') {
                    min = mins[numberOfParent];
                    max = nums[numberOfParent];
                    if (key < min || key >= max) {
                        out.println("NO");
                        return;
                    }
                } else {
                    min = nums[numberOfParent];
                    max = maxs[numberOfParent];
                    if (key < min || key > max) {
                        out.println("NO");
                        return;
                    }
                }
                nums[i] = key;
                mins[i] = min;
                maxs[i] = max;
            }
            out.println("YES");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}