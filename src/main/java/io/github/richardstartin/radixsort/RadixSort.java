package io.github.richardstartin.radixsort;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

public class RadixSort {

  public static void basic(int[] data) {
    int[] copy = new int[data.length];
    int[] histogram = new int[257];
    for (int shift = 0; shift < 32; shift += 8) {
      for (int value : data) {
        ++histogram[((value >>> shift) & 0xFF) + 1];
      }
      for (int i = 1; i < histogram.length; ++i) {
        histogram[i] += histogram[i-1];
      }
      for (int value : data) {
        copy[histogram[(value >>> shift) & 0xFF]++] = value;
      }
      System.arraycopy(copy, 0, data, 0, data.length);
      Arrays.fill(histogram, 0);
    }
  }

  public static void basic(int[] data, int[] buffer) {
    int[] histogram = new int[257];
    for (int shift = 0; shift < 32; shift += 8) {
      for (int value : data) {
        ++histogram[((value >>> shift) & 0xFF) + 1];
      }
      for (int i = 1; i < histogram.length; ++i) {
        histogram[i] += histogram[i-1];
      }
      for (int value : data) {
        buffer[histogram[(value >>> shift) & 0xFF]++] = value;
      }
      System.arraycopy(buffer, 0, data, 0, data.length);
      Arrays.fill(histogram, 0);
    }
  }

  public static void unroll(int[] data) {
    int[] copy = new int[data.length];
    int[] histogram = new int[257];

    for (int value : data) {
      ++histogram[(value & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : data) {
      copy[histogram[value & 0xFF]++] = value;
    }
    Arrays.fill(histogram, 0);

    for (int value : copy) {
      ++histogram[((value >>> 8) & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : copy) {
      data[histogram[(value >>> 8) & 0xFF]++] = value;
    }
    Arrays.fill(histogram, 0);

    for (int value : data) {
      ++histogram[((value >>> 16) & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : data) {
      copy[histogram[(value >>> 16) & 0xFF]++] = value;
    }
    Arrays.fill(histogram, 0);

    for (int value : copy) {
      ++histogram[((value >>> 24) & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : copy) {
      data[histogram[(value >>> 24) & 0xFF]++] = value;
    }
  }


  public static void unroll(int[] data, int[] buffer) {
    int[] histogram = new int[257];

    for (int value : data) {
      ++histogram[(value & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : data) {
      buffer[histogram[value & 0xFF]++] = value;
    }
    Arrays.fill(histogram, 0);

    for (int value : buffer) {
      ++histogram[((value >>> 8) & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : buffer) {
      data[histogram[(value >>> 8) & 0xFF]++] = value;
    }
    Arrays.fill(histogram, 0);

    for (int value : data) {
      ++histogram[((value >>> 16) & 0xFF) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : data) {
      buffer[histogram[(value >>> 16) & 0xFF]++] = value;
    }
    Arrays.fill(histogram, 0);

    for (int value : buffer) {
      ++histogram[(value >>> 24) + 1];
    }
    for (int i = 1; i < histogram.length; ++i) {
      histogram[i] += histogram[i-1];
    }
    for (int value : buffer) {
      data[histogram[value >>> 24]++] = value;
    }
  }


  public static void unrollOnePassHistograms(int[] data) {
    int[] copy = new int[data.length];
    int[] histogram1 = new int[257];
    int[] histogram2 = new int[257];
    int[] histogram3 = new int[257];
    int[] histogram4 = new int[257];

    for (int value : data) {
      ++histogram1[(value & 0xFF) + 1];
      ++histogram2[((value >>> 8) & 0xFF) + 1];
      ++histogram3[((value >>> 16) & 0xFF) + 1];
      ++histogram4[(value >>> 24) + 1];
    }
    for (int i = 1; i < histogram1.length; ++i) {
      histogram1[i] += histogram1[i-1];
    }
    for (int value : data) {
      copy[histogram1[value & 0xFF]++] = value;
    }

    for (int i = 1; i < histogram2.length; ++i) {
      histogram2[i] += histogram2[i-1];
    }
    for (int value : copy) {
      data[histogram2[(value >>> 8) & 0xFF]++] = value;
    }

    for (int i = 1; i < histogram3.length; ++i) {
      histogram3[i] += histogram3[i-1];
    }
    for (int value : data) {
      copy[histogram3[(value >>> 16) & 0xFF]++] = value;
    }

    for (int i = 1; i < histogram4.length; ++i) {
      histogram4[i] += histogram4[i-1];
    }
    for (int value : copy) {
      data[histogram4[(value >>> 24)]++] = value;
    }
  }

  public static void unrollOnePassHistograms(int[] data, int[] buffer) {
    int[] histogram1 = new int[257];
    int[] histogram2 = new int[257];
    int[] histogram3 = new int[257];
    int[] histogram4 = new int[257];

    for (int value : data) {
      ++histogram1[(value & 0xFF) + 1];
      ++histogram2[((value >>> 8) & 0xFF) + 1];
      ++histogram3[((value >>> 16) & 0xFF) + 1];
      ++histogram4[(value >>> 24) + 1];
    }
    for (int i = 1; i < histogram1.length; ++i) {
      histogram1[i] += histogram1[i-1];
    }
    for (int value : data) {
      buffer[histogram1[value & 0xFF]++] = value;
    }

    for (int i = 1; i < histogram2.length; ++i) {
      histogram2[i] += histogram2[i-1];
    }
    for (int value : buffer) {
      data[histogram2[(value >>> 8) & 0xFF]++] = value;
    }

    for (int i = 1; i < histogram3.length; ++i) {
      histogram3[i] += histogram3[i-1];
    }
    for (int value : data) {
      buffer[histogram3[(value >>> 16) & 0xFF]++] = value;
    }

    for (int i = 1; i < histogram4.length; ++i) {
      histogram4[i] += histogram4[i-1];
    }
    for (int value : buffer) {
      data[histogram4[value >>> 24]++] = value;
    }
  }


  public static void unrollOnePassHistogramsSkipLevels(int[] data) {
    int[] histogram1 = new int[257];
    int[] histogram2 = new int[257];
    int[] histogram3 = new int[257];
    int[] histogram4 = new int[257];

    for (int value : data) {
      ++histogram1[(value & 0xFF) + 1];
      ++histogram2[((value >>> 8) & 0xFF) + 1];
      ++histogram3[((value >>> 16) & 0xFF) + 1];
      ++histogram4[(value >>> 24) + 1];
    }
    boolean skipLevel1 = canSkipLevel(histogram1, data.length);
    boolean skipLevel2 = canSkipLevel(histogram2, data.length);
    boolean skipLevel3 = canSkipLevel(histogram3, data.length);
    boolean skipLevel4 = canSkipLevel(histogram4, data.length);

    if (skipLevel1 && skipLevel2 && skipLevel3 && skipLevel4) {
      return;
    }
    int[] copy = new int[data.length];

    int[] source = data;
    int[] dest = copy;

    if (!skipLevel1) {
      for (int i = 1; i < histogram1.length; ++i) {
        histogram1[i] += histogram1[i - 1];
      }
      for (int value : source) {
        dest[histogram1[value & 0xFF]++] = value;
      }
      if (!skipLevel2 || !skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel2) {
      for (int i = 1; i < histogram2.length; ++i) {
        histogram2[i] += histogram2[i - 1];
      }
      for (int value : source) {
        dest[histogram2[(value >>> 8) & 0xFF]++] = value;
      }
      if (!skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel3) {
      for (int i = 1; i < histogram3.length; ++i) {
        histogram3[i] += histogram3[i - 1];
      }
      for (int value : data) {
        dest[histogram3[(value >>> 16) & 0xFF]++] = value;
      }
      if (!skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel4) {
      for (int i = 1; i < histogram4.length; ++i) {
        histogram4[i] += histogram4[i - 1];
      }
      for (int value : source) {
        dest[histogram4[value >>> 24]++] = value;
      }
    }
    if (dest != data) {
      System.arraycopy(dest, 0, data, 0, data.length);
    }
  }

  private static boolean canSkipLevel(int[] histogram, int dataSize) {
    for (int count : histogram) {
      if (count == dataSize) {
        return true;
      } else if (count > 0) {
        return false;
      }
    }
    return true;
  }

  public static void unrollOnePassHistogramsSkipLevels(int[] data, int[] buffer) {
    int[] histogram1 = new int[257];
    int[] histogram2 = new int[257];
    int[] histogram3 = new int[257];
    int[] histogram4 = new int[257];

    for (int value : data) {
      ++histogram1[(value & 0xFF) + 1];
      ++histogram2[((value >>> 8) & 0xFF) + 1];
      ++histogram3[((value >>> 16) & 0xFF) + 1];
      ++histogram4[(value >>> 24) + 1];
    }
    boolean skipLevel1 = canSkipLevel(histogram1, data.length);
    boolean skipLevel2 = canSkipLevel(histogram2, data.length);
    boolean skipLevel3 = canSkipLevel(histogram3, data.length);
    boolean skipLevel4 = canSkipLevel(histogram4, data.length);

    if (skipLevel1 && skipLevel2 && skipLevel3 && skipLevel4) {
      return;
    }

    int[] source = data;
    int[] dest = buffer;

    if (!skipLevel1) {
      for (int i = 1; i < histogram1.length; ++i) {
        histogram1[i] += histogram1[i - 1];
      }
      for (int value : source) {
        dest[histogram1[value & 0xFF]++] = value;
      }
      if (!skipLevel2 || !skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel2) {
      for (int i = 1; i < histogram2.length; ++i) {
        histogram2[i] += histogram2[i - 1];
      }
      for (int value : source) {
        dest[histogram2[(value >>> 8) & 0xFF]++] = value;
      }
      if (!skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel3) {
      for (int i = 1; i < histogram3.length; ++i) {
        histogram3[i] += histogram3[i - 1];
      }
      for (int value : data) {
        dest[histogram3[(value >>> 16) & 0xFF]++] = value;
      }
      if (!skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel4) {
      for (int i = 1; i < histogram4.length; ++i) {
        histogram4[i] += histogram4[i - 1];
      }
      for (int value : source) {
        dest[histogram4[value >>> 24]++] = value;
      }
    }
    if (dest != data) {
      System.arraycopy(dest, 0, data, 0, data.length);
    }
  }


  public static void unrollOnePassHistogramsSkipLevelsWithDetection(int[] data) {
    int[] histogram1 = new int[257];
    int[] histogram2 = new int[257];
    int[] histogram3 = new int[257];
    int[] histogram4 = new int[257];

    int max1 = 0;
    int max2 = 0;
    int max3 = 0;
    int max4 = 0;

    int mask1 = 0;
    int mask2 = 0;
    int mask3 = 0;
    int mask4 = 0;

    for (int value : data) {
      int i1 = (value & 0xFF) + 1;
      int i2 = ((value >>> 8) & 0xFF) + 1;
      int i3 = ((value >>> 16) & 0xFF) + 1;
      int i4 = (value >>> 24) + 1;
      ++histogram1[i1];
      ++histogram2[i2];
      ++histogram3[i3];
      ++histogram4[i4];
      max1 = Math.max(max1, i1);
      max2 = Math.max(max2, i2);
      max3 = Math.max(max3, i3);
      max4 = Math.max(max4, i4);
      mask1 |= (i1 - max1);
      mask2 |= (i2 - max2);
      mask3 |= (i3 - max3);
      mask4 |= (i4 - max4);
    }
    boolean skipLevel1 = mask1 > 0 || canSkipLevel(histogram1, data.length);
    boolean skipLevel2 = mask2 > 0 || canSkipLevel(histogram2, data.length);
    boolean skipLevel3 = mask3 > 0 || canSkipLevel(histogram3, data.length);
    boolean skipLevel4 = mask4 > 0 || canSkipLevel(histogram4, data.length);

    if (skipLevel1 && skipLevel2 && skipLevel3 && skipLevel4) {
      return;
    }
    int[] copy = new int[data.length];
    int[] source = data;
    int[] dest = copy;

    if (!skipLevel1) {
      for (int i = 1; i < histogram1.length; ++i) {
        histogram1[i] += histogram1[i - 1];
      }
      for (int value : source) {
        dest[histogram1[value & 0xFF]++] = value;
      }
      if (!skipLevel2 || !skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel2) {
      for (int i = 1; i < histogram2.length; ++i) {
        histogram2[i] += histogram2[i - 1];
      }
      for (int value : source) {
        dest[histogram2[(value >>> 8) & 0xFF]++] = value;
      }
      if (!skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel3) {
      for (int i = 1; i < histogram3.length; ++i) {
        histogram3[i] += histogram3[i - 1];
      }
      for (int value : data) {
        dest[histogram3[(value >>> 16) & 0xFF]++] = value;
      }
      if (!skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel4) {
      for (int i = 1; i < histogram4.length; ++i) {
        histogram4[i] += histogram4[i - 1];
      }
      for (int value : source) {
        dest[histogram4[value >>> 24]++] = value;
      }
    }
    if (dest != data) {
      System.arraycopy(dest, 0, data, 0, data.length);
    }
  }

  public static void unrollOnePassHistogramsSkipLevelsWithDetection(int[] data, int[] buffer) {
    int[] histogram1 = new int[257];
    int[] histogram2 = new int[257];
    int[] histogram3 = new int[257];
    int[] histogram4 = new int[257];

    int max1 = 0;
    int max2 = 0;
    int max3 = 0;
    int max4 = 0;

    int mask1 = 0;
    int mask2 = 0;
    int mask3 = 0;
    int mask4 = 0;

    for (int value : data) {
      int i1 = (value & 0xFF) + 1;
      int i2 = ((value >>> 8) & 0xFF) + 1;
      int i3 = ((value >>> 16) & 0xFF) + 1;
      int i4 = (value >>> 24) + 1;
      ++histogram1[i1];
      ++histogram2[i2];
      ++histogram3[i3];
      ++histogram4[i4];
      max1 = Math.max(max1, i1);
      max2 = Math.max(max2, i2);
      max3 = Math.max(max3, i3);
      max4 = Math.max(max4, i4);
      mask1 |= (i1 - max1);
      mask2 |= (i2 - max2);
      mask3 |= (i3 - max3);
      mask4 |= (i4 - max4);
    }
    boolean skipLevel1 = mask1 > 0 || canSkipLevel(histogram1, data.length);
    boolean skipLevel2 = mask2 > 0 || canSkipLevel(histogram2, data.length);
    boolean skipLevel3 = mask3 > 0 || canSkipLevel(histogram3, data.length);
    boolean skipLevel4 = mask4 > 0 || canSkipLevel(histogram4, data.length);

    if (skipLevel1 && skipLevel2 && skipLevel3 && skipLevel4) {
      return;
    }

    int[] source = data;
    int[] dest = buffer;

    if (!skipLevel1) {
      for (int i = 1; i < histogram1.length; ++i) {
        histogram1[i] += histogram1[i - 1];
      }
      for (int value : source) {
        dest[histogram1[value & 0xFF]++] = value;
      }
      if (!skipLevel2 || !skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel2) {
      for (int i = 1; i < histogram2.length; ++i) {
        histogram2[i] += histogram2[i - 1];
      }
      for (int value : source) {
        dest[histogram2[(value >>> 8) & 0xFF]++] = value;
      }
      if (!skipLevel3 || !skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel3) {
      for (int i = 1; i < histogram3.length; ++i) {
        histogram3[i] += histogram3[i - 1];
      }
      for (int value : data) {
        dest[histogram3[(value >>> 16) & 0xFF]++] = value;
      }
      if (!skipLevel4) {
        int[] tmp = dest;
        dest = source;
        source = tmp;
      }
    }

    if (!skipLevel4) {
      for (int i = 1; i < histogram4.length; ++i) {
        histogram4[i] += histogram4[i - 1];
      }
      for (int value : source) {
        dest[histogram4[value >>> 24]++] = value;
      }
    }
    if (dest != data) {
      System.arraycopy(dest, 0, data, 0, data.length);
    }
  }
}
