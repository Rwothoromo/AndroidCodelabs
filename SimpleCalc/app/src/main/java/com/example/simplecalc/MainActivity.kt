/*
 * Copyright 2018, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.simplecalc

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

/**
 * SimpleCalc is the initial version of SimpleCalcTest.  It has
 * a number of intentional oversights for the student to debug/fix,
 * including input validation (no input, bad number format, div by zero)
 *
 * In addition there is only one (simple) unit test in this app.
 * All the input validation and the unit tests are added as part of the lessons.
 *
 */
class MainActivity : Activity() {
    private var mCalculator: Calculator? = null
    private var mOperandOneEditText: EditText? = null
    private var mOperandTwoEditText: EditText? = null
    private var mResultTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the calculator class and all the views
        mCalculator = Calculator()
        mResultTextView = findViewById(R.id.operation_result_text_view)
        mOperandOneEditText = findViewById(R.id.operand_one_edit_text)
        mOperandTwoEditText = findViewById(R.id.operand_two_edit_text)
    }

    /**
     * OnClick method called when the add Button is pressed.
     */
    fun onAdd(view: View?) {
        compute(Calculator.Operator.ADD)
    }

    /**
     * OnClick method called when the subtract Button is pressed.
     */
    fun onSub(view: View?) {
        compute(Calculator.Operator.SUB)
    }

    /**
     * OnClick method called when the divide Button is pressed.
     */
    fun onDiv(view: View?) {
        try {
            compute(Calculator.Operator.DIV)
        } catch (iae: IllegalArgumentException) {
            Log.e(TAG, "IllegalArgumentException", iae)
            mResultTextView!!.text = getString(R.string.computationError)
        }
    }

    /**
     * OnClick method called when the multiply Button is pressed.
     */
    fun onMul(view: View?) {
        compute(Calculator.Operator.MUL)
    }

    private fun compute(operator: Calculator.Operator) {
        val operandOne: Double
        val operandTwo: Double
        try {
            operandOne = getOperand(mOperandOneEditText)
            operandTwo = getOperand(mOperandTwoEditText)
        } catch (nfe: NumberFormatException) {
            Log.e(TAG, "NumberFormatException", nfe)
            mResultTextView!!.text = getString(R.string.computationError)
            return
        }
        val result: String
        when (operator) {
            Calculator.Operator.ADD -> result = java.lang.String.valueOf(
                    mCalculator!!.add(operandOne, operandTwo))
            Calculator.Operator.SUB -> result = java.lang.String.valueOf(
                    mCalculator!!.sub(operandOne, operandTwo))
            Calculator.Operator.DIV -> result = java.lang.String.valueOf(
                    mCalculator!!.div(operandOne, operandTwo))
            Calculator.Operator.MUL -> result = java.lang.String.valueOf(
                    mCalculator!!.mul(operandOne, operandTwo))
            else -> result = getString(R.string.computationError)
        }
        mResultTextView!!.text = result
    }

    companion object {
        private const val TAG = "CalculatorActivity"

        /**
         * @return the operand value entered in an EditText as double.
         */
        private fun getOperand(operandEditText: EditText?): Double {
            val operandText = getOperandText(operandEditText)
            return java.lang.Double.valueOf(operandText)
        }

        /**
         * @return the operand text which was entered in an EditText.
         */
        private fun getOperandText(operandEditText: EditText?): String {
            return operandEditText!!.text.toString()
        }
    }
}
