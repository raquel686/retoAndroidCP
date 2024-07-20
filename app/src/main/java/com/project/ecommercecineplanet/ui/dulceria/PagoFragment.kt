package com.project.ecommercecineplanet.ui.dulceria

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.ecommercecineplanet.R
import com.project.ecommercecineplanet.data.model.AdditionalValues
import com.project.ecommercecineplanet.data.model.CompleteDto
import com.project.ecommercecineplanet.data.model.CreditCard
import com.project.ecommercecineplanet.data.model.Payer
import com.project.ecommercecineplanet.data.model.Transaction
import com.project.ecommercecineplanet.data.model.TxValue
import com.project.ecommercecineplanet.databinding.FragmentDulceriaBinding
import com.project.ecommercecineplanet.databinding.FragmentPagoBinding
import com.project.ecommercecineplanet.domain.model.OrderDomainModel
import com.project.ecommercecineplanet.domain.model.TransactionDomainModel
import com.project.ecommercecineplanet.domain.model.toDomain
import com.project.ecommercecineplanet.utils.Constants.API_KEY
import com.project.ecommercecineplanet.utils.Constants.MERCHAN_ID
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.codec.digest.DigestUtils.md5
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.security.MessageDigest

@AndroidEntryPoint
class PagoFragment : Fragment() {

    private var _binding:FragmentPagoBinding?=null
    private val viewModel: DulceriaViewModel by activityViewModels()
    private val binding get() = _binding!!
    private val arg:PagoFragmentArgs by navArgs()
    private lateinit var loadingScreen: LinearLayout
    private var total=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPagoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        total=arg.total
        loadingScreen=binding.loadingScreen

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
            uiState.errorMessage?.let { message ->
                showError(message)
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is DulceriaViewModel.UIEvent.TransaccionExitosa->{
                        Toast.makeText(requireContext(), "Esperando confirmación....", Toast.LENGTH_SHORT).show()
                        viewModel.enviarConfirmacion(CompleteDto(
                            binding.nombreTitularEdit.text.toString(),
                            binding.correoTitularEdit.text.toString(),
                            binding.numDocumentoEditText.text.toString(),
                            uiEvent.operation
                        ))
                    }
                    DulceriaViewModel.UIEvent.ConfirmacionExitosa->{
                        showCompraCorrecta()
                    }
                    else->{}
                }
            }
        }
        loadUserData()
        setupDatePicker()
        setupDocumentTypeDropdown()

        binding.totalPagar.text="Total: s/${total}"
        binding.pagarButton.setOnClickListener {
            pagar()
        }

        return root
    }

    private fun showLoading() {
        loadingScreen.visibility=View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        loadingScreen.visibility=View.GONE

    }

    fun md5(input: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
    fun generateAndEncryptString(referenceCode: String, txValue: String): String {
        val apiKey =API_KEY
        val merchantId = MERCHAN_ID
        val currency = "PEN"

        val data = "$apiKey~$merchantId~$referenceCode~$txValue~$currency"
        Log.i("encriptadoMD5", md5(data))

        return md5(data)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTimeInFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return ZonedDateTime.now().format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pagar(){
        var additionalValues=AdditionalValues(TxValue(arg.total.toDouble(),"PEN"))

        val referenceCode=getCurrentDateTimeInFormat()
        val method=identifyCardType(binding.numTarjetaEdit.text.toString())
        var order=OrderDomainModel(
            referenceCode,
            "Transacción ${getCurrentDateTimeInFormat()}",
            generateAndEncryptString(referenceCode,total.toDouble().toString()),
            additionalValues
            )
        var payer=Payer(
            "1",
            binding.nombreTitularEdit.text.toString(),
            binding.correoTitularEdit.text.toString(),
            "123456789",
            binding.numDocumentoEditText.text.toString()
        )
        var creditCard= CreditCard(
            binding.numTarjetaEdit.text.toString(),
            binding.cvvEdit.text.toString(),
            binding.fecVencimientoEdit.text.toString(),
            "APPROVED"
        )
        var transaction=TransactionDomainModel(order,payer,creditCard,
            method,
            "vghs6tvkcle931686k1900o6e1",
            "127.0.0.1"
            )
        Log.i("transaccion",transaction.toString())
        viewModel.enviarTransacecion(transaction)
    }
    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM", Locale.getDefault())

        binding.fecVencimientoEdit.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, _ ->
                    calendar.set(year, month, 1) // Establece el primer día del mes seleccionado
                    binding.fecVencimientoEdit.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    fun identifyCardType(cardNumber: String): String {
        return when {
            cardNumber.startsWith("4") && (cardNumber.length == 13 || cardNumber.length == 16) -> "VISA"
            cardNumber.startsWith("5") && (cardNumber.length == 16) -> "MASTERCARD"
            cardNumber.startsWith("34") || cardNumber.startsWith("37") && cardNumber.length == 15 -> "AMEX"
            else -> "Unknown"
        }
    }

    private fun showCompraCorrecta() {
        AlertDialog.Builder(requireContext())
            .setTitle("Compra correcta")
            .setMessage("Ha realizado la compra correctamente")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                requireActivity().onBackPressed()
            }
            .setCancelable(false)
            .show()
    }


    private fun loadUserData() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDefaults", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", "")
        val userName = sharedPreferences.getString("userName", "")
        binding.correoTitularEdit.text = Editable.Factory.getInstance().newEditable(userEmail)
        binding.nombreTitularEdit.text = Editable.Factory.getInstance().newEditable(userName)
    }

    private fun setupDocumentTypeDropdown() {
        val documentTypes = arrayOf("CE", "DNI", "RUC")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, documentTypes)
        binding.tipoDocumentoAutocomplete.setAdapter(adapter)
    }
}