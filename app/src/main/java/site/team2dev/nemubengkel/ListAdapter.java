package site.team2dev.nemubengkel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Bengkel> list;
    private MethodCaller methodCaller;

    public ListAdapter (Context context, List<Bengkel> list, MethodCaller methodCaller){
        this.context=context;
        this.list=list;
        this.methodCaller=methodCaller;
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

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
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
            itemView.setOnCreateContextMenuListener(this);
        }
        public void bindView(int position){
            bengkel=list.get(position);
            id=bengkel.getIdbengkel();
            mtextview.setText(bengkel.getNamaBengkel());
            Glide
                    .with(context)
                    .load(bengkel.gerUrlImage())
                    .override(200,180)
                    .centerCrop()
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           MenuItem edit= menu.add(this.getAdapterPosition(),id, 0,"Edit Bengkel");
           MenuItem hapus=menu.add(this.getAdapterPosition(),id, 0,"Delete Bengkel");
           hapus.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                   DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           switch (which){
                               case DialogInterface.BUTTON_POSITIVE:
                                   methodCaller.deleteBengkel(String.valueOf(id), getAdapterPosition());
                                   break;

                               case DialogInterface.BUTTON_NEGATIVE:
                                   //No button clicked
                                   break;
                           }
                       }
                   };

                   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                           .setNegativeButton("No", dialogClickListener).show();
                   return false;
               }
           });

           edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                   Intent intent = new Intent(context,EditBengkel.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("id", id);
                   context.startActivity(intent);
                   return false;
               }
           });


        }


    }

}
