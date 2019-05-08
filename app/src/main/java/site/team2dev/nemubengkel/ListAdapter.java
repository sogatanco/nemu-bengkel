package site.team2dev.nemubengkel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Bengkel> list;

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
        private TextView mkategori;
        private ImageView mgambar;

        public ListViewHolder(View view){
            super(view);
            mtextview=(TextView) itemView.findViewById(R.id.main_nama);
            mkategori=(TextView) itemView.findViewById(R.id.main_kategori);
            mgambar=(ImageView)itemView.findViewById(R.id.main_gambar);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position){
            Bengkel bengkel=list.get(position);
            mtextview.setText(bengkel.getNamaBengkel());
            mkategori.setText(""+bengkel.getRating());
            Glide
                    .with(context)
                    .load(bengkel.gerUrlImage())
                    .centerCrop()
                    .override(200,180)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mgambar);
        }

        public void onClick(View view){

        }
    }
}
