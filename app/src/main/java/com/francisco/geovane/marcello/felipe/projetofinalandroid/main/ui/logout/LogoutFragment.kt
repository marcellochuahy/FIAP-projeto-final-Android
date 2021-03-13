package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.ui.logout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.login.LoginActivity
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.MainActivity
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.AnalyticsUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutFragment : Fragment() {

    private var bundle: Bundle = Bundle()
    private lateinit var analytics: FirebaseAnalytics

    private var appId: String = BuildConfig.APP_ID
    private var pageId: String = "Logout"

    private lateinit var logoutViewModel: LogoutViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        analytics = FirebaseAnalytics.getInstance(context)
        AnalyticsUtils.setPageData(analytics, bundle, appId, pageId)

        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(R.string.logout)
            .setCancelable(false)
            .setPositiveButton(R.string.txt_yes) { dialog, id ->
                AnalyticsUtils.setClickData(analytics, bundle, appId, pageId, "LogOut")
                auth.signOut()
                val currentUser: FirebaseUser? = auth.currentUser
                Log.i("USER", currentUser.toString())
                if (currentUser == null){
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

            }
            .setNegativeButton(R.string.txt_cancel) { dialog, id ->
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

        logoutViewModel = ViewModelProvider(this).get(LogoutViewModel::class.java)

//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        logoutViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }
}