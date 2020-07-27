package com.pyraliron.pyralfishmod.world.gen.noise;

public class PerlinNoiseTwo {
	
	public static float F2  = 0.3660254037844386F;  // 0.5 * (sqrt(3.0) - 1.0)
	public static float G2  = 0.21132486540518713F; // (3.0 - sqrt(3.0)) / 6.0
	
	public static float[][] GRAD3 = {
			{1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0}, 
			{1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1}, 
			{0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1},
			{1,0,-1},{-1,0,-1},{0,-1,1},{0,1,1}};
	
	public static char[] PERM = {
			  151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140,
			  36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120,
			  234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
			  88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71,
			  134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
			  230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161,
			  1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130,
			  116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
			  124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227,
			  47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44,
			  154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98,
			  108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34,
			  242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14,
			  239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121,
			  50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
			  141, 128, 195, 78, 66, 215, 61, 156, 180, 151, 160, 137, 91, 90, 15, 131,
			  13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
			  240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252,
			  219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125,
			  136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158,
			  231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245,
			  40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187,
			  208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198,
			  173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126,
			  255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223,
			  183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167,
			  43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185,
			  112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179,
			  162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199,
			  106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236,
			  205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156,
			  180};
	
	public static float noise2(float x, float y) 
	{
		int i1, j1, I, J, c;
		float s = (x + y) * F2;
		float i = (float)Math.floor(x + s);
		float j = (float)Math.floor(y + s);
		float t = (i + j) * G2;

		float[] xx = new float[3];
		float[] yy = new float[3];
		float[] f = new float[3];
		float[] noise = new float[] {0.0F, 0.0F, 0.0F};
		int[] g = new int[3];

		xx[0] = x - (i - t);
		yy[0] = y - (j - t);

		if (xx[0] > yy[0]) {
			i1 = 1;
		} else {
			i1 = 0;
		};
		if (xx[0] <= yy[0]) {
			j1 = 1;
		} else {
			j1 = 0;
		}

		xx[2] = xx[0] + G2 * 2.0f - 1.0f;
		yy[2] = yy[0] + G2 * 2.0f - 1.0f;
		xx[1] = xx[0] - i1 + G2;
		yy[1] = yy[0] - j1 + G2;

		I = (int) i & 255;
		J = (int) j & 255;
		g[0] = PERM[I + PERM[J]] % 12;
		g[1] = PERM[I + i1 + PERM[J + j1]] % 12;
		g[2] = PERM[I + 1 + PERM[J + 1]] % 12;

		for (c = 0; c <= 2; c++)
			f[c] = 0.5f - xx[c]*xx[c] - yy[c]*yy[c];
		
		for (c = 0; c <= 2; c++)
			if (f[c] > 0)
				noise[c] = f[c]*f[c]*f[c]*f[c] * (GRAD3[g[c]][0]*xx[c] + GRAD3[g[c]][1]*yy[c]);
		
		return (noise[0] + noise[1] + noise[2]) * 70.0f;
	}
	
	public static float snoise2(float x, float y, float octaves) {
		float persistence = 0.5f;
	    float lacunarity = 2.0f;
	    float z = 0.0f;
	    
	    float freq = 1.0f;
        float amp = 1.0f;
        float max = 1.0f;
        float total = noise2(x + z, y + z);
        int i;

        for (i = 1; i < octaves; i++) {
            freq *= lacunarity;
            amp *= persistence;
            max += amp;
            total += noise2(x * freq + z, y * freq + z) * amp;
        }
        return (total/max);
	}

}
