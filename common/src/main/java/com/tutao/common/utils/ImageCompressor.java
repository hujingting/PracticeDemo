package com.tutao.common.utils;

import android.graphics.Bitmap;

/**
 * Created by jingting on 2017/9/26.
 */

public class ImageCompressor {
    private static final int MAX_WIDTH_AND_HEIGHT = 1000;

//    public static String compressImage(String filePath) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = 1;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
////        String standardImagePath = FileUtils.getImagePath(ApplicationContextHelper.getContext(), UUID.randomUUID().toString() + ".png");
//        BitmapUtil.saveBitmap(bitmap, new File(standardImagePath));
//        return standardImagePath;
//    }

    /**∑
     * 按质量压缩
     *
     * @param src         源图片
     * @param maxByteSize 允许最大值字节数
     * @param recycle     是否回收
     * @return 质量压缩压缩过的图片
     */
//    public static String compressByQuality(Bitmap src, long maxByteSize, boolean recycle) {
//        if (isEmptyBitmap(src) || maxByteSize <= 0) return null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int quality = 100;
//        src.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//        while (baos.toByteArray().length > maxByteSize && quality >= 0) {
//            baos.reset();
//            src.compress(Bitmap.CompressFormat.JPEG, quality -= 5, baos);
//        }
//        if (quality < 0) return null;
//        byte[] bytes = baos.toByteArray();
//        if (recycle && !src.isRecycled()) src.recycle();
//
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//        String standardImagePath = FileUtils.getImagePath(ApplicationContextHelper.getContext(), UUID.randomUUID().toString() + ".jpg");
//        BitmapUtil.saveBitmap(bitmap, new File(standardImagePath));
//        return standardImagePath;
//    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
}
