package site.team2dev.nemubengkel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilFragment extends Fragment implements MethodCaller{

UserSessionManager session;
Fungsi fungsi=new Fungsi();
private RequestQueue requestQueue;

private TextView nama, gender, jBengkel;
private ImageView profil;
private String username;
public List<Bengkel> bengkels;
public RecyclerView.Adapter listAdapter;
public RecyclerView recyclerView;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profil_fragment,container,false);



        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session=new UserSessionManager(getActivity().getApplicationContext());

        username=fungsi.getUsername( session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
        requestQueue= Volley.newRequestQueue(getActivity());

// data profil
        nama=(TextView)getView().findViewById((R.id.nama));
        gender=(TextView)getView().findViewById(R.id.gender);
        profil=(ImageView)getView().findViewById(R.id.profile_image);

        getUserData();

// list bengkel
        jBengkel=(TextView)getView().findViewById(R.id.jBengkel);
        recyclerView=(RecyclerView)view.findViewById(R.id.list_dafar_bengkel);
        bengkels=new ArrayList<>();
        getDataBengkel();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        if(isAdded()){listAdapter=new ListAdapter(getActivity(),bengkels, this::deleteBengkel);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }

    }


    private void getUserData(){

        final String URL=getString(R.string.base_url)+"api/general/user?token=1234567&email="+username;
        JsonObjectRequest getUserData=new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("data");
                            JSONObject data=array.getJSONObject(0);
                            if(isAdded()){
                                nama.setText(fungsi.isNull(data.getString("us_nama")).toUpperCase());
                                gender.setText(fungsi.isNull(data.getString("us_jk")).toUpperCase());
                                Glide
                                        .with(getContext())
                                        .load(getString(R.string.base_url)+"asset/images/"+fungsi.isImgProfilNull(data.getString("us_profil")))
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profil);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 1 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);

                }catch (UnsupportedEncodingException |JSONException e){
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        requestQueue.add(getUserData);
    }

    private void getDataBengkel(){
        final String URL=getString(R.string.base_url)+"api/bengkel?token="+session.getUserDetails().get(UserSessionManager.KEY_TOKEN);
        JsonObjectRequest getBengkel=new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("data");
                            jBengkel.setText(String.valueOf(array.length()));
                            for (int i=0;i<array.length();i++){
                                JSONObject data=array.getJSONObject(i);

                                    Bengkel bengkel=new Bengkel();
                                    bengkel.setNamaBengkel(data.getString("bk_namabengkel"));
                                    bengkel.setRating(data.getString("total_rating"));

                                    bengkel.setUrlImage(getString(R.string.base_url)+"asset/images/"+fungsi.isImgBengkelNull(data.getString("bk_foto")));


                                    bengkel.setApproved(data.getInt("bk_approved"));
                                    bengkel.setKategori(data.getInt("bk_kategori"));
                                    bengkel.setIdbengkel(data.getInt("bk_id"));
                                    bengkel.setUlasan(data.getInt("j_ulasan"));
                                    bengkels.add(bengkel);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("eror", error.toString());
            }
        });


        requestQueue.add(getBengkel);

    }


    @Override
    public void deleteBengkel(String id, int position) {
        bengkels.remove(position);
        recyclerView.removeViewAt(position);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, bengkels.size());
        listAdapter.notifyDataSetChanged();

        final String URL=getString(R.string.base_url)+"api/bengkel/delete";
        StringRequest delete=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "removed", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap=new HashMap<String, String>();
                hashMap.put("token", session.getUserDetails().get(UserSessionManager.KEY_TOKEN));
                hashMap.put("id", id);
                return hashMap;
            }
        };
        requestQueue.add(delete);
    }

}
