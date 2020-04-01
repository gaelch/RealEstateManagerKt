package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.FORMAT_TO_TWO_DEC
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.SYMBOL_DOLLAR
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.SYMBOL_EURO
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import kotlinx.android.synthetic.main.fragment_mort_gage_calculator.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow

/**
 * A simple [Fragment] subclass.
 */
class MortGageCalculatorFragment : Fragment() {

    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    private var purchaseAmount = 0.0
    private var contribution = 0.0
    private var interestRate = 1.0
    private var value = 0.0
    private var duration = 1
    private var isDollars: Boolean = true
    private var monthlyEuro = ""
    private var totalEuro = ""

    companion object {

        fun newInstance(): MortGageCalculatorFragment {
            return MortGageCalculatorFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mort_gage_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        editTextPurchasePrice.addTextChangedListener(
            getEditableTextWatcher(
                textViewPurchasePrice,
                "PP"
            )
        )
        editTextDownPaymentAmount.addTextChangedListener(
            getEditableTextWatcher(
                textViewDownPaymentAmount,
                "DPA"
            )
        )
        editTextInterestRate.addTextChangedListener(
            getEditableTextWatcher(
                textViewInterestRate,
                "IR"
            )
        )

        loan_seekBar.setOnSeekbarChangeListener { minValue ->
            textViewDuration.text = minValue.toString()
            val str: String = textViewDuration.text.toString()
            duration = str.toInt()
        }

        calculate.setOnClickListener {
            val dialog = AppCompatDialog(context)
            dialog.setContentView(R.layout.dialog)
            dialog.setTitle("Result")

            val monthlyPaymentTV =
                dialog.findViewById<View>(R.id.monthly_payment) as TextView?
            val totalPaymentTV =
                dialog.findViewById<View>(R.id.total_payment) as TextView?
            val monthlyStr: Double = getMonthlyPayment(
                interestRate,
                purchaseAmount,
                contribution,
                duration
            )
            monthlyPaymentTV?.text = FORMAT_TO_TWO_DEC.format(monthlyStr) + SYMBOL_DOLLAR
            val totalStr: Double = getTotalPayment(monthlyStr, duration)
            totalPaymentTV?.text = FORMAT_TO_TWO_DEC.format(totalStr) + SYMBOL_DOLLAR

            val dialogBtnConvert =
                dialog.findViewById<View>(R.id.dialogButtonConvert) as Button?
            dialogBtnConvert!!.setOnClickListener {
                if (isDollars) {
                    if (monthlyEuro.isEmpty()) {
                        monthlyEuro = (Utils.convertDollarToEuro(monthlyStr)).toString()
                        totalEuro = (Utils.convertDollarToEuro(totalStr)).toString()
                    }
                    monthlyPaymentTV?.text = monthlyEuro + SYMBOL_EURO
                    totalPaymentTV?.text = totalEuro + SYMBOL_EURO
                    dialogBtnConvert.setText(R.string.convertDollar)
                    isDollars = false
                } else {
                    monthlyPaymentTV?.text =
                        Utils.convertEuroToDollar(monthlyEuro.toDouble()).toString() + SYMBOL_DOLLAR
                    totalPaymentTV?.text =
                        Utils.convertEuroToDollar(totalEuro.toDouble()).toString() + SYMBOL_DOLLAR
                    dialogBtnConvert.setText(R.string.convertEuro)
                    isDollars = true
                }
            }


            val dialogButtonOk =
                dialog.findViewById<View>(R.id.dialogButtonOK) as Button?
            dialogButtonOk!!.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }
    }

    private fun getEditableTextWatcher(textView: TextView, type: String): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                try {
                    value = s.toString().toDouble() / 100.0
                    when (type) {
                        "PP" -> {
                            purchaseAmount = value
                            textView.text = currencyFormat.format(value)
                        }
                        "DPA" -> {
                            contribution = value
                            textView.text = currencyFormat.format(value)
                        }
                        "IR" -> {
                            interestRate = value
                            textView.text = value.toString()
                        }
                    }
                } catch (e: NumberFormatException) {
                    textView.text = ""
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }


    fun getMonthlyPayment(
        interestRate: Double,
        purchaseAmount: Double,
        contribution: Double,
        duration: Int
    ): Double {
        val monthlyInterestRate: Double = interestRate / 1200
        return (purchaseAmount - contribution) * monthlyInterestRate / (1 -
                (1 / (1 + monthlyInterestRate).pow(duration * 12.toDouble())))
    }

    fun getTotalPayment(monthlyPayment: Double, duration: Int): Double {
        return monthlyPayment * duration * 12
    }

}
