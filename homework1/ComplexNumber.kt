class ComplexNumber(private var re: Double, private var im: Double=0.0) {

    override fun toString(): String {
        return "($re, $im)";
    }

    operator fun plus (other: ComplexNumber): ComplexNumber {
        return ComplexNumber(re + other.re, im + other.im)
    }
    operator fun minus (other: ComplexNumber): ComplexNumber {
        return ComplexNumber(re - other.re, im - other.im)
    }

    operator fun times (other: ComplexNumber): ComplexNumber {
        return ComplexNumber(re*other.re - im*other.im, re*other.im + im*other.re)
    }

    operator fun times (other: Double): ComplexNumber {
        return ComplexNumber(re*other,im*other)
    }

}