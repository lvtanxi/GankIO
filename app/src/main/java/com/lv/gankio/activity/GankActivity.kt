package com.lv.gankio.activity

import android.app.DatePickerDialog
import android.view.Menu
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.lv.gankio.R
import com.lv.gankio.adapter.LBaseFragmentAdapter
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.fragment.AboutFragment
import com.lv.gankio.fragment.DailyFragment
import com.lv.gankio.fragment.SortFragment
import com.lv.gankio.fragment.SubmitFragment
import com.lv.gankio.model.DateData
import com.lv.gankio.util.PublicTools
import com.lv.gankio.util.RxBus
import com.lv.gankio.util.getColorInt
import kotlinx.android.synthetic.main.activity_gank.*
import java.util.*

@Route(path = "/gank/home")
class GankActivity : BaseActivity() {

    private var mainMenu: Menu? = null
    private var currentFragmentIndex = -1
    private var datePickerDialog: DatePickerDialog? = null
    private var exitTime = 0L
    private var EXIT_TIME = 2000

    override fun loadLayoutId() = R.layout.activity_gank

    override fun initData() {
        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        common_view_pager.adapter = LBaseFragmentAdapter(supportFragmentManager, arrayOf(DailyFragment(), SortFragment(), SubmitFragment(), AboutFragment()))
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.navigation)
        navigationAdapter.setupWithBottomNavigation(bottom_navigation)
        bottom_navigation.defaultBackgroundColor = getColorInt(R.color.colorPrimary)
        bottom_navigation.accentColor = getColorInt(android.R.color.white)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        mainMenu = menu
        showOrHideMenu(R.id.action_filter, currentFragmentIndex == 1)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_datepicker ->
                showDatePickDialog()
            R.id.action_filter ->
                ARouter.getInstance().build("/gank/order").navigation()
            R.id.action_search ->
                ARouter.getInstance().build("/gank/search").navigation()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDatePickDialog() {
        if (datePickerDialog == null) {
            val calendar = Calendar.getInstance()
            datePickerDialog = DatePickerDialog(this@GankActivity,
                    // 绑定监听器
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        RxBus.post(DateData(year, month, dayOfMonth))
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))// 设置初始日期
        }
        datePickerDialog?.show()
    }

    override fun bindListener() {
        bottom_navigation.setOnTabSelectedListener { position, _ ->
            if (currentFragmentIndex != position) {
                currentFragmentIndex = position
                common_view_pager.setCurrentItem(position, false)
            }
            showOrHideMenu(R.id.action_filter, currentFragmentIndex == 1)
            showOrHideMenu(R.id.action_datepicker, currentFragmentIndex == 0)
            true
        }
        PublicTools.update(this)
    }

    private fun showOrHideMenu(menuId: Int, show: Boolean) {
        mainMenu?.findItem(menuId)?.isVisible = show
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > EXIT_TIME) {
            toastFailure("再按一次，将退出程序！")
            exitTime = System.currentTimeMillis()
            return
        }
        super.onBackPressed()
    }


}
