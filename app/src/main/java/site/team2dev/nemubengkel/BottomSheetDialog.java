package site.team2dev.nemubengkel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    @Nullable
    TextView tes, ulasan, jarak;
    RatingBar rating;
    ImageView gambar, direct;
    LinearLayout clickarea;
    String[] data;
    Bundle mArgs;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_detail_map, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mArgs = getArguments();
        data = mArgs.getString("data").split(",");
        double s_jarak=mArgs.getDouble("jarak");

        float jrating;
        if(data[3].equals("null")){
            jrating=0;
        }else{
            jrating=Float.valueOf(data[3]);
        }

        clickarea=(LinearLayout)view.findViewById(R.id.clickarea);
        tes=(TextView) view.findViewById(R.id.tes);
        gambar=(ImageView) view.findViewById(R.id.gambar);
        ulasan=(TextView)view.findViewById(R.id.ulasan);
        rating=(RatingBar)view.findViewById(R.id.rating);
        direct=(ImageView)view.findViewById(R.id.direction);
        jarak=(TextView)view.findViewById(R.id.jarak);

        tes.setText(data[1]);
        ulasan.setText(data[2]+" ulasan");
        rating.setRating((float)jrating/Integer.valueOf(data[2]));
         jarak.setText(new DecimalFormat("##.##").format(s_jarak)+" meter");

        Glide
                .with(getContext())
                .load(getString(R.string.base_url)+"asset/images/"+data[4])
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(80, 40)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(5)))
                .into(gambar);

        clickarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DetailBengkel.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",data[0]);
                intent.putExtra("namaBengkel",data[1]);
                intent.putExtra("urlImage",getString(R.string.base_url)+"asset/images/"+data[4]);
                getActivity().startActivity(intent);
            }
        });

        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Routes.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("lat",mArgs.getDouble("lat"));
                intent.putExtra("long",mArgs.getDouble("long"));
                getActivity().startActivity(intent);
            }
        });


    }
}
