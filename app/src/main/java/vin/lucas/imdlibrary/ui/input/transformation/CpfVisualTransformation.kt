package vin.lucas.imdlibrary.ui.input.transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CpfVisualTransformation : VisualTransformation {
    private companion object DefaultOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) {
                return offset
            }

            if (offset <= 6) {
                return offset + 1
            }

            if (offset <= 9) {
                return offset + 2
            }

            if (offset <= 11) {
                return offset + 3
            }

            return 14
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 3) {
                return offset
            }

            if (offset <= 7) {
                return offset - 1
            }

            if (offset <= 11) {
                return offset - 2
            }

            if (offset <= 14) {
                return offset - 3
            }

            return 11
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }

        val result = buildString {
            append(digits.take(3))

            if (digits.length > 3) {
                append(".")
                append(digits.drop(3).take(3))
            }

            if (digits.length > 6) {
                append(".")
                append(digits.drop(6).take(3))
            }

            if (digits.length > 9) {
                append("-")
                append(digits.drop(9).take(2))
            }
        }

        return TransformedText(
            AnnotatedString(result),
            offsetMapping = DefaultOffsetMapping,
        )
    }
}
