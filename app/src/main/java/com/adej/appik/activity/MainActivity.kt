package com.adej.appik.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.adej.appik.R
import com.adej.appik.model.ClaimAdapter
import com.adej.appik.network.DataApi
import com.adej.appik.network.DataList
import com.adej.appik.util.AppPreference
import com.adej.appik.util.GlobalFunction
import com.adej.appik.util.RetrofitResultCallBack
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.support.v4.onRefresh

class MainActivity : AppCompatActivity() {

    private lateinit var claimAdapter: ClaimAdapter
    private lateinit var appPreference : AppPreference
    private lateinit var globalFunction: GlobalFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appPreference= AppPreference(this)
        globalFunction= GlobalFunction()


        setPreference()
        btn_input.setOnClickListener {
            val i = Intent(applicationContext, ClaimActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(i)
        }
        showList()
        swiperefresh.onRefresh {
            showList()
            swiperefresh.isRefreshing = false
        }
        globalFunction.checkPermisionAndroidM(this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_sign_out) {
            alert()
        }
        else if (id == R.id.action_stock) {
            val i = Intent(applicationContext, Profile::class.java)
            startActivity(i)

        }
        else if (id == R.id.action_search) {

        /**    val i = Intent(applicationContext, SearchActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(i)
        **/
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setPreference() {
        if (appPreference.user!!.isEmpty()) {
            startActivity(Intent(applicationContext, Authentication::class.java))
            finish()
        }
    }
    private fun showList() {
        val dataApi = DataApi(this)
        dataApi.fnGetData(appPreference.user,"fnGetDataClaim","data:null", object : RetrofitResultCallBack {

            override fun onSuccess(value: String) {
                val g = Gson()
                val p = g.fromJson<DataList>(value.toString(), DataList::class.java)
                val datas = p.getResults()
                claimAdapter = ClaimAdapter(applicationContext, datas!!)
                recycler_view.layoutManager = LinearLayoutManager(applicationContext)
                recycler_view.adapter = claimAdapter
                progress_bar.visibility= View.GONE
            }

            override fun onError(value: String) {
                runOnUiThread {
                    progress_bar.visibility= View.GONE
                }
            }

        })

    }
    fun alert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Peringatan")
        builder.setMessage("Anda yakin ingin Keluar dari Aplikasi ini ?")
        builder.setPositiveButton("Ya") { dialog, which ->
            // Launch settings, allowing user to make a change
            appPreference.user=""
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Tidak") { dialog, which ->
            // Launch settings, allowing user to make a change
        }

        builder.create().show()
    }
}