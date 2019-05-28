package site.team2dev.nemubengkel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.ViewHolder> {

    private List<Ulasan> list;
    private Context context;
    private Ulasan ulasan;

    public UlasanAdapter(List<Ulasan> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_komentar, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ulasan=list.get(i);
        viewHolder.user.setText(ulasan.getUser());
        viewHolder.tanggal.setText(ulasan.getDate());
        viewHolder.ulasan.setText(ulasan.getUlasan());
        viewHolder.rating.setRating((float)Float.valueOf(ulasan.getRating()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView user, tanggal, ulasan;
        RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user=(TextView)itemView.findViewById(R.id.user);
            tanggal=(TextView)itemView.findViewById(R.id.tanggal);
            ulasan=(TextView)itemView.findViewById(R.id.ulasan);
            rating=(RatingBar)itemView.findViewById(R.id.rating);


        }
    }

}
