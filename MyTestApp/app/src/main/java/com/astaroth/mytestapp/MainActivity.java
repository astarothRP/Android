package com.astaroth.mytestapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager m = this.getSupportFragmentManager();
		m.beginTransaction().replace(R.id.activity_container_framelayout, new MyMainFragment()).commit();
		//this.getSupportFragmentManager().beginTransaction().commit(); putFragment(new Bundle(), "KEY", new MyMainFragment());
	}




}
