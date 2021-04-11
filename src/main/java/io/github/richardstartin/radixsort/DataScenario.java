package io.github.richardstartin.radixsort;

import java.util.Arrays;
import java.util.SplittableRandom;

public enum DataScenario {
  UNIFORM {
    @Override
    int[] generate(int size, int seed, int mask) {
      SplittableRandom random = new SplittableRandom(seed);
      int[] data = new int[size];
      for (int i = 0; i < data.length; ++i) {
        data[i] = random.nextInt() & mask;
      }
      return data;
    }
  },
  CONTIGUOUS {
    @Override
    int[] generate(int size, int seed, int mask) {
      int[] data = new int[size];
      for (int i = 0; i < data.length; ++i) {
        data[i] = i & mask;
      }
      return data;
    }
  },
  CONTIGUOUS_REVERSE {
    @Override
    int[] generate(int size, int seed, int mask) {
      int[] data = new int[size];
      for (int i = 0; i < size; ++i) {
        data[i] = size - i;
      }
      return data;
    }
  },
  ALMOST_CONTIGUOUS {
    @Override
    int[] generate(int size, int seed, int mask) {
      int[] data = new int[size];
      for (int i = 0; i < data.length; ++i) {
        data[i] = (i ^ 0xFF) & mask;
      }
      return data;
    }
  },
  SORTED {
    @Override
    int[] generate(int size, int seed, int mask) {
      SplittableRandom random = new SplittableRandom(seed);
      int[] data = new int[size];
      int x = (int) (Math.log(random.nextDouble())/Math.log(0.999)) + 1;
      for (int i = 0; i < size; ++i) {
        data[i] = x & mask;
        x += (int) (Math.log(random.nextDouble())/Math.log(0.999)) + 1;
      }
      return data;
    }
  },
  ALMOST_SORTED {
    @Override
    int[] generate(int size, int seed, int mask) {
      int[] data = new int[size];
      SplittableRandom random = new SplittableRandom(seed);
      int x = (int) (Math.log(random.nextDouble())/Math.log(0.999)) + 1;
      for (int i = 0; i < size; ++i) {
        data[i] = (x ^ 0xFF) & mask;
        x += (int) (Math.log(random.nextDouble())/Math.log(0.999)) + 1;
      }
      return data;
    }
  },
  EXP {
    @Override
    int[] generate(int size, int seed, int mask) {
      SplittableRandom random = new SplittableRandom(seed);
      int[] data = new int[size];
      for (int i = 0; i < data.length; ++i) {
        int x = (int) (Math.log(random.nextDouble())/Math.log(0.9999)) + 1;
        data[i] = x & mask;
      }
      return data;
    }
  },
  ;
  abstract int[] generate(int size, int seed, int mask);
}
