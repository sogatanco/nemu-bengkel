package site.team2dev.nemubengkel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ProfilFragment extends Fragment {

 


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profil_fragment, null);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView=(ImageView)getView().findViewById(R.id.profile_image);
        String url="https://awsimages.detik.net.id/community/media/visual/2015/06/22/b00ec3fa-dbfa-4b4f-8111-38c72f80842c_169.jpg";

        Picasso.get().load(url).into(imageView);



    }

}
