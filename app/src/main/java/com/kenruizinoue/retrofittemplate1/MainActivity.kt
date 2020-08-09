package com.kenruizinoue.retrofittemplate1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ApiServe by lazy { ApiService.create() }
    private var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            text.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            beginSearch(editText.text.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun beginSearch(srsearch: String) {
        disposable =
                ApiServe.hitCountCheck("query", "json", "search", srsearch)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> showResult(result.query.searchinfo.totalhits) },
                                { error -> showError(error.message) }
                        )
    }

    private fun showError(message: String?) {
        progressBar.visibility = View.GONE
        text.text = "An error has occurred"
    }

    private fun showResult(qty: Int) {
        progressBar.visibility = View.GONE
        text.text = qty.toString()
    }
}