package com.softechfoundation.municipal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.horizontalScrollMenuItem.ListItem;
import com.softechfoundation.municipal.horizontalScrollMenuItem.ListItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    private static String[] stateNames;
    private BottomNavigationView navigation;
    private RecyclerView recyclerView;
    private ListItemAdapter adapter;
    public static Button stateBtn, districtBtn, vdcBtn;
    public static AutoCompleteTextView searchBox;
    public static TextView catagories;
    private ArrayAdapter<String> autoComAdapter;
    private Button btnOldVdc, btnMetropolitian, btnSubMetropolitian, btnMunicipality, btnRuralMunicipality, btnAll;
    public static HorizontalScrollView horizontalScrollViewMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        initialization();

    }

    private void initialization() {
        recyclerView = findViewById(R.id.list_recycleriew);
        navigation = findViewById(R.id.navigation);
        stateBtn = findViewById(R.id.path_state_name);
        districtBtn = findViewById(R.id.path_district_name);
        vdcBtn = findViewById(R.id.path_vdc_name);
        searchBox = findViewById(R.id.search_box);
        catagories = findViewById(R.id.catagory);
        btnOldVdc = findViewById(R.id.btn_old_vdc);
        btnMetropolitian = findViewById(R.id.btn_metropolitian);
        btnSubMetropolitian = findViewById(R.id.btn_sub_metropolitian);
        btnMunicipality = findViewById(R.id.btn_municipality);
        btnRuralMunicipality = findViewById(R.id.btn_rural_municipality);
        btnAll=findViewById(R.id.btn_all);
        horizontalScrollViewMenu = findViewById(R.id.menu_horizontal_scroll_view);
        mainWork();
    }

    private void mainWork() {
        adapter = new ListItemAdapter(this, getData(), recyclerView);
        autoComAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stateNames);
        recyclerView.setAdapter(adapter);
        searchBox.setAdapter(autoComAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(adapter);
                searchBox.setHint("Search States...");
                stateBtn.setText("States");
                districtBtn.setVisibility(View.GONE);
                catagories.setText("States");
                stateBtn.setVisibility(View.VISIBLE);
                vdcBtn.setVisibility(View.GONE);
                catagories.setVisibility(View.VISIBLE);
                horizontalScrollViewMenu.setVisibility(View.GONE);
            }
        });
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnAll.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnAll.setTextColor(Color.WHITE);
            }
        });

        btnMetropolitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnMetropolitian.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnMetropolitian.setTextColor(Color.WHITE);
            }
        });
        btnSubMetropolitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnSubMetropolitian.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnSubMetropolitian.setTextColor(Color.WHITE);
            }
        });

        btnMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnMunicipality.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnMunicipality.setTextColor(Color.WHITE);
            }
        });
        btnRuralMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnRuralMunicipality.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnRuralMunicipality.setTextColor(Color.WHITE);
            }
        });

        btnOldVdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnOldVdc.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnOldVdc.setTextColor(Color.WHITE);
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                List<ListItem> newList = new ArrayList<>();
                for (ListItem list : getData()) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                adapter = new ListItemAdapter(MainPage.this, newList, recyclerView);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://jsonplaceholder.typicode.com/posts/1";

        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                   // textView.setText(jsonObject.toString(5));
                    Toast.makeText(MainPage.this, "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPage.this, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

    //End of Caching

    }

    private void SetAllBtnColorToDefault() {
        btnAll.setBackgroundColor(Color.TRANSPARENT);
        btnMetropolitian.setBackgroundColor(Color.TRANSPARENT);
        btnMunicipality.setBackgroundColor(Color.TRANSPARENT);
        btnRuralMunicipality.setBackgroundColor(Color.TRANSPARENT);
        btnSubMetropolitian.setBackgroundColor(Color.TRANSPARENT);
        btnOldVdc.setBackgroundColor(Color.TRANSPARENT);

        btnAll.setTextColor(getResources().getColor(R.color.black));
        btnMetropolitian.setTextColor(getResources().getColor(R.color.black));
        btnSubMetropolitian.setTextColor(getResources().getColor(R.color.black));
        btnMunicipality.setTextColor(getResources().getColor(R.color.black));
        btnRuralMunicipality.setTextColor(getResources().getColor(R.color.black));
        btnOldVdc.setTextColor(getResources().getColor(R.color.black));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history) {
            Toast.makeText(this, "There is no history yet.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public static List<ListItem> getData() {
        List<ListItem> listItems = new ArrayList<>();
        int[] icons = {R.drawable.map, R.drawable.ministry, R.drawable.globe, R.drawable.map, R.drawable.map, R.drawable.ministry, R.drawable.globe};
        String[] names = {"State 1", "State 2", "State 3", "State 4", "State 5", "State 6", "State 7"};
        stateNames = names;
        for (int i = 0; i < names.length && i < icons.length; i++) {
            ListItem current = new ListItem();
            current.setIcon(icons[i]);
            current.setName(names[i]);
            current.setType("state");
            listItems.add(current);
        }
        return listItems;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    stateBtn.setText("States");
                    stateBtn.setVisibility(View.VISIBLE);
                    districtBtn.setVisibility(View.GONE);
                    vdcBtn.setVisibility(View.GONE);
                    catagories.setVisibility(View.VISIBLE);
                    horizontalScrollViewMenu.setVisibility(View.GONE);
                    catagories.setText("States");
                    recyclerView.setAdapter(adapter);
                    searchBox.setAdapter(autoComAdapter);

                    searchBox.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String typedText = s.toString().toLowerCase();
                            List<ListItem> newList = new ArrayList<>();
                            for (ListItem list : getData()) {
                                String stateName = list.getName().toLowerCase();
                                if (stateName.contains(typedText)) {
                                    newList.add(list);
                                }
                            }
                            adapter = new ListItemAdapter(MainPage.this, newList, recyclerView);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    return true;
                case R.id.navigation_new_nepal:

                    return true;
                case R.id.navigation_detail_map:
                    return true;
                case R.id.navigation_ministry:

                    return true;
            }
            return false;
        }
    };


}
