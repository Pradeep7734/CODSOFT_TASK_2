package com.example.quoto;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.example.quoto.database.QOTDModel;
import com.example.quoto.database.QuoteDao;
import com.example.quoto.database.QuoteDatabase;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String quote = null, author = null;

    private final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    private BroadcastReceiver quoteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (QuoteReceiver.ACTION_QUOTE_RECEIVED.equals(intent.getAction())) {
                quote = intent.getStringExtra(QuoteReceiver.QUOTE);
                author = intent.getStringExtra(QuoteReceiver.AUTHOR);
            }
            sendDataToFragments(quote, author);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setFragmentsWithTabs();
        setAlarmManagerForQuoteUpdate();
        registerBroadcastReceiver();
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
    }

    private void setFragmentsWithTabs() {
        viewPagerAdapter.addFragment(new QuotoMainScreen(), "QOTD");
        viewPagerAdapter.addFragment(new SavedQuotoScreen(), "Saved");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setAlarmManagerForQuoteUpdate() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(MainActivity.this, QuoteReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                60000,
                pi
        );
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(QuoteReceiver.ACTION_QUOTE_RECEIVED);
        LocalBroadcastManager.getInstance(this).registerReceiver(quoteReceiver, filter);
    }

    private void sendDataToFragments(String quote, String author) {

        new DBThread(quote, author).start();
        QuotoMainScreen quotoMainScreen = (QuotoMainScreen) viewPagerAdapter.getItem(0);
        quotoMainScreen.updateQuote();
    }

    class DBThread extends Thread {

        String quote, author;
        DBThread(String quote, String author){
            this.quote = quote;
            this.author = author;
        }
        public void run() {
            super.run();

            QuoteDatabase db = Room.databaseBuilder(
                            getApplicationContext(),
                            QuoteDatabase.class,
                            "QuoteDB"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

            QuoteDao quoteDao = db.quoteDao();
            QOTDModel qotdModel = new QOTDModel(1, this.quote, this.author);
            quoteDao.insertQOTD(qotdModel);
        }
    }
}
