package com.lukelorusso.moneydivider.scenes.base.view

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.moneydivider.AndroidApplication
import com.lukelorusso.moneydivider.di.components.ActivityComponent
import com.lukelorusso.moneydivider.di.components.ApplicationComponent

abstract class ABaseActivity : AppCompatActivity() {

    private val applicationComponent: ApplicationComponent by lazy { (application as AndroidApplication).appComponent }

    val activityComponent: ActivityComponent by lazy { applicationComponent.activityComponent().create(this) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toolbar back arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
