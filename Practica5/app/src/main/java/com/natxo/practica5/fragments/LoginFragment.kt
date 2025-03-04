package com.natxo.practica5.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.natxo.practica5.NoticiasActivity
import com.natxo.practica5.NoticiasApplication.Companion.db
import com.natxo.practica5.NoticiasApplication.Companion.prefs
import com.natxo.practica5.R
import com.natxo.practica5.database.entity.UsuarioEntity
import com.natxo.practica5.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLogin()
        register()
        checkPreferences()
    }

    private fun register() {
        binding.btnRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initLogin() {
        binding.btnLogin.setOnClickListener { userConfirmation() }
    }

    private fun userConfirmation() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            lifecycleScope.launch {
                val user = getUserEntity(username, password)
                if (user != null) {
                    savePreferences(user)
                    goToNoticias(user)
                } else {
                    Toast.makeText(requireContext(), R.string.login_missmatch, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Rellene los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToNoticias(user: UsuarioEntity?) {
        val intent = Intent(requireContext(), NoticiasActivity::class.java)
        intent.putExtra("Usuario", user)
        startActivity(intent)
    }

    private fun savePreferences(user: UsuarioEntity) {
        prefs.setName(user.email.toString())
        prefs.setPassword(user.contrasena.toString())
    }

    private fun checkPreferences() {
        lifecycleScope.launch {
            if (prefs.getName().isNotEmpty() && prefs.getPassword().isNotEmpty()) {
                val user = getUserEntity(prefs.getName(), prefs.getPassword())
                goToNoticias(user)
            }
        }
    }

    private suspend fun getUserEntity(email: String?, contrasena: String?): UsuarioEntity? {
        return withContext(Dispatchers.IO) {
            email?.let { e ->
                contrasena?.let { c -> db.usuarioDao().getUsuario(e.trim(), c) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
