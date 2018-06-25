package net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata;

/**
 * Enum that contains a member for each supported timing length of a step.
 *
 * @author Dan
 */

public enum Timing {

    L1ST, L4TH, L8TH, L12TH, L16TH, L24TH, L32ND, L48TH, L64TH;

    public static Timing fromLineIndexAndMeasureSize(int lineIndex, int numberLinesInMeasure) {
        int length;
        if (lineIndex != 0) {
            int gcd = getGCD(numberLinesInMeasure, lineIndex);
            length = numberLinesInMeasure / gcd;
        } else {
            return Timing.L1ST;
        }

        switch (length) {
            case 8:
                return Timing.L8TH;
            case 12:
                return Timing.L12TH;
            case 16:
                return Timing.L16TH;
            case 24:
                return Timing.L24TH;
            case 32:
                return Timing.L32ND;
            case 48:
                return Timing.L48TH;
            case 64:
                return Timing.L64TH;
            default:
                return Timing.L4TH;
        }
    }

    private static int getGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
