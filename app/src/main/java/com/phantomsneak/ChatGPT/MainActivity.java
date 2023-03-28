package com.phantomsneak.ChatGPT;

import android.animation.*;
import android.annotation.SuppressLint;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import okhttp3.*;
import org.json.*;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private HashMap<String, Object> bjj = new HashMap<>();
	private ArrayList<HashMap<String, Object>> mappy = new ArrayList<>();
	private RecyclerView recyclerview1;
	private LinearLayout linear6;
	private ImageView imageview1;
	private EditText edittext1;
	
	private SharedPreferences data;
	private RequestNetwork call;
	private RequestNetwork.RequestListener _call_request_listener;
	private TimerTask t;
	private AlertDialog.Builder no_api_key;

	private String app_ver;
	private String ask;
	private String add_api;
	private String change_api;
	private String about;
	private String api;
	private String done;
	private String invalid_api;
	private String empty;
	private String no_api_key_title;
	private String no_api_key_message;
	private String no_api_key_ok;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		recyclerview1 = findViewById(R.id.recyclerview1);
		linear6 = findViewById(R.id.linear6);
		imageview1 = findViewById(R.id.imageview1);
		edittext1 = findViewById(R.id.edittext1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		call = new RequestNetwork(this);
		no_api_key = new AlertDialog.Builder(this);
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext1.getText().toString().equals("")) {
					showMessage(empty);
					return;
				}
				sendMessage(edittext1.getText().toString());
				edittext1.setText("");
			}
		});
		
		_call_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};

	}
	
	private void initializeLogic() {
		if (Locale.getDefault().toLanguageTag().contains("zh")){
			ask = getString(R.string.ask_tw);
			add_api = getString(R.string.add_api_tw);
			change_api = getString(R.string.change_api_tw);
			about = getString(R.string.about_tw);
			api = getString(R.string.api_tw);
			done = getString(R.string.done_tw);
			invalid_api = getString(R.string.invalid_api_tw);
			empty = getString(R.string.empty_tw);
			no_api_key_title = getString(R.string.no_api_key_title_tw);
			no_api_key_message = getString(R.string.no_api_key_message_tw);
			no_api_key_ok = getString(R.string.no_api_key_ok_tw);
		} else {
			ask = getString(R.string.ask);
			add_api = getString(R.string.add_api);
			change_api = getString(R.string.change_api);
			about = getString(R.string.about);
			api = getString(R.string.api);
			done = getString(R.string.done_tw);
			invalid_api = getString(R.string.invalid_api);
			empty = getString(R.string.empty);
			no_api_key_title = getString(R.string.no_api_key_title);
			no_api_key_message = getString(R.string.no_api_key_message);
			no_api_key_ok = getString(R.string.no_api_key_ok);
		}
		try{
			android.content.pm.PackageInfo pinfo = getPackageManager().getPackageInfo("com.phantomsneak.ChatGPT", PackageManager.GET_ACTIVITIES);
			app_ver = pinfo.versionName;
		} catch (Exception e){
		}
		edittext1.setHint(ask);
		OpenAiClient.apiKey = FileUtil.readFile("/data/data/com.phantomsneak.ChatGPT/files/User.ini");
		if (OpenAiClient.apiKey == ""){
			no_api_key.setTitle(no_api_key_title);
			no_api_key.setMessage(no_api_key_message);
			no_api_key.setCancelable(false);
			no_api_key.setPositiveButton(no_api_key_ok, null);
			no_api_key.create().show();
		}
		if (!data.getString("chat", "").equals("")) {
			mappy = new Gson().fromJson(data.getString("chat", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setStackFromEnd(true);
		// layoutManager.setReverseLayout(true);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview1.setLayoutManager(layoutManager);
		
		recyclerview1.setAdapter(new Recyclerview1Adapter(mappy));
		linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF444653));
		_navigationBarColor("#353541");
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		if (!(mappy.size() == 0)) {
			data.edit().putString("chat", new Gson().toJson(mappy)).commit();
		}
	}

	
	private void sendMessage(String userMsg) {
			//below line is to pass message to our array list which is entered by the user.
			addResponse(1, userMsg);
			OpenAiClient openAiClient = new OpenAiClient();
			openAiClient.generateResponse(userMsg, new Callback() {
					@Override
					public void onResponse(Call call, Response response) throws IOException {
							String responseBody = response.body().string();
							// Do something with the response body
							addResponse(0, OpenAiClient.parseResponse(responseBody));
							// addResponse(0, responseBody);
					}
					
					@Override
					public void onFailure(Call call, IOException e) {
							// Handle the failure
							addResponse(0, "Something went wrong!");
					}
			});
			
	}
	
	public void addResponse(final int who, final String response) {
			t = new TimerTask() {
					@Override
					public void run() {
							runOnUiThread(new Runnable() {
									@Override
									public void run() {
											bjj = new HashMap<>();
											bjj.put("who", (who == 0) ? "bot" : "you");
											bjj.put("text", response);
											mappy.add(bjj);
											recyclerview1.getAdapter().notifyItemInserted(recyclerview1.getAdapter().getItemCount()-1);
											recyclerview1.scrollToPosition(recyclerview1.getAdapter().getItemCount()-1);

									}
							});
					}
			};
			_timer.schedule(t, (int)(0));
	}
	
	
	/*使選項內Icon與文字並存*/
	private CharSequence menuIconWithText(Drawable r, String title) {
			r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
			SpannableString sb = new SpannableString("    " + title);
			ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
			sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return sb;
	}
	/*程式中新增MenuItem選項*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			/**itemId為稍後判斷點擊事件要用的*/
		    if (OpenAiClient.apiKey == ""){
			        menu.add(0,0,0,menuIconWithText(getDrawable(R.drawable.api),add_api));
			    } else {
			        menu.add(0,0,0,menuIconWithText(getDrawable(R.drawable.api),change_api));
			    }
		    menu.add(0,1,1,menuIconWithText(getDrawable(R.drawable.youtube),"Youtube"));
		    menu.add(0,2,2,menuIconWithText(getDrawable(R.drawable.github),"Github"));
			menu.add(0,3,3,menuIconWithText(getDrawable(R.drawable.about),about));
			//menu.add(0,3,3,menuIconWithText(getDrawable(R.drawable.more),"更多apps"));
			return super.onCreateOptionsMenu(menu);
	}
	/*此處為設置點擊事件*/
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
			/*取得Item的ItemId，判斷點擊到哪個元件*/
			switch (item.getItemId()){
					case 0:
			        AlertDialog.Builder builder = new AlertDialog.Builder(this);
			        LinearLayout layout = new LinearLayout(this);
			        layout.setOrientation(LinearLayout.VERTICAL);
			        final ImageView image = new ImageView(MainActivity.this);
					image.setImageResource(R.drawable.click_me);
			        layout.addView(image);
			        final EditText input = new EditText(this);
			        input.setHint("API key");
			        layout.addView(input);
			        builder.setTitle(api)
			           .setCancelable(true)
			           .setView(layout);
			        builder.setPositiveButton(done, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							if (!(input.getText().toString().startsWith("sk-"))){
								showMessage(invalid_api);
							} else {
								FileUtil.deleteFile("/data/data/com.phantomsneak.ChatGPT/files/User.ini");
								FileUtil.writeFile("/data/data/com.phantomsneak.ChatGPT/files/User.ini", input.getText().toString());
								OpenAiClient.apiKey = input.getText().toString();
							}

						}
				        });
			        image.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
							Intent browserintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://platform.openai.com/account/api-keys"));
							startActivity(browserintent);
					        }
					});
			        builder.show();
					break;
			        case 1:
					Intent browserintent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/UCrBaHnJwilrSZ87hGU4AVfg"));
					startActivity(browserintent1);
			        break;
			        case 2:
					Intent browserintent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KennyYang0726/ChatGPT"));
					startActivity(browserintent2);
			        break;
					case 3:
					AlertDialog.Builder about_dialog = new AlertDialog.Builder(this);
					about_dialog.setTitle(about)
							.setMessage("App_ver：" + app_ver)
							.setCancelable(true)
							.setPositiveButton("OK", null);
					about_dialog.create().show();
					break;
			}
			return super.onOptionsItemSelected(item);
	}
	

	public void _navigationBarColor(final String _color) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { getWindow().setNavigationBarColor(Color.parseColor(_color)); }
	}
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.message, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, @SuppressLint("RecyclerView") final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			if (_data.get((int)_position).get("who").toString().equals("you")) {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, Color.TRANSPARENT));
				imageview1.setImageResource(R.drawable.avater_2);
			}
			else {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF444653));
				imageview1.setImageResource(R.drawable.avater_1);
			}
			textview1.setText(_data.get((int)_position).get("text").toString());
			textview1.setOnLongClickListener(new View.OnLongClickListener(){
				@Override
				public boolean onLongClick(View view){
					((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", _data.get((int)_position).get("text").toString()));
					return true;
				}
			});
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}

	public void showMessage(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}


	public int getRandom(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
}
