package com.example.androidfeature.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

import java.util.Hashtable

const val initUrl = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMjgiIGhlaWdodD0iMTI4IiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTUgM2gxM2EzIDMgMCAwIDEgMyAzdjEzYTMgMyAwIDAgMS0zIDNINWEzIDMgMCAwIDEtMy0zVjZhMyAzIDAgMCAxIDMtM1ptMCAxYTIgMiAwIDAgMC0yIDJ2MTEuNTg2bDQuMjkzLTQuMjkzbDIuNSAyLjVsNS01TDIwIDE2VjZhMiAyIDAgMCAwLTItMkg1Wm00Ljc5MyAxMy4yMDdsLTIuNS0yLjVMMyAxOWEyIDIgMCAwIDAgMiAyaDEzYTIgMiAwIDAgMCAyLTJ2LTEuNTg2bC01LjIwNy01LjIwN2wtNSA1Wk03LjUgNmEyLjUgMi41IDAgMSAxIDAgNWEyLjUgMi41IDAgMCAxIDAtNVptMCAxYTEuNSAxLjUgMCAxIDAgMCAzYTEuNSAxLjUgMCAwIDAgMC0zWiIvPjwvc3ZnPg=="

const val failurl = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMjgiIGhlaWdodD0iMTI4IiB2aWV3Qm94PSIwIDAgNDggNDgiPjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iY3VycmVudENvbG9yIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS13aWR0aD0iNCIgZD0iTTEwIDQ0aDI4YTIgMiAwIDAgMCAyLTJWMTRIMzBWNEgxMGEyIDIgMCAwIDAtMiAydjM2YTIgMiAwIDAgMCAyIDJaTTMwIDRsMTAgMTBtLTIyIDhsMTIgMTJtMC0xMkwxOCAzNCIvPjwvc3ZnPg=="
object ZxingUtils {

    fun createBarCode(
        str: String?,
        type: BarcodeFormat?,
        codeWidth: Int,
        codeHeight: Int
    ): Bitmap? {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        val hints: Hashtable<EncodeHintType, Any> = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.MARGIN] = 1
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        var matrix: BitMatrix? = null
        try {
            matrix = MultiFormatWriter().encode(str, type, codeWidth, codeHeight, hints)
        } catch (e: Exception) {
        }
        if (matrix == null) {
            return null
        }
        val width: Int = matrix.width
        val height: Int = matrix.height
        //二维矩阵转为一维像素数组,也就是一直横着排了，然后给目标位置注入黑色信息
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = -0x1000000
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }


}