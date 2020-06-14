package net.vizja.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import net.vizja.calculator.databinding.MainBinding

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, MainViewModelProviderFactory(applicationContext))
            .get(MainViewModel::class.java)
    }

    private lateinit var mainBinding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainBinding>(this, R.layout.main).also {
            it.viewModel = mainViewModel
            it.lifecycleOwner = this
        }
    }
}