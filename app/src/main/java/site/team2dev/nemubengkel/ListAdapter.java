package site.team2dev.nemubengkel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Bengkel> list;
    private SlideUp slideUp;

    public ListAdapter (Context context, List<Bengkel> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_bengkel_item, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mtextview;
        private ImageView mgambar;
        private ImageView mapprove;
        private ImageView mkategori;
        private Bengkel bengkel;
        private int id;

        public ListViewHolder(View view){
            super(view);
            mtextview=(TextView) itemView.findViewById(R.id.main_nama);
            mgambar=(ImageView)itemView.findViewById(R.id.main_gambar);
            mapprove=(ImageView)itemView.findViewById(R.id.main_approve);
            mkategori=(ImageView) itemView.findViewById(R.id.main_kategori);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position){
            bengkel=list.get(position);
            id=bengkel.getIdbengkel();
            mtextview.setText(bengkel.getNamaBengkel());
            Glide
                    .with(context)
                    .load(bengkel.gerUrlImage())
                    .centerCrop()
                    .override(200,180)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mgambar);

//              approve
            if(bengkel.getApproved()>0) {
                mapprove.setImageResource(R.drawable.approved);
            }else {
                mapprove.setImageResource(R.drawable.unapprove);
            }

//            kategori
            if(bengkel.getKategori() == 1){
                mkategori.setImageResource(R.drawable.ic_motorcycle_red_24dp);
            }
            else {
                mkategori.setImageResource(R.drawable.ic_directions_car_red_24dp);
            }


        }

        public void onClick(View view){
            Toast.makeText(context,""+id,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,DetailBengkel.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("namaBengkel", bengkel.getNamaBengkel());
            intent.putExtra("urlImage",bengkel.gerUrlImage());
            context.startActivity(intent);
        }
    }
}
