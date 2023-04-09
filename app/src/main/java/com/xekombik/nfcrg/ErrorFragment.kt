package com.xekombik.nfcrg

import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xekombik.nfcrg.databinding.FragmentErrorBinding
import kotlin.system.exitProcess


class ErrorFragment : Fragment() {

    private var _binding: FragmentErrorBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = context?.getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter

        if (adapter != null){
            binding.errorImage.setImageResource(R.drawable.error_nfc_off)
            binding.errorName.text = getText(R.string.nfc_disabled)
            binding.errorDescription.text = getText(R.string.enable_nfc_in_your_device_settings)
            binding.button.text = getText(R.string.enable_nfc)
            binding.button.setOnClickListener {
                startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
            }
        }else
            binding.button.setOnClickListener {
                exitProcess(0)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}