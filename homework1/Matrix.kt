class Matrix {
    private var matrix: Array<Array<ComplexNumber>>
    set(value) {
        if (value.isEmpty() || value[0].isEmpty()) {
            throw IllegalArgumentException("Matrix must not be empty")
        }
        val nColumn = value[0].size
        for (row in value){
            if (row.size != nColumn) {
                throw IndexOutOfBoundsException("Incorrect size of matrix")
            }
        }
        field = value
        rows = value.size
        columns = value[0].size
    }

    private var rows: Int = 0
    private var columns: Int = 0

    constructor(matrix: Array<Array<ComplexNumber>>){
        this.matrix = matrix
    }

    operator fun plus (matrix2: Matrix): Matrix {
        val result : Array<Array<ComplexNumber>> = matrix.copyOf()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[i][j] += matrix2.matrix[i][j]
            }
        }
        return Matrix(result)
    }

    operator fun times (matrix2: Matrix): Matrix {
        val result : Array<Array<ComplexNumber>> = Array(rows) { Array(matrix2.columns) {ComplexNumber(0.0,0.0)} }
        for (i in 0 until rows) {
            for (j in 0 until matrix2.columns) {
                for (k in 0 until columns) {
                    result[i][j] += matrix[i][k] * matrix2.matrix[k][j]
                }
            }
        }
        return Matrix(result)
    }

    override fun toString(): String {
        var result = ""
        for (row in matrix) {
            for (cell in row) {
                result += "$cell "
            }
            result += "\n"
        }
        return result.substringBeforeLast("\n")
    }

    fun transpose(){
        rows = columns.also { columns = rows }
        val result : Array<Array<ComplexNumber>> = Array(rows) { Array(columns) {ComplexNumber(0.0,0.0)} }
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[i][j] = matrix[j][i]
            }
        }
        matrix = result
    }

    private fun matrixWithoutRowAndColumn(size: Int, column: Int): Matrix {
        val temp = Matrix(Array(size-1) {Array(size-1){ComplexNumber(0.0)} })
        var tempColumn = 0
        for (j in 0 until size) {
            if (j == column) {
                continue
            }
            for (i in 1 until size) {
                temp.matrix[i - 1][tempColumn] = matrix[i][j]
            }
            tempColumn++
        }
        return temp
    }

    fun determinant(): ComplexNumber { //разложение по первой строке
        if (rows != columns){
            throw IllegalArgumentException("Matrix must be square")
        }
        if (rows == 1) {
            return matrix[0][0]
        } else if (rows == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        } else {
            var result = ComplexNumber(0.0)
            var temp = 1.0
            for (i in 0 until columns) {
                result += matrix[0][i] * matrixWithoutRowAndColumn(rows, i).determinant() * temp
                temp *= -1.0
            }
            return result
        }
    }
}