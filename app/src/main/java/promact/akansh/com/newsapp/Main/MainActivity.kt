package promact.akansh.com.newsapp.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import promact.akansh.com.newsapp.Adapter.ViewPagerAdapter
import promact.akansh.com.newsapp.News.GeneralNews
import promact.akansh.com.newsapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var general: GeneralNews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs)

        setSupportActionBar(toolbar)
        general = GeneralNews()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupViePager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when {
                    tab!!.position == 0 -> {
                        GeneralNews().setSource("the-times-of-india")
                    }
                    tab.position == 1 -> {
                        GeneralNews().setSource("espn")
                    }
                    tab.position == 2 -> {
                        GeneralNews().setSource("buzzfeed")
                    }
                    tab.position == 3 -> {
                        GeneralNews().setSource("politico")
                    }
                    tab.position == 4 -> {
                        GeneralNews().setSource("techcrunch")
                    }
                    tab.position == 5 -> {
                        GeneralNews().setSource("business-insider")
                    }
                    tab.position == 6 -> {
                        GeneralNews().setSource("medical-news-today")
                    }
                }
            }
        })
    }

    private fun setupViePager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(GeneralNews(), "General News (India)")
        adapter.addFragment(GeneralNews(), "Sports News")
        adapter.addFragment(GeneralNews(), "Entertainment News")
        adapter.addFragment(GeneralNews(), "Political News")
        adapter.addFragment(GeneralNews(), "Technological News")
        adapter.addFragment(GeneralNews(), "Business News")
        adapter.addFragment(GeneralNews(), "Health News")
        viewPager.adapter = adapter
    }
}
