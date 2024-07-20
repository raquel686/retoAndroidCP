package com.project.ecommercecineplanet.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.project.ecommercecineplanet.R
import com.project.ecommercecineplanet.databinding.FragmentLoginBinding
import com.project.ecommercecineplanet.ui.home.HomeFragmentDirections
import com.project.ecommercecineplanet.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleCard:MaterialCardView
    private lateinit var sesionIniciada:LinearLayout
    private lateinit var invitado:MaterialCardView
    private lateinit var cerrarSesion:MaterialCardView


    private val REQ_ONE_TAP = 2  // Identificador único para la actividad de autenticación


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        googleCard=binding.googleCardView
        sesionIniciada=binding.sesionIniciadoPantalla
        invitado=binding.invitadoCardView
        cerrarSesion=binding.cerrarSesion

        auth = Firebase.auth
        oneTapClient = Identity.getSignInClient(requireActivity())


        val sharedPreferences = requireActivity().getSharedPreferences("UserDefaults", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val currentUser = if (isLoggedIn) Firebase.auth.currentUser else null
        updateUI(currentUser)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(false)
            .build()

        googleCard.setOnClickListener {
            startSignIn()
        }
        cerrarSesion.setOnClickListener {
            cerrarSesion()
        }
        invitado.setOnClickListener {
            navigateToDulceria()
        }


        return root
    }

    private fun startSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "One Tap sign-in failed: ${e.localizedMessage}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ONE_TAP) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "signInWithCredential:success")
                                    val user = auth.currentUser
                                    user?.let {
                                        saveUserData(it)
                                        showWelcomeDialog(it.displayName ?: "Usuario")
                                    }
                                    updateUI(user)
                                } else {
                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                                    updateUI(null)
                                }
                            }
                    }
                    else -> {
                        Log.d(TAG, "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Sign-in failed: ${e.localizedMessage}")
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            googleCard.visibility = View.GONE
            invitado.visibility = View.GONE
            sesionIniciada.visibility=View.VISIBLE
            binding.textView3.visibility=View.GONE

        } else {
            googleCard.visibility = View.VISIBLE
            invitado.visibility = View.VISIBLE
            sesionIniciada.visibility = View.GONE
            binding.textView3.visibility=View.VISIBLE
        }
    }

    private fun cerrarSesion(){
        Firebase.auth.signOut()
        val sharedPreferences = requireActivity().getSharedPreferences("UserDefaults", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            putString("userEmail", "")
            putString("userName", "")
            apply()
        }
        updateUI(null)
    }

    private fun saveUserData(user: FirebaseUser) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDefaults", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("userEmail", user.email)
            putString("userName", user.displayName)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    private fun showWelcomeDialog(userName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Bienvenido")
            .setMessage("Hola, $userName")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                navigateToDulceria()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToDulceria(){
        val action = LoginFragmentDirections.actionNavigationNotificationsToNavigationDashboard()
        findNavController().navigate(action)
    }
    companion object {
        private const val TAG = "YourFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}