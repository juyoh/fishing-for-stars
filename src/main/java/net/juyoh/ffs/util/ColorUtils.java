package net.juyoh.ffs.util;

public class ColorUtils {
    public static int lerp(int argbA, int argbB, float t) {
        t = Math.min(Math.max(t, 0), 1.0f);

        int aA = (argbA >> 24) & 0xFF;
        int rA = (argbA >> 16) & 0xFF;
        int gA = (argbA >> 8)  & 0xFF;
        int bA = argbA & 0xFF;

        int aB = (argbB >> 24) & 0xFF;
        int rB = (argbB >> 16) & 0xFF;
        int gB = (argbB >> 8)  & 0xFF;
        int bB = argbB & 0xFF;

        int a = (int)(aA + (aB - aA) * t);
        int r = (int)(rA + (rB - rA) * t);
        int g = (int)(gA + (gB - gA) * t);
        int b = (int)(bA + (bB - bA) * t);

        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
}
