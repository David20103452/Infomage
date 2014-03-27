/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This is the activity class responsible for configuration, creating and
 * handling actions in activity_main.xml. It is further developed based on the
 * NavigationDrawer demo of the Android Open Source Project. This page is the
 * main interface of this application.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	private static final String TAG = "id";
	public static final int RESULT_LOAD_IMAGE = 1;
	public static int INPUT_METHOD = 0;
	public static String OUTPUT_PATH = "";
	public static String CONFIG_PATH = Environment
			.getExternalStorageDirectory() + File.separator + "encoded1/";
	public static int MODE_FLAG = 0;
	public static int DECODE_FLAG = 0;
	public static String ALGORITHM = "F5Factory";
	public static long ALGORITHM_ID = 0;

	public void initialization() {

		String defaultPath = CONFIG_PATH;

		try {
			// Check for the default directory - encoded/, create if not exist
			File file = new File(defaultPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			// Check for test.txt in encoded/
			File file6 = new File(defaultPath + "test.txt");
			if (!file6.exists()) {
				file6.createNewFile();
			}

			// Check for the temp.jpg file in encoded/
			File file2 = new File(defaultPath + "temp.jpg");
			if (!file2.exists()) {
				file2.createNewFile();
			}

			// Check for the a.jpg file in encoded/
			File file3 = new File(defaultPath + "a.jpg");
			if (!file3.exists()) {
				file3.createNewFile();
			}

			// Check for the content.txt file in encoded/
			File file4 = new File(defaultPath + "content.txt");
			if (!file4.exists()) {
				file4.createNewFile();
			}

			File file7 = new File(defaultPath.replace("encoded1", "output"));
			if (!file7.exists()) {
				file7.mkdirs();
			}

			// Check for the configuration file, create if it does not exist
			File file5 = new File(defaultPath + "configuration.txt");
			if (!file5.exists()) {
				file5.createNewFile();

				// Write parameters to the configuration file
				BufferedWriter bw = new BufferedWriter(new FileWriter(file5));
				String toWrite = "CAMERA_NUMBER = 1\nOUTPUT_PATH = "
						+ defaultPath.replace("encoded1", "output");
				bw.write(toWrite);
				bw.close();
			}

			// Initialize the OUTPUT_PATH

			BufferedReader br = new BufferedReader(new FileReader(defaultPath
					+ "configuration.txt"));

			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("OUTPUT_PATH")) {
					String[] result = line.split("\\s");
					String path = result[result.length - 1];
					OUTPUT_PATH = path;
				}
			}
			br.close();
		} catch (Exception e) {
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();

		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		File file = new File(OUTPUT_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		initialization();
		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows the menu
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";
		public RadioGroup group;
		public EditText outputPath;
		public Button save;
		public ArrayAdapter adapter;
		public int spinnerItemId;
		Spinner spinner;
		public Activity a = this.getActivity();
		View rootView;
		String pictureName;

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			DECODE_FLAG = 0;
			rootView = inflater.inflate(R.layout.encode, container, false);
			rootView.findViewById(R.id.button1).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							MainActivity.MODE_FLAG = 0;

							int imageNum = 0;
							Intent imageIntent = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

							int pictureNumber = this.initiatePictureNumber();
							pictureName = "picture_" + pictureNumber + ".jpg";

							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(OUTPUT_PATH,
											pictureName)));
							startActivityForResult(intent, 1);

						}

						public int initiatePictureNumber() {
							int number = 0;
							int next = 0;
							StringBuffer buffer = new StringBuffer();

							// Read the picture number from configuration file
							try {
								BufferedReader br = new BufferedReader(
										new FileReader(CONFIG_PATH
												+ "configuration.txt"));

								String line = "";
								while ((line = br.readLine()) != null) {
									if (line.startsWith("CAMERA_NUMBER")) {
										String[] result = line.split("\\s");
										number = Integer
												.parseInt(result[result.length - 1]);
										buffer.append(line + "\n");
									} else {
										buffer.append(line + "\n");
									}
								}
								br.close();

								// Write the new number to the configuration
								// file
								next = number + 1;
								File file = new File(CONFIG_PATH
										+ "configuration.txt");
								BufferedWriter bw = new BufferedWriter(
										new FileWriter(file));

								String toWrite = buffer.toString().replace(
										"" + number, "" + next);
								bw.write(toWrite);
								bw.close();

							} catch (Exception e) {
							}
							return number;
						}

					});

			rootView.findViewById(R.id.button2).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							MainActivity.MODE_FLAG = 1;
							Intent i = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(i, RESULT_LOAD_IMAGE);

						}
					});

			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			Log.i(TAG, i + "");
			if (i == 1) {
				DECODE_FLAG = 1;
				MainActivity.MODE_FLAG = 1;
				// int i = getArguments().getInt(ARG_PLANET_NUMBER);
				rootView = inflater.inflate(R.layout.decode, container, false);
				rootView.findViewById(R.id.button1).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, RESULT_LOAD_IMAGE);

							}
						});

			}
			if (i == 2) {
				rootView = inflater.inflate(R.layout.option, container, false);
				group = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
				RadioButton radioButtonText = (RadioButton) rootView
						.findViewById(R.id.radio0);
				RadioButton radioButtonFile = (RadioButton) rootView
						.findViewById(R.id.radio1);
				if (INPUT_METHOD == 0)
					radioButtonText.setChecked(true);
				if (INPUT_METHOD == 1)
					radioButtonFile.setChecked(true);

				outputPath = (EditText) rootView.findViewById(R.id.editText1);
				outputPath.setText(OUTPUT_PATH);

				spinner = (Spinner) rootView.findViewById(R.id.spinner);
				adapter = ArrayAdapter.createFromResource(getActivity(),
						R.array.algorithms,
						android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
				spinner.setSelection((int) MainActivity.ALGORITHM_ID);
				spinner.setVisibility(View.VISIBLE);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						spinnerItemId = arg2;
						Log.i("spinner", adapter.getItem(arg2).toString() + " "
								+ arg2);
					}

					public void onNothingSelected(AdapterView<?> arg0) {

					}

				}

				);

				rootView.findViewById(R.id.save).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								String newPath = outputPath.getText()
										.toString();
								saveToConfiguration(newPath);

								int radioButtonId = group
										.getCheckedRadioButtonId();
								RadioButton radioButton = (RadioButton) rootView
										.findViewById(radioButtonId);
								Log.i(TAG, "" + radioButtonId);
								if (radioButton.getText().equals(
										"Input via keyboard"))
									INPUT_METHOD = 0;
								else
									INPUT_METHOD = 1;

								MainActivity.ALGORITHM = adapter.getItem(
										spinnerItemId).toString();
								MainActivity.ALGORITHM_ID = adapter
										.getItemId(spinnerItemId);

								Toast.makeText(getActivity(),
										"Saved!" + INPUT_METHOD,
										Toast.LENGTH_SHORT).show();

							}

							public void saveToConfiguration(String newPath) {
								try {
									// Check if the new path is legal
									if (!newPath.startsWith(Environment
											.getExternalStorageDirectory()
											.toString())) {
										this.showWrongDialog(newPath);
									} else {
										// Check if directory exist and create
										// if needed
										File destDir = new File(newPath);
										if (!destDir.exists()) {
											destDir.mkdirs();
										}

										// Read from configuration
										BufferedReader br = new BufferedReader(
												new FileReader(CONFIG_PATH
														+ "configuration.txt"));
										StringBuffer buffer = new StringBuffer(
												"");
										String line = "";
										String oldPath = "";
										while ((line = br.readLine()) != null) {
											if (line.startsWith("OUTPUT_PATH")) {
												String[] parts = line
														.split("\\s");
												oldPath = parts[parts.length - 1];
											}
											buffer.append(line + "\n");

										}
										br.close();

										// Write to the configuration file
										File file = new File(CONFIG_PATH
												+ "configuration.txt");
										BufferedWriter bw = new BufferedWriter(
												new FileWriter(file));
										String toWrite = buffer.toString()
												.replace(oldPath, newPath);
										bw.write(toWrite);
										bw.close();
										OUTPUT_PATH = newPath;
									}

								} catch (Exception e) {
								}
							}

							public void showWrongDialog(String newPath) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setMessage(newPath)
										.setCancelable(false)
										.setPositiveButton(
												"OK",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.cancel();
													}
												});
								AlertDialog alert = builder.create();
								alert.setTitle("Illegal path:");
								alert.show();
							}

						});

			}
			if (i == 3) {

				rootView = inflater.inflate(R.layout.help, container, false);
				rootView.findViewById(R.id.button2).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										Manual.class);
								startActivity(i);

							}
						});
				rootView.findViewById(R.id.button1).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										About.class);
								startActivity(i);

							}
						});
				rootView.findViewById(R.id.button3).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										License.class);
								startActivity(i);

							}
						});

			}
			if (i == 4) {
				System.exit(0);
			}

			return rootView;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
					&& null != data && MODE_FLAG == 1) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = this.getActivity().getContentResolver()
						.query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				Log.i(TAG, picturePath);
				Intent intent = new Intent();
				intent.setClass(this.getActivity(), ImageViewer.class);
				intent.putExtra("picturePath", picturePath);
				startActivity(intent);
			}
			if (resultCode == Activity.RESULT_OK && MODE_FLAG == 0) {

				String fileName = OUTPUT_PATH + pictureName;
				Log.i(TAG, fileName);
				Intent intent = new Intent();
				intent.setClass(this.getActivity(), ImageViewer.class);
				intent.putExtra("picturePath", fileName);
				startActivity(intent);
			}

		}

	}
}