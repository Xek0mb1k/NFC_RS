package com.xekombik.nfcrg

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.nfc.NfcManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xekombik.nfcrg.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val animCircle = binding.animationCircle.background as AnimationDrawable

        val manager = context?.getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter

        binding.sendDataButton.setOnClickListener {
            sendData()
            animCircle.start()
        }

        binding.readDataButton.setOnClickListener {
            readData()
            animCircle.start()
        }


        return binding.root
    }

    private fun sendData() {
        binding.animationCircle.visibility = VISIBLE
        binding.modeTextView.visibility = VISIBLE
        binding.modeTextView.text = getText(R.string.data_sending)
        binding.editText.hint = "Enter text..."
    }

    private fun readData() {
        binding.animationCircle.visibility = VISIBLE
        binding.modeTextView.visibility = VISIBLE
        binding.modeTextView.text = getText(R.string.data_reading)
        binding.editText.hint = "Data will be here"


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}