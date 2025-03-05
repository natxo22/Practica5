package com.natxo.practica5.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.natxo.practica5.NoticiasApplication.Companion.db
import com.natxo.practica5.R
import com.natxo.practica5.databinding.FragmentRegisterBinding
import com.natxo.practica5.database.entity.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newUser()
    }

    private fun newUser() {
        binding.btnRegister.setOnClickListener {
            val nombre = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val contrasena = binding.etPassword.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val exist = db.usuarioDao().getExistEmailBoolean(email)
                withContext(Dispatchers.Main) {
                    if (!exist) {
                        saveUser(email, nombre, contrasena)
                        goToLogin()
                    } else {
                        Toast.makeText(requireContext(), "Email en uso", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun goToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }

    private fun saveUser(email: String, nombre: String, contrasena: String) {
        val user = UsuarioEntity(email = email, nombre = nombre, contrasena = contrasena)
        lifecycleScope.launch(Dispatchers.IO) {
            db.usuarioDao().setUsuario(user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
