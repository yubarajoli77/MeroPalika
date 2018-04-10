package com.softechfoundation.municipal;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.android.volley.Request.Method.GET;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeperateDetailMapFragment extends Fragment {

private GridView gridView;
ImageAdapter mapImageCustomAdapter;
    public SeperateDetailMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_seperate_detail_map, container, false);
        defineView(view);
        return view;
    }

    private void defineView(View view) {
        gridView=view.findViewById(R.id.detail_map_gird_view);
       // callServerForImages();
        showImagesInGrid();
    }

    private void showImagesInGrid() {
        String [] names={"State 1","State 2","State 3","State 4", "State 5", "State 6", "State 7"};
        Integer[] images={R.drawable.state1,R.drawable.state2,
                R.drawable.state3,R.drawable.state4,R.drawable.state5,R.drawable.state6,R.drawable.state7};
         final ArrayList<GridImageViewPojo> gridImageList=new ArrayList<>();
        for (int i=0;i<names.length ;i++){
           GridImageViewPojo imageViewPojo=new GridImageViewPojo();
           imageViewPojo.setImage(images[i]);
           imageViewPojo.setName(names[i]);

           gridImageList.add(imageViewPojo);
        }

        mapImageCustomAdapter= new ImageAdapter(getActivity(),gridImageList);
        gridView.setAdapter(mapImageCustomAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer image=gridImageList.get(position).getImage();
                loadPhoto(image);
            }
        });
    }

//    private void callServerForImages(String parameter) {
//        final List<GridImageViewPojo> gridImageList=new ArrayList<>();
//        //Start Caching
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        String url = makeFinalUrl("http://192.168.100.237:8088/localLevel/rest/mountains/getMountains/",
//                parameter);
//
//        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                try {
//                    final String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers));
//                    //JSONObject jsonObject = new JSONObject(jsonString);
//                    JSONArray jsonArray = new JSONArray(jsonString);
//                    gridImageList.clear();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        GridImageViewPojo listItem = new GridImageViewPojo();
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        String resourceName = jsonObject1.getString("name");
//                        String gridImage=jsonObject1.getString("image");
//                        listItem.setName(resourceName);
//                        byte[] decodeValue = Base64.decode(gridImage, Base64.DEFAULT);
//                        Bitmap picture= BitmapFactory.decodeByteArray(decodeValue,0,decodeValue.length);
//                        listItem.setImage(picture);
//
//                        gridImageList.add(listItem);
//                    }
//
//                    mapImageCustomAdapter = new ResourceCustomAdapter(getActivity(),gridImageList);
//                    gridView.setAdapter(mapImageCustomAdapter);
//                    //resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//                } catch (UnsupportedEncodingException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(cacheRequest);
//
//        //End of Caching
//    }


    public class ImageAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<GridImageViewPojo> bitmapList;

        public ImageAdapter(Context context, ArrayList<GridImageViewPojo> bitmapList) {
            this.context = context;
            this.bitmapList = bitmapList;
        }

        @Override
        public int getCount() {
            return this.bitmapList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final GridImageViewPojo gridImageViewPojo= bitmapList.get(position);
            if (convertView == null) {
                final LayoutInflater inflater=LayoutInflater.from(context);
                convertView=inflater.inflate(R.layout.custom_design_grid_item,null);

                ImageView image=convertView.findViewById(R.id.grid_image);
                TextView name=convertView.findViewById(R.id.grid_image_name);

                final ViewHolder viewHolder=new ViewHolder(image,name);
                convertView.setTag(viewHolder);
            }
           final ViewHolder viewHolder= (ViewHolder) convertView.getTag();
            viewHolder.name.setText(gridImageViewPojo.getName());
            viewHolder.imageView.setImageResource(gridImageViewPojo.getImage());

            return convertView;
        }

    private class ViewHolder{
            ImageView imageView;
            TextView name;
            ViewHolder(ImageView imageView,TextView name){
                this.imageView=imageView;
                this.name=name;
            }
    }
    }
    private void loadPhoto(Integer pic) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.image_viewer);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.image_viewer,
                (ViewGroup)getActivity(). findViewById(R.id.image_viewer_framelayout));
        ImageView image = layout.findViewById(R.id.touch_image_view);
//       image.setImageDrawable(imageView.getDrawable());
//        image.getLayoutParams().height = height;
//        image.getLayoutParams().width = width;
//        mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        if (pic != null) {
            image.setImageResource(pic);
        } else {
            image.setImageResource(R.drawable.state1);
        }

        dialog.show();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.8f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }
}
