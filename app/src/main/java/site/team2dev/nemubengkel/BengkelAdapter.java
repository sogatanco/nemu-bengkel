package site.team2dev.nemubengkel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class BengkelAdapter extends RecyclerView.Adapter<BengkelAdapter.ViewHolder> {

    private Context context;
    private List<Bengkel> list;
    private Bengkel bengkel;


    public BengkelAdapter(List<Bengkel> list, Context context) {
        this.list=list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_bengkel_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BengkelAdapter.ViewHolder viewHolder, int i) {

        bengkel=list.get(i);
        viewHolder.id=bengkel.getIdbengkel();
        viewHolder.nama=bengkel.getNamaBengkel();
        viewHolder.gambar=bengkel.gerUrlImage();
        viewHolder.mtextview.setText(bengkel.getNamaBengkel());
        Glide
                .with(context)
                .load(bengkel.gerUrlImage())
                .override(200,180)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.mgambar);

//              approve
        if(bengkel.getApproved()>0) {
            viewHolder.mapprove.setImageResource(R.drawable.approved);
        }else {
            viewHolder.mapprove.setImageResource(R.drawable.unapprove);
        }

//            kategori
        if(bengkel.getKategori() == 1){
            viewHolder.mkategori.setImageResource(R.drawable.ic_motorcycle_red_24dp);
        }
        else {
            viewHolder.mkategori.setImageResource(R.drawable.ic_directions_car_red_24dp);
        }

        float jrating;
        if(bengkel.getRating().equals("null")){
            jrating=0;
        }else {
            jrating=Float.valueOf(bengkel.getRating());
        }
        viewHolder.ratingBar.setRating((float)jrating/bengkel.getUlasan());

        viewHolder.ulasan.setText(String.valueOf(bengkel.getUlasan())+" ulasan");
        viewHolder.jarak.setText(bengkel.getJarak());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int id;
        private String nama;
        private String gambar;
        private TextView mtextview;
        private ImageView mgambar;
        private ImageView mapprove;
        private ImageView mkategori;
        private RatingBar ratingBar;
        private TextView ulasan;
        private TextView jarak;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mtextview=(TextView) itemView.findViewById(R.id.main_nama);
            mgambar=(ImageView)itemView.findViewById(R.id.main_gambar);
            mapprove=(ImageView)itemView.findViewById(R.id.main_approve);
            mkategori=(ImageView) itemView.findViewById(R.id.main_kategori);
            ratingBar=(RatingBar)itemView.findViewById(R.id.ratingBar);
            ulasan=(TextView)itemView.findViewById(R.id.julasan);
            jarak=(TextView)itemView.findViewById(R.id.jarak);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,DetailBengkel.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", String.valueOf(id));
            intent.putExtra("namaBengkel", nama);
            intent.putExtra("urlImage",gambar);
            context.startActivity(intent);

        }
    }
}
