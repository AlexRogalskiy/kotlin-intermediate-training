package com.rockthejvm.practice

interface BlendMode {
    fun combine(fg: Color, bg: Color): Color
}

class Transparency(f: Double): BlendMode {
    val factor = f.coerceIn(0.0..1.0)

    override fun combine(fg: Color, bg: Color): Color =
        fg * factor + bg * (1 - factor)
}

class Multiply: BlendMode {
    override fun combine(fg: Color, bg: Color): Color =
        Color(
            (fg.red * bg.red / 255.0).toInt(),
            (fg.green * bg.green / 255.0).toInt(),
            (fg.blue * bg.blue / 255.0).toInt(),
        )
}