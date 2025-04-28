import java.util.ArrayList;
import java.util.Collections;

public class GeneratePuzzle {
    public static void generateSolvablePuzzle() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= 8; i++) { // From 0 to 8 instead of 1 to 9
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        // Check the number of inversions
        // If inversions are odd, swap the first two non-blank tiles
        if (countInversions(numbers) % 2 == 1) {
            for (int i = 0; i < numbers.size(); i++) {
                for (int j = i + 1; j < numbers.size(); j++) {
                    if (numbers.get(i) != 0 && numbers.get(j) != 0) {
                        // Swap
                        Collections.swap(numbers, i, j);
                        // After one swap, break both loops
                        i = numbers.size();
                        break;
                    }
                }
            }
        }

        new EightPuzzle(numbers);
    }

    public static int countInversions(ArrayList<Integer> numbers) {
        int inversions = 0;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                int numI = numbers.get(i);
                int numJ = numbers.get(j);
                if (numI != 0 && numJ != 0 && numI > numJ) {
                    inversions++;
                }
            }
        }

        return inversions;
    }
}
