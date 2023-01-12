package com.example.sophosapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.sophos.userslogin.db_service
import com.example.sophos.userslogin.db_result
import com.example.sophosapp.SophosApplication.Companion.prefs
import com.example.sophosapp.databinding.FragmentLoginScreenBinding
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor


class login_screen : Fragment() {
    private lateinit var binding: FragmentLoginScreenBinding
    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    //--Button Enter--//
        binding.buttonEnter.setOnClickListener {
            Login()
        }

        //--Button FingerPrint--//
        binding.fingerprintBtn.setOnClickListener {
            fingerAuthentication()
        }


    }


    //--Process Login with api and button Enter--//
    private fun Login(){
        val email: EditText = binding.emailBox
        val password: EditText = binding.passwordBox
        var email_1 = email.text.toString()
        var password_1 = password.text.toString()


        db_service.service.listusers(email_1, password_1)
            .enqueue(object : retrofit2.Callback<db_result> {
                override fun onResponse(call: Call<db_result>, response: Response<db_result>) {
                    if (response.isSuccessful) {
                        var body = response.body()
                        if (body!!.access) {
                            Log.i("BODY", body.toString())
                            Toast.makeText(activity, "Welcome ${body.name}", Toast.LENGTH_SHORT)
                                .show()
                            activity!!.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.container, menu_screen())
                                commit()
                            }
                            prefs.saveName(body.name)
                            prefs.saveEmail(email_1)
                        } else {
                            Toast.makeText(activity, "Wrong Data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<db_result>, t: Throwable) {
                    call.cancel()
                }

            })
    }

    //--Process with FingerPrint--//
    private fun fingerAuthentication() {
        if (prefs.getName().isNotEmpty()) {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Cancel")
                .build()

            executor = ContextCompat.getMainExecutor(requireActivity())

            biometricPrompt =
                BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(activity, "Error $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                        activity!!.supportFragmentManager.beginTransaction().apply {
                            replace(R.id.container, menu_screen())
                            commit()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            biometricPrompt.authenticate(promptInfo)
        } else {
            Toast.makeText(activity, "Firts, login with your credentials", Toast.LENGTH_SHORT)
                .show()
        }

    }

}