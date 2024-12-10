package br.com.beatriz.exceptions

import java.lang.*
import kotlin.RuntimeException

class UnsupportedMathOperationException (exception: String?): RuntimeException(exception) {
}