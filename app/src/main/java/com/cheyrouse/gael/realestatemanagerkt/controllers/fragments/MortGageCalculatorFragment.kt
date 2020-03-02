package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


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
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener
import kotlinx.android.synthetic.main.fragment_mort_gage_calculator.*
import java.text.NumberFormat


/**
 * A simple [Fragment] subclass.
 */
class MortGageCalculatorFragment : Fragment() {

    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()

    private var purchaseAmount = 0.0
    private var downPaymentAmount = 0.0
    private var interestRate = 1.0
    private var value = 0.0
    private var duration = 1
    private var monthlyPayment: Double = 0.0

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

        loan_seekBar.setOnSeekbarChangeListener(OnSeekbarChangeListener { minValue ->
            textViewDuration.text = minValue.toString()
            val str: String = textViewDuration.text.toString()
            duration = str.toInt()
        })

        calculate.setOnClickListener {
            val dialog = AppCompatDialog(context)
            dialog.setContentView(R.layout.dialog)
            dialog.setTitle("Result")

            val monthlyPaymentTV =
                dialog.findViewById<View>(R.id.monthly_payment) as TextView?

            val totalPaymentTV =
                dialog.findViewById<View>(R.id.total_payment) as TextView?
            val monthlyStr:Double = getMontlyPayement()
            monthlyPaymentTV?.text = currencyFormat.format(monthlyStr)
            val totalStr:Double = getTotalPayment()
            totalPaymentTV?.text = currencyFormat.format(totalStr)

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
                            downPaymentAmount = value
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


    fun getMontlyPayement(): Double {
        val monthlyInterestRate: Double = interestRate / 1200
        this.monthlyPayment = purchaseAmount * monthlyInterestRate / (1 -
                (1 / Math.pow(1 + monthlyInterestRate, duration * 12.toDouble())))
        return monthlyPayment
    }

    fun getTotalPayment(): Double {
        return monthlyPayment * duration * 12
    }

}
